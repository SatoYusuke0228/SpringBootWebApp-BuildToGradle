package net.code;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class adminController {

	@Autowired
	private TrProductInsertService productInsertService;

	@GetMapping("/admin/addition")
	public ModelAndView showItemAdditionPage(
			TrProductEntity productEntity,
			ModelAndView mav
			) {
		mav.setViewName("addition");
		return mav;
	}

	@PostMapping("/admin/addition/check")
	public ModelAndView itemAdditionPage(
			@Validated TrProductEntity productEntity,
			BindingResult result,
			ModelAndView mav) {

		if (result.hasErrors()) { //もし入力FORMに不備があれば

			//元のページに戻る
			mav.setViewName("addition");

		} else { //もし入力FORMに不備がなければ

			//現在時刻を取得してInsert日時とUpdate日時に設定
			final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		 	productEntity.setInsertDate(timestamp);
		 	productEntity.setUpdateDate(timestamp);

		 	//現在時刻を取得してInsert日時とUpdate日時に設定
		 	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 	final String loginUserName = auth.getName();//get logged in username
		 	productEntity.setInsertUser(loginUserName);
			productEntity.setUpdateUser(loginUserName);

			//商品登録情報を保存
			mav.addObject("productEntity", productEntity);

			//確認画面に進む
			mav.setViewName("additionCheck");
		}
		return mav;
	}

	@RequestMapping("/admin/management")
	public ModelAndView showItemManagementPage(ModelAndView mav) {

		mav.setViewName("management");
		return mav;
	}

	@RequestMapping("/admin/history")
	public ModelAndView SalesHistory(ModelAndView mav) {

		mav.setViewName("history");
		return mav;
	}
}
