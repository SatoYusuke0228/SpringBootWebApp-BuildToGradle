package net.sales_history;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import net.charge.TrChargeHistoryService;

@Controller
public class SalesHistoryAdminController {

	@Autowired
	TrSalesHistoryService salesHistoryService;

	@Autowired
	TrSalesProductHistoryService salesProductHistoryService;

	@Autowired
	TrChargeHistoryService chargeHistoryService;

	@Autowired
	HttpSession session;

	//Viewに渡すObjectの命名
	private final String SALES_HISTORY = "trSalesHistoryEntity";
	private final String SALES_PRODUCT_HISTORY = "trSalesProductHistoryEntity";

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
	@GetMapping("/admin/history/{salesHistoryId}")
	public ModelAndView showSalesHistoryDetailsPage(
			@PathVariable long salesHistoryId,
			TrSalesHistoryEntity salesHistoryEntity,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);
		mav.addObject(SALES_HISTORY, salesHistoryEntity);

		//取得した販売履歴を元にCustomerオブジェクトとShippingオブジェクトを作成
		mav.addObject("customer", new Customer(salesHistoryEntity));
		mav.addObject("shipping", new Shipping(salesHistoryEntity));

		//Viewファイル名セット
		mav.setViewName("sales-history-details");

		return mav;
	}

	/**
	 * 販売履歴の購入者情報を変更する確認画面を表示するメソッド。
	 * 販売履歴の取得・表示段階で購入者情報を１度Customerクラスに渡しているため、
	 * そのCustomerクラスのフィールドを変更し、再度salesHistoryEntityにsetするための準備をする。
	 * またvalidationErrorが発生した場合は、元のページに戻りエラーメッセージを表示する。
	 *
	 * @see Customer
	 * @param salesHistoryId
	 * @param customer
	 * @param result
	 * @param salesHistoryEntity
	 * @param mav
	 */
	@PostMapping("/admin/history/{salesHistoryId}/change/customer")
	public ModelAndView postChangeCustamerInfoPage(
			@PathVariable long salesHistoryId,
			@Validated Customer customer,
			BindingResult result,
			TrSalesHistoryEntity salesHistoryEntity,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);
		mav.addObject(SALES_HISTORY, salesHistoryEntity);

		if (result.hasErrors()) { //もし入力情報に不備がある場合

			mav.addObject("customer", customer);
			mav.addObject("shipping", new Shipping(salesHistoryEntity));

			//Viewファイル名セット
			mav.setViewName("sales-history-details");

		} else {

			session.setAttribute("customer", customer);

			//確認ページに進む
			mav.setViewName("sales-history-change");
		}

		return mav;
	}

	/**
	 * 販売履歴の配送先情報を変更する確認画面を表示メソッド。
	 * 販売履歴の取得・表示段階で配送先情報を１度Shippingクラスに渡しているため、
	 * そのShippingクラスのフィールドを変更し、再度salesHistoryEntityにsetための準備をする。
	 * またvalidationErrorが発生した場合は、元のページに戻りエラーメッセージを表示する。
	 *
	 * @see Shipping
	 * @param salesHistoryId
	 * @param customer
	 * @param result
	 * @param salesHistoryEntity
	 * @param mav
	 */
	@PostMapping("/admin/history/{salesHistoryId}/change/shipping")
	public ModelAndView postChangeShippingInfoPage(
			@PathVariable long salesHistoryId,
			@Validated Shipping shipping,
			BindingResult result,
			TrSalesHistoryEntity salesHistoryEntity,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);
		mav.addObject(SALES_HISTORY, salesHistoryEntity);

		if (result.hasErrors()) { //もし入力情報に不備がある場合

			mav.addObject("shipping", shipping);
			mav.addObject("customer", new Customer(salesHistoryEntity));

			//Viewファイル名セット
			mav.setViewName("sales-history-details");

		} else {

			session.setAttribute("shipping", shipping);

			//確認ページに進む
			mav.setViewName("sales-history-change");
		}

		return mav;
	}

	/**
	 * 販売履歴の購入者情報を変更しリザルト画面に遷移するメソッド
	 *
	 * @see Customer
	 * @param salesHistoryId
	 * @param customer
	 * @param salesHistoryEntity
	 * @param resultMessage
	 * @param mav
	 * @return
	 */
	@RequestMapping("/admin/history/{salesHistoryId}/change/customer/result")
	public ModelAndView showChangeCustomerInfoResultPage(
			@PathVariable long salesHistoryId,
			@SessionAttribute(name = "customer") Customer customer,
			TrSalesHistoryEntity salesHistoryEntity,
			String resultMessage,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);
		resultMessage = "購入者情報の変更を失敗しました";

		if (customer != null) {

			salesHistoryEntity.setCustomerName(customer.getName());
			salesHistoryEntity.setCustomerZipcode(customer.getZipcode());
			salesHistoryEntity.setCustomerAddress(customer.getAddress());
			salesHistoryEntity.setCustomerEmail(customer.getEmail());
			salesHistoryEntity.setCustomerTell(customer.getTell());

			//DBの販売履歴テーブルにUPDATEクエリを実行
			salesHistoryService.saveAndFlushSalesHistory(salesHistoryEntity);

			resultMessage = "購入者情報の変更を完了しました";
		}

		mav.addObject("result_message", resultMessage);
		mav.setViewName("admin-result");

		return mav;
	}

	/**
	 * 販売履歴の配送先情報を変更しリザルト画面に遷移するメソッド
	 *
	 * @see Shipping
	 * @param salesHistoryId
	 * @param customer
	 * @param salesHistoryEntity
	 * @param resultMessage
	 * @param mav
	 * @return
	 */
	@RequestMapping("/admin/history/{salesHistoryId}/change/shipping/result")
	public ModelAndView showChangeShippingInfoResultPage(
			@PathVariable long salesHistoryId,
			@SessionAttribute(name = "shipping") Shipping shipping,
			TrSalesHistoryEntity salesHistoryEntity,
			String resultMessage,
			ModelAndView mav) {

		//URLの販売履歴IDを元に１件の販売履歴を取得
		salesHistoryEntity = salesHistoryService.getOne(salesHistoryId);
		resultMessage = "配送先情報の変更を失敗しました";

		if (shipping != null) {

			salesHistoryEntity.setShippingName(shipping.getName());
			salesHistoryEntity.setShippingZipcode(shipping.getZipcode());
			salesHistoryEntity.setShippingAddress(shipping.getAddress());
			salesHistoryEntity.setShippingEmail(shipping.getEmail());
			salesHistoryEntity.setShippingTell(shipping.getTell());

			//DBの販売履歴テーブルにUPDATEクエリを実行
			salesHistoryService.saveAndFlushSalesHistory(salesHistoryEntity);

			resultMessage = "配送先情報の変更を完了しました";
		}

		mav.addObject("result_message", resultMessage);
		mav.setViewName("admin-result");

		return mav;
	}

	@RequestMapping("/admin/history/product/{salesProductHistoryId}/change/shipping")
	public ModelAndView changeSalsProductShippingStatusConfig(
			@PathVariable String salesProductHistoryId,
			TrSalesProductHistoryEntity salesProductHistoryEntity,
			ModelAndView mav) {

		//１件の販売商品履歴を取得
		salesProductHistoryEntity = salesProductHistoryService.getOne(salesProductHistoryId);

		mav.addObject("changeShippingStatus", true);
		mav.addObject(SALES_PRODUCT_HISTORY, salesProductHistoryEntity);
		mav.setViewName("sales-history-change");

		session.setAttribute(SALES_PRODUCT_HISTORY, salesProductHistoryEntity);

		return mav;
	}

	@RequestMapping("/admin/history/product/{salesProductHistoryId}/change/shipping/result")
	public ModelAndView changeSalsProductShippingStatus(
			@PathVariable String salesProductHistoryId,
			TrSalesProductHistoryEntity salesProductHistoryEntity,
			ModelAndView mav) {

		//１件の販売商品履歴を取得
		salesProductHistoryEntity = salesProductHistoryService.getOne(salesProductHistoryId);

		//配送ステータスの変更
		salesProductHistoryEntity.setShippingStatus("発送済み");

		//Systemのログインユーザーを取得して配送ステータス変更処理者にセット
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final String loginUserName = auth.getName();
		salesProductHistoryEntity.setProductShippingUser(loginUserName);

		//配送日の取得を取得してEntityにセット
		final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		salesProductHistoryEntity.setProductShippingDate(timestamp);

		//DBにUPDATEクエリを実行
		salesProductHistoryService.saveAndFlush(salesProductHistoryEntity);

		mav.addObject("result_message", "商品の配送ステータスを「発送済み」に変更しました。");
		mav.setViewName("admin-result");

		return mav;
	}
}