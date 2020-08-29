package net.sales_history;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

public class ShippingAddressChangeController {

	@Autowired
	TrSalesHistoryService salesHistoryService;

	@Autowired
	HttpSession session;

	private final String SALES_HISTORY_ENTITY = "trSalesHistory";

	@GetMapping("/admin/history/{id}/change")
	public String showShippingChangePage(
			TrSalesHistoryEntity salesHistoryEntity,
			@PathVariable long id) {

		salesHistoryEntity = salesHistoryService.getOne(id);
		session.setAttribute(SALES_HISTORY_ENTITY , salesHistoryEntity);

		return "ViewName";
	}

	@PostMapping("/admin/history/{id}/change")
	public ModelAndView postShippingChangePage(
			@PathVariable long id,
			@SessionAttribute(SALES_HISTORY_ENTITY) TrSalesProductHistoryEntity salesHistoryEntity,
			BindingResult result,
			ModelAndView mav) {

		if (result != null) {
			mav.setViewName("viewName");
			return mav;
		}

		session.setAttribute(SALES_HISTORY_ENTITY, salesHistoryEntity);

		mav.setViewName("ViewName2");
		return mav;
	}


	@RequestMapping("/admin/history/{id}/change/confirmation")
	public ModelAndView showShippingConfirmationPage(
			ModelAndView mav) {

		mav.addObject("Result", "hogehoge");
		mav.setViewName("result");
		return mav;
	}

}
