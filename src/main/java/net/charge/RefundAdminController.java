package net.charge;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.stripe.exception.StripeException;
import com.stripe.model.Refund;

import net.sales_history.ChangeProductHistoryStatus;
import net.sales_history.TrSalesHistoryEntity;
import net.sales_history.TrSalesHistoryService;
import net.sales_history.TrSalesProductHistoryEntity;
import net.sales_history.TrSalesProductHistoryService;

@Controller
public class RefundAdminController {

	@Autowired
	private TrSalesHistoryService salesHistoryService;

	@Autowired
	private TrSalesProductHistoryService salesProductHistoryService;

	@Autowired
	TrChargeHistoryService chargeHistoryService;

	@Autowired
	private StripeService stripeService;

	@Autowired
	private ChangeProductHistoryStatus changeProductHistoryStatus;

//	@Autowired
//	public void setChangeProductHistoryStatus(ChangeProductHistoryStatus changeProductHistoryStatus) {
//		this.changeProductHistoryStatus = changeProductHistoryStatus;
//	}

	//Viewに渡すObjectの命名
	private final String SALES_HISTORY = "trSalesHistoryEntity";

	/**
	 * １件の販売履歴の決済ステータスを『返金済』に変更し
	 * 返金処理を実行するかどうか確認するページを表示するメソッド
	 */
//	@RequestMapping("/admin/history/{salesHistoryId}/cancel")
//	public ModelAndView showRefundPage(
//			@PathVariable long salesHistoryId,
//			ModelAndView mav) {
//
//		//URLの販売履歴IDを元に１件の販売履歴を取得
//		TrSalesHistoryEntity salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);
//
//		//決済フラグをキャンセルに変更する準備
//		if ("決済完了".equals(salesHistoryEntity.getSettlementFlag())) {
//
//			//ENTITYをVIEWに渡す
//			mav.addObject(SALES_HISTORY, salesHistoryEntity);
//
//			//表示Flagをキャンセル準備成功に設定
//			mav.addObject("refund_preparation", true);
//
//		} else {
//
//			//表示Flagをキャンセル準備成功に設定
//			mav.addObject("refund_preparation", false);
//		}
//
//		//Viewファイル名セット
//		mav.setViewName("refund");
//
//		return mav;
//	}
//
//	/**
//	 * １件の販売履歴の決済ステータスを『返金済』に変更し
//	 * 返金処理を実行した結果を表示するページを遷移するメソッド
//	 */
//	@RequestMapping("/admin/history/{salesHistoryId}/cancel/result")
//	public ModelAndView showRefundResultPage(
//			@PathVariable long salesHistoryId,
//			TrSalesHistoryEntity salesHistoryEntity,
//			TrChargeHistoryEntity chargeHistoryEntity,
//			Refund refund,
//			ModelAndView mav) {
//
//		//Viewファイル名セット
//		mav.setViewName("admin-result");
//
//		try {
//
//			//URLの販売履歴IDを元に１件の販売履歴を取得
//			salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);
//
//			//販売履歴IDを元にCharge履歴を取得
//			chargeHistoryEntity = salesHistoryEntity.getChargeHistoryEntity();
//
//		} catch (NullPointerException e) {
//
//			//表示Flagをキャンセル失敗に設定
//			mav.addObject("result_message", "取引の決済ステータス変更を失敗しました");
//
//			return mav;
//		}
//
//		if ("決済完了".equals(salesHistoryEntity.getSettlementFlag())) {
//
//			try {
//
//				//(合計購入金額 - 返金済み金額)を算出してStripeの返金処理の実行
//				refund = stripeService.refund(chargeHistoryEntity.getStripeChargeId(),
//						salesHistoryEntity.getSalesAmount() - salesHistoryEntity.getRefundAmount());
//
//				//販売履歴の最新のrefundAmountをセット
//				salesHistoryEntity.setRefundAmount(salesHistoryEntity.getRefundAmount() + refund.getAmount());
//
//				//ログイン中のユーザー名を取得してrefundUserに設定
//				final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//				final String loginUserName = auth.getName();
//				salesHistoryEntity.setRefundUser(loginUserName);
//
//				//現在時刻を取得してrefundCancellationに設定
//				final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//				salesHistoryEntity.setRefundDate(timestamp);
//
//				//決済ステータスを『返金済み』に変更
//				salesHistoryEntity.setSettlementFlag("返金済み");
//
//				//販売商品履歴の配送ステータスを全て『キャンセル』に変更
//				List<TrSalesProductHistoryEntity> salesProductHistoryEntity = salesHistoryEntity
//						.getSalesProductHistoryEntity();
//				salesProductHistoryEntity = changeProductHistoryStatus.changeSippingStatus("キャンセル",
//						salesProductHistoryEntity);
//
//				//UPDATE分の実行
//				salesHistoryService.saveAndFlushSalesHistory(salesHistoryEntity);
//				salesProductHistoryService.saveAndFlusheSalesProductHistoryList(salesProductHistoryEntity);
//
//				//表示Flagをキャンセル成功に設定
//				mav.addObject("result_message", "取引の返金処理を完了しました");
//
//			} catch (StripeException e) {
//
//				//表示Flagをキャンセル失敗に設定
//				mav.addObject("result_message", "取引の返金処理を失敗しました");
//			}
//
//		} else {
//
//			//表示Flagをキャンセル失敗に設定
//			mav.addObject("result_message", "取引の返金処理を失敗しました");
//		}
//
//		return mav;
//	}

