package net.sales_history;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.stripe.exception.StripeException;
import com.stripe.model.Refund;

import net.charge.StripeService;

@Controller
public class SalesHistoryAdminController {

	@Autowired
	TrSalesHistoryService salesHistoryService;

	@Autowired
	TrSalesProductHistoryService salesProductHistoryService;

	@Autowired
	private StripeService stripeService;

	//Viewに渡すObjectの命名
	private final String SALES_HISTORY = "trSalesHistoryEntity";

	/**
	 * 販売履歴一覧ページを表示するメソッド
	 */
	@RequestMapping("/admin/history")
	public ModelAndView showSalesHistoryPage(ModelAndView mav) {

		//販売履歴をすべて取得
		final List<TrSalesHistoryEntity> salesHistoryList = salesHistoryService.findAll();
		mav.addObject(SALES_HISTORY, salesHistoryList);

		//Viewファイル名セット
		mav.setViewName("sales-history");

		return mav;
	}

	/**
	 * 販売履歴を検索ワードごとに表示するためのメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/admin/history/search")
	public ModelAndView sendItemsByKeyword(
			@RequestParam String keyword,
			ModelAndView mav) {

		//検索ワードを元に販売履歴を取得
		final List<TrSalesHistoryEntity> salesHistoryList = salesHistoryService.findByKeyword(keyword);
		mav.addObject(SALES_HISTORY, salesHistoryList);

		//Viewファイル名セット
		mav.setViewName("sales-history");

		return mav;
	}

	/**
	 * 販売履歴を期間ごとに表示するためのメソッドe
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/admin/history/search/date")
	public ModelAndView sendItemsByDate(
			@RequestParam String start,
			@RequestParam String end,
			Timestamp startDate,
			Timestamp endDate,
			ModelAndView mav) {

		try { //検索開始日チェックとTimestamp型への変換
			startDate = new Timestamp(
					new SimpleDateFormat("yyyy-MM-dd" + "HH:mm:ss.SSS").parse(start + "00:00:00.000").getTime());
		} catch (ParseException e) {
			startDate = null;
		}

		try { //検索終了日のチェックとTimestamp型への変換
			endDate = new Timestamp(
					new SimpleDateFormat("yyyy-MM-dd" + "HH:mm:ss.SSS").parse(end + "23:59:59.999").getTime());
		} catch (ParseException e) {
			endDate = null;
		}

		//期間検索するメソッドを実行してDBから該当のデータを取得してスコープに保存
		final List<TrSalesHistoryEntity> salesHistoryList = salesHistoryService.findByDates(startDate, endDate);
		mav.addObject(SALES_HISTORY, salesHistoryList);

		//Viewファイル名セット
		mav.setViewName("sales-history");

		return mav;
	}

	/**
	 * 販売履歴詳細ページを表示するメソッド
	 */
	@RequestMapping("/admin/history/{id}")
	public ModelAndView showSalesHistoryDetailsPage(
			@PathVariable long id,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		TrSalesHistoryEntity salesHistoryEntity = salesHistoryService.getOne(id);
		mav.addObject(SALES_HISTORY, salesHistoryEntity);

		//Viewファイル名セット
		mav.setViewName("sales-product-history");

		return mav;
	}

	/**
	 * 決済ステータスを『決済完了』に変更するか確認するページを表示するメソッド
	 */
	@RequestMapping("/admin/history/{id}/settlement")
	public ModelAndView showSettlementPage(
			@PathVariable long id,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		TrSalesHistoryEntity salesHistoryEntity = salesHistoryService.getOne(id);

		//決済フラグを決済完了に変更する準備
		if ("未決済".equals(salesHistoryEntity.getSettlementFlag())
				&& salesHistoryEntity.getTransactionCancellationDate() == null
				&& salesHistoryEntity.getTransactionCancellationUser() == null) {

			//ENTITYをVIEWに渡す
			mav.addObject(SALES_HISTORY, salesHistoryEntity);

			//表示Flagを決済完了に変更準備成功に設定
			mav.addObject("settlement", true);

		}

		//Viewファイル名セット
		mav.setViewName("change-payment-status");

		return mav;
	}

