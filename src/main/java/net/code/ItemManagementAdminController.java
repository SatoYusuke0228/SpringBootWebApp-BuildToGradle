package net.code;


import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ItemManagementAdminController {

	@Autowired
	private TrProductSelectService productSelectService;

	@Autowired
	private TrProductDeleteAndUpdateService productDeleteAndUpdateService;

	@Autowired
	private HttpSession session;

	//viewで扱う商品のオブジェクト名を統一
	final private String OBJECT_NAME = "trProductEntity";

	/**
	 * 管理者用の商品管理画面を表示するメソッド
	 * DBから取得した商品一覧情報をViewに渡して遷移する。
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/admin/management")
	public ModelAndView showItemManagementPage(ModelAndView mav) {

		//DB内の商品を全て取得してViewに渡す
		List<TrProductEntity> productEntity = productSelectService.findAll();
		mav.addObject(OBJECT_NAME, productEntity);

		//Viewファイル名セット
		mav.setViewName("management");

		return mav;
	}

	/**
	 * 管理者用の商品管理画面をから
	 * 「詳細」ボタンが押下されたときに
	 * 商品詳細ページを表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/admin/management/details/{id}")
	public ModelAndView showItemDetailsManagementPage(
			@PathVariable String id,
			ModelAndView mav) {

		//選択された商品のIDを元にDBから商品を取得
		TrProductEntity productEntity = productSelectService.getItemInfo(id);
		mav.addObject(OBJECT_NAME, productEntity);

		mav.setViewName("details");
		return mav;
	}

	/**
	 * 管理者用の商品管理画面をから
	 * 「変更」ボタンが押下されたときに
	 * 商品情報変更ページを表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/admin/management/update/{id}")
	public ModelAndView showItemUpdateManagementPage(
			@PathVariable String id,
			ModelAndView mav) {

		//選択された商品のIDを元にDBから商品を取得
		TrProductEntity productEntity = productSelectService.getItemInfo(id);
		mav.addObject(OBJECT_NAME, productEntity);

		//Viewファイル名セット
		mav.setViewName("admin");

		return mav;
	}

	/**
	 * 管理者用の商品管理画面をから
	 * 「削除」ボタンが押下されたときに
	 * 商品削除確認ページを表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/admin/management/delete/{id}")
	public ModelAndView showItemDeleteManagementPage(
			@PathVariable String id,
			Model model,
			ModelAndView mav) {

		//選択された商品のIDを元にDBから商品を取得
		TrProductEntity productEntity = productSelectService.getItemInfo(id);

		//取得した商品をModelAndViewとSessionに渡す
		session.setAttribute(OBJECT_NAME, productEntity);

		//model.addAttribute(OBJECT_NAME, productEntity);
		mav.addObject(OBJECT_NAME, productEntity);

		//Viewファイル名セット
		mav.setViewName("delete_confirmation");

		return mav;
	}

	/**
	 * 管理者用の商品管理画面をから
	 * 「削除」ボタンが押下されたときに
	 * 商品削除確認ページを表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/admin/management/delete/result")
	public ModelAndView deleteQueryExecution(
//			@ModelAttribute(OBJECT_NAME) TrProductEntity productEntity,
			@SessionAttribute(OBJECT_NAME) TrProductEntity productEntity,
			ModelAndView mav) {

		//現在時刻を取得してUpdateDateとDeleteDateカラムに設定
		final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		productEntity.setUpdateDate(timestamp);
		productEntity.setDeleteDate(timestamp);

		//ログイン中の管理者の名前を取得してUpdateUserとDeleteUserカラムに設定
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final String loginUserName = auth.getName();
		productEntity.setUpdateUser(loginUserName);
		productEntity.setDeleteUser(loginUserName);

		//DeleteDateとDeleteUserを更新
		productDeleteAndUpdateService.saveAndFlush(productEntity);

		//もし商品の削除日が入力されている場合、DBの商品を削除する
		if (productEntity.getProductId() != null
				&& productEntity.getDeleteDate() != null
//				productEntity.getDeleteDate().equals(productEntity.getUpdateDate())
				){

			//DBに対して商品削除を実行
			productDeleteAndUpdateService.delete(productEntity);

			//View側で使用する商品削除クエリ実行のflag
			mav.addObject("Result", "商品削除成功");

		} else {

			//View側で使用する商品削除クエリ実行のflag
			mav.addObject("Result", "商品削除失敗");
		}

		//Viewファイル名セット
		mav.setViewName("admin_result");

		return mav;
	}

	@RequestMapping("/admin/history")
	public ModelAndView SalesHistory(ModelAndView mav) {

		//Viewファイル名セット
		mav.setViewName("history");

		return mav;
	}
}