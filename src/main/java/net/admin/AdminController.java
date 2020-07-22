package net.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {

	/**
	 * 管理者トップに画面に戻るメソッド
	 *
	 **/
	@RequestMapping("/admin")
	public String buckAdminTopPage() {
		return"admin";
	}
}