	/**
	 * 決済ステータスを『決済完了』に変更した結果を表示するメソッド
	 */
	@RequestMapping("/admin/history/{id}/settlement/result")
	public ModelAndView showSettlementResultPage(
			@PathVariable long id,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		TrSalesHistoryEntity salesHistoryEntity = salesHistoryService.getOne(id);

		//決済フラグを決済完了に変更
		if ("未決済".equals(salesHistoryEntity.getSettlementFlag())
				&& salesHistoryEntity.getTransactionCancellationDate() == null
				&& salesHistoryEntity.getTransactionCancellationUser() == null) {

			//ENTITYの決済フラグを変更
			salesHistoryEntity.setSettlementFlag("決済完了");

			//ログイン中のユーザー名を取得してSettlementUserに設定
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			final String loginUserName = auth.getName();
			salesHistoryEntity.setSettlementUser(loginUserName);

			//現在時刻を取得してSettlementDateに設定
			final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			salesHistoryEntity.setSettlementDate(timestamp);

			//DBにUPDATEクエリ
			salesHistoryService.saveAndFlushSalesHistory(salesHistoryEntity);

			//表示Flagをキャンセル成功に設定
			mav.addObject("Result", "取引の決済ステータスを『決済完了』に変更を完了しました");

		} else {

			//表示Flagをキャンセル失敗に設定
			mav.addObject("Result", "取引の決済ステータス変更を失敗しました");
		}

		//Viewファイル名セット
		mav.setViewName("admin-result");

		return mav;
	}

	/**
	 * 決済ステータスを『キャンセル』に変更するか確認するページを表示するメソッド
	 */
	@RequestMapping("/admin/history/{id}/cancel")
	public ModelAndView showCancelPage(
			@PathVariable long id,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		TrSalesHistoryEntity salesHistoryEntity = salesHistoryService.getOne(id);

		//決済フラグをキャンセルに変更する準備
		if ("未決済".equals(salesHistoryEntity.getSettlementFlag())
				&& salesHistoryEntity.getSettlementDate() == null
				&& salesHistoryEntity.getSettlementUser() == null) {

			//ENTITYをVIEWに渡す
			mav.addObject(SALES_HISTORY, salesHistoryEntity);

			//表示Flagをキャンセル準備成功に設定
			mav.addObject("cancel", true);

		}

		//Viewファイル名セット
		mav.setViewName("change-payment-status");

		return mav;
	}

	/**
	 * 決済ステータスを『キャンセル』に変更した結果を表示するメソッド
	 */
	@RequestMapping("/admin/history/{id}/cancel/result")
	public ModelAndView showCancelResultPage(
			@PathVariable long id,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		TrSalesHistoryEntity salesHistoryEntity = salesHistoryService.getOne(id);

		//決済フラグをキャンセルに変更
		if ("未決済".equals(salesHistoryEntity.getSettlementFlag())
				&& salesHistoryEntity.getSettlementDate() == null
				&& salesHistoryEntity.getSettlementUser() == null) {

			//Viewファイル名セット
			mav.setViewName("admin-result");

			try {

				//Stripeの金額を更新
				Refund refund = stripeService.refund(salesHistoryEntity.getStripeChargeId(),
						salesHistoryEntity.getTotalSalesAmount());
				System.out.println("払い戻し金額：" + refund.getAmount());

			} catch (StripeException e) {

				//表示Flagをキャンセル失敗に設定
				mav.addObject("Result", "取引の決済ステータス変更を失敗しました");

				return mav;
			}

			//ENTITYの決済フラグを変更
			salesHistoryEntity.setSettlementFlag("キャンセル");

			//販売合計額を0円に設定
			salesHistoryEntity.setTotalSalesAmount(0);

			//ログイン中のユーザー名を取得してTransactionCancellationUserに設定
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			final String loginUserName = auth.getName();
			salesHistoryEntity.setTransactionCancellationUser(loginUserName);

			//現在時刻を取得してTransactionCancellationに設定
			final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			salesHistoryEntity.setTransactionCancellationDate(timestamp);

			//DBにUPDATEクエリ
			salesHistoryService.saveAndFlushSalesHistory(salesHistoryEntity);

			//表示Flagをキャンセル成功に設定
			mav.addObject("Result", "取引の決済ステータスを『キャンセル』に変更を完了しました");

		} else {

			//表示Flagをキャンセル失敗に設定
			mav.addObject("Result", "取引の決済ステータス変更を失敗しました");
		}

		return mav;
	}

	/**
	 * 指定した商品の決済ステータスを『キャンセル』に変更するか確認するページを表示するメソッド
	 */
	@RequestMapping("/admin/history/{salesHistoryId}/cancel/{salesProductId}")
	public ModelAndView showSalesProductCancelPage(
			@PathVariable long salesHistoryId,
			@PathVariable String salesProductId,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		TrSalesHistoryEntity salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);

		//URLの販売履歴ID＋販売商品IDを元に１件の販売商品履歴を取得
		TrSalesProductHistoryEntity cancelProduct = salesProductHistoryService
				.getOne(salesHistoryId + "(" + salesProductId + ")");

