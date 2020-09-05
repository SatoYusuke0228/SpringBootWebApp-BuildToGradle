package net.sales_history;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
}