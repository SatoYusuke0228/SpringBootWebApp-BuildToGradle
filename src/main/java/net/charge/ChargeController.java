package net.charge;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import net.cart.Cart;
import net.cart.CartItem;
import net.charge.ChargeRequest.Currency;
import net.checkout.Checkout;
import net.product.TrProductDeleteAndUpdateService;
import net.product.TrProductEntity;
import net.product.TrProductSelectService;
import net.sales_history.ChangeProductHistoryStatus;
import net.sales_history.TrSalesHistoryEntity;
import net.sales_history.TrSalesHistoryService;
import net.sales_history.TrSalesProductHistoryEntity;
import net.sales_history.TrSalesProductHistoryService;

@Controller
public class ChargeController {

	@Autowired
	private TrProductSelectService productSelectService;

	@Autowired
	private TrProductDeleteAndUpdateService productDeleteAndUpdateService;

	@Autowired
	private TrSalesHistoryService salesHistoryService;

	@Autowired
	private TrSalesProductHistoryService salesProductHistoryService;

	@Autowired
	private TrChargeHistoryService chargeHistorySarvice;

	@Autowired
	private StripeService paymentsService;

	//セッションスコープのインスタンス
	@Autowired
	private HttpSession session;

	@Autowired
	private ChangeProductHistoryStatus changeProductHistoryStatus;

	/**
	 * 販売した商品を商品テーブルから減算処理して
	 * 販売履歴テーブルに１件の販売履歴を作成するメソッド
	 *
	 * @param cart
	 * @param charge
	 * @param chargeRequest
	 * @param checkout
	 * @return 作成された１件の販売履歴
	 */
	private TrSalesHistoryEntity createSalesHistory(
			Cart cart,
			ChargeRequest chargeRequest,
			Checkout checkout) {

		//販売履歴オブジェクトを作成してDBに保存する
		TrSalesHistoryEntity salesHistoryEntity = new TrSalesHistoryEntity(
				cart, checkout, new Timestamp(System.currentTimeMillis()));

		salesHistoryService.saveSalesHistory(salesHistoryEntity);

		return salesHistoryEntity;
	}

	/**
	 * １件の販売履歴を元に販売商品の種類数分の販売商品履歴を作成する
	 *
	 * @param salesHistoryEntity
	 * @param cart
	 * @return salesProductHistoryEntity 販売した商品種類数ごとの販売商品履歴List
	 */
	private List<TrSalesProductHistoryEntity> createSalesProductHistory(
			TrSalesHistoryEntity salesHistoryEntity,
			Cart cart) {

		//販売商品履歴を商品種類ごとに格納するListを作成
		List<TrSalesProductHistoryEntity> salesProductHistoryEntity = new ArrayList<>();

		//カートから売れた商品を取得
		Map<String, CartItem> soldItems = new HashMap<>();
		soldItems = cart.getCartItems();

		//売れた商品ごとに販売商品履歴Listに格納していく処理
		for (CartItem soldItem : soldItems.values()) {

			//販売商品履歴Listに格納していく
			final TrSalesProductHistoryEntity salesProductHistory = new TrSalesProductHistoryEntity(
					salesHistoryEntity.getSalesHistoryId(), soldItem);

			salesProductHistoryEntity.add(salesProductHistory);
		}

		//販売商品を格納したListをすべて保存する
		salesProductHistoryService.saveSalesProductHistoryList(salesProductHistoryEntity);

		return salesProductHistoryEntity;
	}

	/**
	 * 商品テーブルの商品を売れた分だけ減算処理していく
	 * @param salesHistoryEntity
	 */
	private void productStockSubtraction(Cart cart) {

		//カートから売れた商品を取得
		Map<String, CartItem> soldItems = new HashMap<>();
		soldItems = cart.getCartItems();

		//商品テーブルの商品ごとに減算処理
		for (CartItem soldItem : soldItems.values()) {

			//商品IDを元にDBの商品を取得
			TrProductEntity productEntity = productSelectService.getItemInfo(soldItem.getId());

			//商品在庫数を変更してDBに反映させる
			productEntity.setProductStock(productEntity.getProductStock() - soldItem.getQuantity());
			productDeleteAndUpdateService.saveAndFlush(productEntity);
		}
	}