		System.out.println(cancelProduct.getSalesProductName());

		//ENTITYをVIEWに渡す
		mav.addObject(SALES_HISTORY, salesHistoryEntity);
		mav.addObject("cancelProduct", cancelProduct);

		//表示Flagを設定
		mav.addObject("productCancel", true);

		//Viewファイル名セット
		mav.setViewName("change-payment-status");

		return mav;
	}

	/**
	 * 指定した商品の決済ステータスを『キャンセル』に変更した結果を表示するメソッド
	 */
	@RequestMapping("/admin/history/{salesHistoryId}/cancel/{salesProductId}/result")
	public ModelAndView showSalesProductCancelResultPage(
			@PathVariable long salesHistoryId,
			@PathVariable String salesProductId,
			ModelAndView mav) {

		//Viewファイル名セット
		mav.setViewName("admin-result");

		//URLの販売履歴IDを元に１件の販売履歴を取得
		TrSalesHistoryEntity salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);

		//URLの販売履歴ID＋販売商品IDを元に１件の販売商品履歴を取得
		TrSalesProductHistoryEntity cancelProduct = salesProductHistoryService
				.getOne(salesHistoryId + "(" + salesProductId + ")");

		//決済フラグをキャンセルに変更
		if ("未決済".equals(salesHistoryEntity.getSettlementFlag())
				&& salesHistoryEntity.getSettlementDate() == null
				&& salesHistoryEntity.getSettlementUser() == null
				&& salesHistoryEntity.getTransactionCancellationDate() == null
				&& salesHistoryEntity.getTransactionCancellationUser() == null
				&& cancelProduct.getProductCancellationDate() == null
				&& cancelProduct.getProductCancellationUser() == null) {

			try {

				//Stripeの金額を更新
				Refund refund = stripeService.refund(salesHistoryEntity.getStripeChargeId(),
						cancelProduct.getSalesProductPrice());
				System.out.println("払い戻し金額：" + refund.getAmount());

			} catch (StripeException e) {

				//表示Flagをキャンセル失敗に設定
				mav.addObject("Result", "販売商品の決済ステータス変更を失敗しました");
				return mav;
			}

			//販売履歴の販売合計額を更新
			salesHistoryEntity.setTotalSalesAmount(
					salesHistoryEntity.getTotalSalesAmount() - cancelProduct.getSalesProductPrice());

			//ログイン中のユーザー名を取得してTransactionCancellationUserに設定
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			final String loginUserName = auth.getName();
			cancelProduct.setProductCancellationUser(loginUserName);

			//現在時刻を取得してTransactionCancellationに設定
			final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			cancelProduct.setProductCancellationDate(timestamp);

			//DBにUPDATEクエリ
			salesHistoryService.saveAndFlushSalesHistory(salesHistoryEntity);
			salesProductHistoryService.saveAndFlush(cancelProduct);

			//販売履歴の販売商品履歴がすべてキャンセル済みかチェック
			int notCanceledProduct = 0;

			for (int i = salesHistoryEntity.getSalesProductHistoryEntity().size() - 1; 0 <= i; i--) {

				if (salesHistoryEntity.getSalesProductHistoryEntity().get(i).getProductCancellationDate() == null
						&& salesHistoryEntity.getSalesProductHistoryEntity().get(i)
								.getProductCancellationDate() == null) {
					notCanceledProduct++;
				}
			}

			//販売履歴の全ての商品がキャンセル済みなら、取引ごとキャンセル
			if (notCanceledProduct == 0
					&& "未決済".equals(salesHistoryEntity.getSettlementFlag())
					&& salesHistoryEntity.getSettlementDate() == null
					&& salesHistoryEntity.getSettlementUser() == null) {

				//ENTITYの決済フラグを変更
				salesHistoryEntity.setSettlementFlag("キャンセル");

				//ログイン中のユーザー名を取得してTransactionCancellationUserに設定
				salesHistoryEntity.setTransactionCancellationUser(loginUserName);

				//現在時刻を取得してTransactionCancellationに設定
				salesHistoryEntity.setTransactionCancellationDate(timestamp);

				//DBにUPDATEクエリ
				salesHistoryService.saveAndFlushSalesHistory(salesHistoryEntity);
			}

			//表示Flagをキャンセル成功に設定
			mav.addObject("Result", "販売商品の決済ステータスを『キャンセル』に変更を完了しました");

		} else {

			//表示Flagをキャンセル失敗に設定
			mav.addObject("Result", "販売商品の決済ステータス変更を失敗しました");
		}

		return mav;
	}
}