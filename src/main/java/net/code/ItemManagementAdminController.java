package net.code;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ItemManagementAdminController {

	@Autowired
	private TrProductSelectService productSelectService;

	private final String objectName = "productEntity";

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
		mav.addObject(objectName, productEntity);

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
		mav.addObject(objectName, productEntity);

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
		mav.addObject(objectName, productEntity);

		mav.setViewName("admin");
		return mav;
	}

	/**
	 * 管理者用の商品管理画面をから
	 * 「削除」ボタンが押下されたときに
	 * 商品削除ページを表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/admin/management/delete/{id}")
	public ModelAndView showItemDeleteManagementPage(
			@PathVariable String id,
			ModelAndView mav) {

		//選択された商品のIDを元にDBから商品を取得
		TrProductEntity productEntity = productSelectService.getItemInfo(id);
		mav.addObject(objectName, productEntity);

		mav.setViewName("admin");
		return mav;
	}

	@RequestMapping("/admin/history")
	public ModelAndView SalesHistory(ModelAndView mav) {

		mav.setViewName("history");
		return mav;
	}
}