	@GetMapping("/charge")
	public String showChargePage() {
		return "charge";
	}

	/**
	 * StripeでChargeの処理をする
	 *
	 * @param cart
	 * @param checkout
	 * @param chargeRequest
	 * @param charge
	 * @param salesHistoryEntity
	 * @param mav
	 * @return
	 * @throws StripeException
	 */
	@PostMapping("/charge")
	public ModelAndView postChargePage(
			@SessionAttribute("cart") Cart cart,
			@SessionAttribute("checkout") Checkout checkout,
			ChargeRequest chargeRequest,
			Charge charge,
			TrSalesHistoryEntity salesHistoryEntity,
			ModelAndView mav) throws StripeException {

		//決済ステータス『決済待ち』の販売履歴を１件作成
		salesHistoryEntity = createSalesHistory(cart, chargeRequest, checkout);

		//販売商品履歴Listの作成
		List<TrSalesProductHistoryEntity> salesProductHistoryEntity = new ArrayList<>();
		salesProductHistoryEntity = createSalesProductHistory(salesHistoryEntity, cart);

		try {

			//決済処理の準備
			chargeRequest.setDescription("販売ID：" + salesHistoryEntity.getSalesHistoryId());
			chargeRequest.setCurrency(Currency.JPY);

			//決済処理try
			charge = paymentsService.charge(checkout, chargeRequest);

			//販売履歴の作成及び商品テーブルのデータ更新
			if ("succeeded".equals(charge.getStatus())) {

				//商品テーブル減算処理
				productStockSubtraction(cart);

				//ChargeHistoryテーブルに決済情報を保存
				TrChargeHistoryEntity chargeHistoryEntity = new TrChargeHistoryEntity(
						salesHistoryEntity.getSalesHistoryId(), charge, chargeRequest);
				chargeHistorySarvice.save(chargeHistoryEntity);

				//決済ステータス『決済完了』にUPDATE
				salesHistoryEntity.setSettlementFlag("決済完了");
				salesHistoryEntity.setSettlementDate(salesHistoryEntity.getSalesDate());
				salesHistoryEntity.setSettlementUser(charge.getCalculatedStatementDescriptor()); //String "stripe" が入っている

				//決済ステータス
				mav.addObject("chargeId", charge.getId());
				mav.addObject("balance__transaction", charge.getBalanceTransaction());

				//決済成功フラグ
				mav.addObject("charge_success", true);

			} else {

				//決済失敗フラグ
				mav.addObject("charge_failed", true);
			}

		} catch (CardException e) {

			//全ての販売商品履歴の配送ステータスを『キャンセル』に変更してDBに保存
			salesProductHistoryEntity = changeProductHistoryStatus.changeSippingStatus("キャンセル", salesProductHistoryEntity);
			salesProductHistoryService.saveAndFlusheSalesProductHistoryList(salesProductHistoryEntity);

			//販売履歴の決済ステータス『決済拒否』にUPDATE
			salesHistoryEntity.setSettlementFlag("決済拒否");
			salesHistoryEntity.setTransactionCancellationDate(salesHistoryEntity.getSalesDate());
			salesHistoryEntity.setTransactionCancellationUser("Stripe.CardException");

			//決済失敗フラグ
			mav.addObject("charge_failed", true);

			//エラーメッセージ
			mav.addObject("error", e.getMessage());

		} finally {

			//販売履歴テーブルのUPDATE文の実行
			salesHistoryService.saveAndFlushSalesHistory(salesHistoryEntity);

			// カートの中身を初期化
			cart = new Cart();
			session.setAttribute("cart", cart);

			//viewファイル名をセット
			mav.setViewName("charge");
		}

		return mav;
	}

	/**
	 * Stripeエラーがthrowされた場合の処理
	 * 商品購入失敗の結果とエラーメッセージをVIEWに表示させる
	 */
	@ExceptionHandler(StripeException.class)
	public ModelAndView handleError(ModelAndView mav, StripeException e) {

		//viewファイル名をセット
		mav.setViewName("charge");

		//エラーメッセージ
		mav.addObject("error", e.getMessage());

		//決済失敗フラグ
		mav.addObject("charge_failed", true);

		return mav;
	}
}
