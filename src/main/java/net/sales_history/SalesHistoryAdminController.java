package net.sales_history;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SalesHistoryAdminController {

	@Autowired
	TrSalesHistoryService salesHistoryService;

	//Viewに渡すObjectの命名
	private final String SALES_HISTORY_LIST = "trSalesHistoryEntity";

	@RequestMapping("/admin/history")
	public ModelAndView showSalesHistoryPage(ModelAndView mav) {

		final List<TrSalesHistoryEntity> salesHistoryList = salesHistoryService.findAll();

		mav.addObject(SALES_HISTORY_LIST, salesHistoryList);

		//Viewファイル名セット
		mav.setViewName("sales-history");

		System.out.println(salesHistoryList.get(0).getSalesHistoryId());

		return mav;
	}
}