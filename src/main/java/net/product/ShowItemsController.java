package net.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShowItemsController {

	//商品テーブルのセレクト文に関わる処理のインスタンス(TrProductServiceインターフェース)
	@Autowired
	private TrProductSelectService productSelectService;

	//商品カテゴリーテーブルに関わる処理のインスタンス(MsProductCategoryInventoryServiceインターフェース)
	@Autowired
	private MsProductCategoryInventoryService categoryService;

	//セッションスコープのインスタンス
	//	@Autowired
	//	private HttpSession session;

	/**
	 * 商品一覧ページをカテゴリーごとに表示するためのメソッド
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/{viewName}/category/{categoryId}")
	public ModelAndView showItemsByKeyword(
			@PathVariable String viewName,
			@PathVariable int categoryId,
			ModelAndView mav) {

		List<TrProductEntity> items = categoryService.findById(categoryId).get().getTrProductEntity();

		//取得した商品情報の表示FlagがなければListから削除する
		for (int i = items.size(); 0 < i; i--) {
			if (items.get(i - 1).getProductShowFlag() != 0) {
				items.remove(i - 1);
			}
		}

		if (0 < items.size()) {
			viewName = "item-list";
			mav.addObject("items", items);
		} else {
			//viewName = notFoundItemPage();
		}

		mav.setViewName(viewName);
		return mav;
	}

	/**
	 * 商品一覧ページを検索ワードごとに表示するためのメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/{viewName}/search")
	public ModelAndView sendItemsByKeyword(
			@PathVariable String viewName,
			@RequestParam String keyword,
			ModelAndView mav) {

		List<TrProductEntity> items = productSelectService.findByKeyword(keyword);

		//取得した商品情報の表示FlagがなければListから削除する
		for (int i = items.size(); 0 < i; i--) {
			if (items.get(i - 1).getProductShowFlag() != 0) {
				items.remove(i - 1);
			}
		}

		//もしListの中に商品があれば、商品一覧ページに遷移
		//しかし、Listの中に商品がなければ、商品が見つからないという結果を表示するページに遷移
		if (0 < items.size()) {
			viewName = "item-list";
			mav.addObject("items", items);
		} else {
			//viewName = notFoundItemPage();
		}

		mav.setViewName(viewName);

		return mav;
	}

	/**
	 * 商品詳細ページを表示するためのメソッド
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/item/{id}")
	public ModelAndView showItemPage(
			@PathVariable String id,
			ModelAndView mav) {

		//指定されたIDの商品を取得
		TrProductEntity item = productSelectService.getItemInfo(id);

		//もし引数のIDを持つ商品が非表示設定だったらerrorページに遷移するようにする
		if (item.getProductShowFlag() != 0) {
			item = null;
		}

		String buttonName = new String();
		String urlConponent = new String();

		if (item.getProductStock() == 0) {
			buttonName = "※商品の在庫がありません";
			urlConponent = "/item/";
		} else {
			buttonName = "カートに入れる";
			urlConponent = "/cart/add/";
		}

		//EntityやObjectをviewで使用可能にする
		mav.addObject("item", item);
		mav.addObject("buttonName", buttonName);
		mav.addObject("urlConponent", urlConponent);

		mav.setViewName("item");

		return mav;
	}
}
