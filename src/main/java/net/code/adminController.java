package net.code;

import java.sql.Timestamp;

import javax.servlet.http.HttpSession;

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

	@Autowired
	private HttpSession session;

	@GetMapping("/admin/addition")
	public ModelAndView getItemAdditionPage(
			TrProductEntity productEntity,
			ModelAndView mav) {

		mav.addObject("productEntity", new TrProductEntity());
		mav.setViewName("addition");

		return mav;
	}

	@PostMapping("/admin/add_confirmation")
	public ModelAndView postItemAdditionPage(
			@Validated TrProductEntity productEntity,
			BindingResult result,
			ModelAndView mav) {

//		if (result.hasErrors()) { //入力FORMに不備があれば
//
//			//元のページに戻る
//			mav.setViewName("addition");
//
//		} else { //入力FORMに不備がなければ

			//ログイン中のユーザー名を取得してInsertUserとUpdateUserに設定
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			final String loginUserName = auth.getName();
			//System.out.println(loginUserName);
			productEntity.setInsertUser(loginUserName);
			productEntity.setUpdateUser(loginUserName);

			//現在時刻を取得してInsertDateとUpdateDateに設定
			final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			productEntity.setInsertDate(timestamp);
			productEntity.setUpdateDate(timestamp);

			//商品登録情報を保存して確認画面に進む
			session.setAttribute("productEntity", productEntity);
			mav.addObject("productEntity", productEntity);
			mav.setViewName("add_confirmation");
//		}
		return mav;
	}

	@RequestMapping("/admin/add/admin_result")
	public ModelAndView showItemAdditionResultPage(
			ModelAndView mav) {

		TrProductEntity productEntity =
				(TrProductEntity)session.getAttribute("productEntity");

		//商品を追加してスコープの中身を初期化
		productInsertService.insert(productEntity);
		session.setAttribute("productEntity", new TrProductEntity());

		mav.setViewName("admin_result");
		return mav;
	}

//
//	@RequestMapping("/admin/management")
//	public ModelAndView showItemManagementPage(ModelAndView mav) {
//
//		mav.setViewName("management");
//		return mav;
//	}
//
//	@RequestMapping("/admin/history")
//	public ModelAndView SalesHistory(ModelAndView mav) {
//
//		mav.setViewName("history");
//		return mav;
//	}
}
