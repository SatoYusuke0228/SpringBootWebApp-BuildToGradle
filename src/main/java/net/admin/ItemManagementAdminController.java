package net.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import net.common.Flag;
import net.product.MsProductCategoryInventoryEntity;
import net.product.MsProductCategoryInventoryService;
import net.product.TrProductDeleteAndUpdateService;
import net.product.TrProductEntity;
import net.product.TrProductSelectService;

@Controller
public class ItemManagementAdminController {

	@Autowired
	private TrProductSelectService productSelectService;

	@Autowired
	private TrProductDeleteAndUpdateService productDeleteAndUpdateService;

	@Autowired
	private MsProductCategoryInventoryService productCategoryService;

	@Autowired
	private HttpSession session;

	final private String OBJECT_NAME = "trProductEntity";
	final private String PAGE_TITLE_NAME = "pageTitle";
	final private String BUTTON_NAME = "buttonName";
	final private String URL_CONPONENT = "urlComponent";
	final private String TWEET_FLAG = "flag";

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
			MsProductCategoryInventoryEntity productCategoryInventory,
			ModelAndView mav) {

		//選択された商品のIDを元にDBから商品を取得
		TrProductEntity productEntity = productSelectService.getItemInfo(id);
		mav.addObject(OBJECT_NAME, productEntity);

		//category名を取得するメソッド
		String categoryName = getCategoryName(productEntity);
		mav.addObject("categoryName", categoryName);

		final Object buttonName = null;
		mav.addObject(PAGE_TITLE_NAME, "商品詳細画面");
		mav.addObject(BUTTON_NAME, buttonName);

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
	@GetMapping("/admin/management/update/{id}")
	public ModelAndView showItemUpdateManagementPage(
			@PathVariable String id,
			TrProductEntity productEntity,
			ModelAndView mav) {

		//選択された商品のIDを元にDBから商品を取得
		productEntity = productSelectService.getItemInfo(id);
		mav.addObject(OBJECT_NAME, productEntity);

		//Viewファイル名セット
		mav.setViewName("productUpdate");

		return mav;
	}

	@PostMapping("/admin/management/update/confirmation")
	public ModelAndView showItemUpdate(
			//@ModelAttribute(OBJECT_NAME)
			@Validated TrProductEntity productEntity,
			BindingResult result,
			ModelAndView mav) {

		//Error情報が格納されるobject名を確認する
		//System.out.println(result.getObjectName());

		if (result.hasErrors()) { //FORMに不備がある場合

			//error詳細をコンソールに表示
			//System.out.println(result.getFieldError());

			//元のページに戻る
			mav.setViewName("productUpdate");

		} else { //FORMに不備がない場合

			//バックエンド側で扱う商品オブジェクトをsessionに預けて処理準備
			session.setAttribute(OBJECT_NAME, productEntity);

			//View側で扱う商品オブジェクト
			mav.addObject(OBJECT_NAME, productEntity);

			//category名を取得するメソッド
			String categoryName = getCategoryName(productEntity);
			mav.addObject("categoryName", categoryName);

			//ページ名と送信ボタン名とURL構成要素をセット
			mav.addObject(PAGE_TITLE_NAME, "商品変更確認画面");
			mav.addObject(BUTTON_NAME, "変更");
			mav.addObject(URL_CONPONENT, "/management/update");
			mav.addObject(TWEET_FLAG, new Flag());

			mav.setViewName("details");
		}

		return mav;
	}

	@RequestMapping("/admin/management/update/result")
	public ModelAndView showItemUpdateResultPage(
			@SessionAttribute(OBJECT_NAME) TrProductEntity productEntity,
			ModelAndView mav) {

		//result画面Viewファイル名セット
		mav.setViewName("admin_result");

		//ログイン中のユーザー名を取得してUpdateUserに設定
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final String loginUserName = auth.getName();
		productEntity.setUpdateUser(loginUserName);

		//現在時刻を取得してUpdateDateに設定
		final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		productEntity.setUpdateDate(timestamp);

		try {
			//商品情報変更を実行
			productDeleteAndUpdateService.saveAndFlush(productEntity);
			mav.addObject("Result", "商品情報変更成功");
		} catch (Exception e) {
			mav.addObject("Result", "商品情報変更失敗");
		}

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

		//ページ名と送信ボタン名とURL構成要素をセット
		mav.addObject(PAGE_TITLE_NAME, "商品削除確認画面");
		mav.addObject(BUTTON_NAME, "削除");
		mav.addObject(URL_CONPONENT, "/management/delete");
		mav.addObject(TWEET_FLAG, new Flag());

		//Viewファイル名セット
		mav.setViewName("details");

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
		) {

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

	/**
	* 商品カテゴリIDからカテゴリ名を取得するメソッド
	* @param productEntity
	* @return categoryName カテゴリ名の文字列
	*/
	private String getCategoryName(TrProductEntity productEntity) {

		//商品カテゴリーIDから商品カテゴリー名を取得
		Optional<MsProductCategoryInventoryEntity> category = productCategoryService
				.findById(productEntity.getProductCategoryId());
		String categoryName = new String(category.get().getProductCategoryName());

		return categoryName;
	}
}