	/**
	 * 指定した商品の配送ステータスを『キャンセル』に変更し
	 * 返金処理を実行するかどうか確認するページを表示するメソッド
	 */
	@RequestMapping("/admin/history/{salesHistoryId}/cancel/{salesProductId}")
	public ModelAndView showProductRefundPage(
			@PathVariable long salesHistoryId,
			@PathVariable String salesProductId,
			TrSalesHistoryEntity salesHistoryEntity,
			TrSalesProductHistoryEntity cancelProduct,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);

		//URLの販売履歴ID＋販売商品IDを元に１件の販売商品履歴を取得
		cancelProduct = salesProductHistoryService
				.getOne(salesHistoryId + "(" + salesProductId + ")");

		if ("決済完了".equals(salesHistoryEntity.getSettlementFlag())
				&& !"発送済み".equals(cancelProduct.getShippingStatus())) {

			//ENTITYをVIEWに渡す
			mav.addObject(SALES_HISTORY, salesHistoryEntity);
			mav.addObject("refundProduct", cancelProduct);

			mav.addObject("productRefund_preparation", true);
		}

		//表示Flagをキャンセル準備成功に設定
		mav.addObject("refund_preparation", false);

		//Viewファイル名セット
		mav.setViewName("refund");

		return mav;
	}

	/**
	 * 指定した商品の配送ステータスを『キャンセル』に変更し
	 * 返金処理を実行した結果を表示するページを遷移するメソッド
	 */
	@RequestMapping("/admin/history/{salesHistoryId}/cancel/{salesProductId}/result")
	public ModelAndView showProductRefundResultPage(
			@PathVariable long salesHistoryId,
			@PathVariable String salesProductId,
			TrSalesHistoryEntity salesHistoryEntity,
			TrSalesProductHistoryEntity cancelProduct,
			TrChargeHistoryEntity chargeHistoryEntity,
			Refund refund,
			ModelAndView mav) {

		//Viewファイル名セット
		mav.setViewName("admin-result");

		try {

			//URLの販売履歴IDを元に１件の販売履歴を取得
			salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);

			//URLの販売履歴ID＋販売商品IDを元に１件の販売商品履歴を取得
			cancelProduct = salesProductHistoryService
					.getOne(salesHistoryId + "(" + salesProductId + ")");

			//販売履歴IDを元にCharge履歴を取得
			chargeHistoryEntity = salesHistoryEntity.getChargeHistoryEntity();

		} catch (NullPointerException e) {

			//表示Flagをキャンセル失敗に設定
			mav.addObject("result_message", "商品の返金処理を失敗しました");

			return mav;
		}

		//決済フラグをキャンセルに変更
		if ("決済完了".equals(salesHistoryEntity.getSettlementFlag())
				&& "発送待ち".equals(cancelProduct.getShippingStatus())) {

			try {

				//Stripeの返金処理を実行
				refund = stripeService.refund(chargeHistoryEntity.getStripeChargeId(),
						cancelProduct.getSalesProductPrice());

				System.out.println("払い戻し金額：" + refund.getAmount());

				//販売履歴の返金額を更新
				salesHistoryEntity.setRefundAmount(salesHistoryEntity.getRefundAmount() + refund.getAmount());

				//販売商品履歴の配送ステータスを『キャンセル』に変更
				cancelProduct.setShippingStatus("キャンセル");

				//現在時刻を取得してTransactionCancellationに設定
				final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				salesHistoryEntity.setRefundDate(timestamp);
				cancelProduct.setProductCancellationDate(timestamp);

				//ログイン中のユーザー名を取得してrefundDateとTransactionCancellationUserに設定
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				final String loginUserName = auth.getName();
				salesHistoryEntity.setRefundUser(loginUserName);
				cancelProduct.setProductCancellationUser(loginUserName);

				//DBにUPDATEクエリ
				salesProductHistoryService.saveAndFlush(cancelProduct);

				//販売履歴の全ての商品がキャンセル済みなら取引自体の決済ステータスを『返金済み』に変更
				if (notCanceledProductCheck(salesHistoryEntity) == 0) {

					//ENTITYの決済フラグを変更
					salesHistoryEntity.setSettlementFlag("返金済み");
				}

				//DBにUPDATEクエリ
				salesHistoryService.saveAndFlushSalesHistory(salesHistoryEntity);

				//表示Flagをキャンセル成功に設定
				mav.addObject("result_message", "商品の返金処理を完了しました");

			} catch (StripeException e) {

				//表示Flagをキャンセル失敗に設定
				mav.addObject("result_message", "商品の返金処理を失敗しました");
			}

		} else {

			//表示Flagをキャンセル失敗に設定
			mav.addObject("result_message", "商品の返金処理を失敗しました");
		}

		return mav;
	}

	/**
	 * 販売履歴の『キャンセル済みではない商品数』を数えるメソッド
	 *
	 * @param salesHistoryEntity
	 * @return
	 */
	private int notCanceledProductCheck(TrSalesHistoryEntity salesHistoryEntity) {

		//販売履歴の販売商品履歴がすべてキャンセル済みかチェック
		int notCanceledProduct = 0;

		for (int i = salesHistoryEntity.getSalesProductHistoryEntity().size() - 1; 0 <= i; i--) {

			if (!"キャンセル".equals(salesHistoryEntity.getSalesProductHistoryEntity().get(i).getShippingStatus())) {
				notCanceledProduct++;
			}
		}

		return notCanceledProduct;
	}
}