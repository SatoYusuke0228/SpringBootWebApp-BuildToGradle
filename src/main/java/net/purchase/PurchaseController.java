package net.purchase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import net.product.TrProductDeleteAndUpdateService;
import net.product.TrProductEntity;
import net.product.TrProductSelectService;
import net.purchase.ChargeRequest.Currency;
import net.sales_history.TrSalesHistoryEntity;
import net.sales_history.TrSalesHistoryService;
import net.sales_history.TrSalesProductHistoryEntity;
import net.sales_history.TrSalesProductHistoryService;

/**
 * 商品購入手続き関係のコントローラー
 * @author SatoYusuke0228
 */
@Controller
public class PurchaseController {

	@Autowired
	TrProductSelectService productSelectService;

	@Autowired
	TrProductDeleteAndUpdateService productDeleteAndUpdateService;

	@Autowired
	TrSalesHistoryService salesHistoryService;

	@Autowired
	TrSalesProductHistoryService salesProductHistoryService;

	@Autowired
	private StripeService paymentsService;

	//StripeAPIパブリックキー
	@Value("pk_test_51HJ6pwG2TPycVyZrEyKZDWmeOMZsppzPupPZGU2cEYyB5ep1TKjzUTuGJX3w3TmelaDjNe6JWMzQgFXceCE6s6op006Bovigok")
	private String stripePublicKey;

	//セッションスコープのインスタンス
	@Autowired
	private HttpSession session;

	/**
	 * Form入力欄にスペース等が入れられた場合にトリミングするメソッド
	 * @author SatoYusuke0228
	 */
	@InitBinder
	public void InitBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor editor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, editor);
	}

	/**
	 * checkout画面を表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@GetMapping("/showform")
	public ModelAndView showForm(
			@SessionAttribute("cart") Cart cart,
			ModelAndView mav) {

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", 0 < cart.getCartItems().size());

		//購入情報オブジェクトを作成してモデルに登録
		mav.addObject("checkout", new Checkout());
		mav.setViewName("form");

		session.setAttribute("cart", cart);

		return mav;
	}

	/**
	 * 「購入」ボタンがクリックされた時の処理メソッド
	 *
	 * @parm checkout
	 * 引数1
	 * 名前やクレジットカードなど
	 * 入力されたデータを持っているオブジェクト.
	 *
	 * @parm result
	 * 引数2
	 * バリデーションチェックの結果が格納されています。
	 * hasErrors()メソッドの戻り値がtrueの場合は、
	 * バリデーションチェックがエラーということで、
	 * もういちどcheckout画面へ遷移してエラーメッセージを表示。
	 * OKの場合はカートを空にして購入完了画面purchase.htmlへ遷移。
	 *
	 * @author SatoYusuke0228
	 */
	@PostMapping("/checkout")
	public ModelAndView postPurchasePage(
			@SessionAttribute("cart") Cart cart,
			@Validated Checkout checkout,
			BindingResult result,
			ModelAndView mav) {

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", 0 < cart.getCartItems().size());

		if (result.hasErrors()) { //FROMに不備があれば入力画面を再表示する

			// 元の画面に戻りエラーメッセージを表示
			mav.setViewName("form");

		} else { //FORMに不備がなければ販売処理をする

			session.setAttribute("checkout", checkout);
			session.setAttribute("cart", cart);

			//請求金額と通貨単位のセット
			mav.addObject("amount", cart.getGrandTotal()); //in JPY
			mav.addObject("currency", ChargeRequest.Currency.JPY);

			//StripeAPIキー
			mav.addObject("stripePublicKey", stripePublicKey);

			// 購入確認画面を表示
			mav.setViewName("checkout");
		}
		return mav;
	}

	@GetMapping("/checkout")
	public String getCheckoutPage() {
		return "checkout";
	}

	@PostMapping("/charge")
	public ModelAndView showChargePage(
			@SessionAttribute("cart") Cart cart,
			@SessionAttribute("checkout") Checkout checkout,
			ChargeRequest chargeRequest,
			Charge charge,
			ModelAndView mav) throws StripeException {

		//viewファイル名をセット
		mav.setViewName("charge");

		try {

			//決済処理の準備
			chargeRequest.setDescription(cart.getCartItems().size() + "商品の販売");
			chargeRequest.setCurrency(Currency.JPY);

			//決済処理
			charge = paymentsService.charge(chargeRequest);

			//決済ステータス
			mav.addObject("chargeId", charge.getId());
			mav.addObject("balance__transaction", charge.getBalanceTransaction());

		} catch (CardException e) {

			//決済失敗フラグ
			mav.addObject("charge_failed", true);

			//エラーメッセージ
			mav.addObject("error", e.getMessage());

			return mav;
		}

		//販売履歴の作成及び商品テーブルのデータ更新
		if (charge.getId() != null
				&& charge.getBalanceTransaction() != null
				&& "succeeded".equals(charge.getStatus())) {

			//Sessinに保存したカートから売れた商品を取得
			Map<String, CartItem> soldItems = new HashMap<>();
			soldItems = cart.getCartItems();

			//販売履歴オブジェクトを作成してDBに保存する
			TrSalesHistoryEntity salesHistoryEntity = new TrSalesHistoryEntity(
					cart, checkout, chargeRequest, charge,
					new Timestamp(System.currentTimeMillis()));
			salesHistoryService.saveSalesHistory(salesHistoryEntity);

			//販売商品履歴を商品種類ごとに格納するListを作成
			List<TrSalesProductHistoryEntity> salesProductHistoryEntity = new ArrayList<>();

			//売れた商品ごとに商品在庫から商品個数を減算し、販売商品履歴Listに格納していく処理
			for (CartItem soldItem : soldItems.values()) {

				//商品IDを元にDBの商品を取得
				TrProductEntity productEntity = productSelectService.getItemInfo(soldItem.getId());

				//商品在庫数を変更してDBに反映させる
				productEntity.setProductStock(productEntity.getProductStock() - soldItem.getQuantity());
				productDeleteAndUpdateService.saveAndFlush(productEntity);

				//販売商品履歴Listに格納していく
				final TrSalesProductHistoryEntity salesProductHistory = new TrSalesProductHistoryEntity(
						salesHistoryEntity.getSalesHistoryId(), soldItem);
				salesProductHistoryEntity.add(salesProductHistory);
			}

			//販売商品を格納したListをすべて保存する
			salesProductHistoryService.saveSalesProductHistory(salesProductHistoryEntity);

			// カートの中身を初期化
			cart = new Cart();
			session.setAttribute("cart", cart);

			//決済成功フラグ
			mav.addObject("charge_success", true);

		} else {

			//決済失敗フラグ
			mav.addObject("charge_failed", true);
		}

		// カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", 0 < cart.getCartItems().size());

		return mav;
	}

	@ExceptionHandler(StripeException.class)
	public ModelAndView handleError(ModelAndView mav, StripeException e) {

		//viewファイル名をセット
		mav.setViewName("charge");

		//決済失敗フラグ
		mav.addObject("charge_failed", true);

		//エラーメッセージ
		mav.addObject("error", e.getMessage());

		return mav;
	}
}