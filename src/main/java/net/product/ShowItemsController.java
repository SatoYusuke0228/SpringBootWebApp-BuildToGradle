package net.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	 * TOPページでオススメ商品を「productCategoryId == 0」から４種取得するためのメソッド。
	 *
	 * 「商品の表示設定がON」になっている商品の数を算定し、
	 * その数が４以上の場合のみ、４種類のオススメ商品をピックアップして、
	 * TOPページのスライド写真として掲載する。
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/")
	public ModelAndView showRecommendedItems(ModelAndView mav) {

		//カテゴリーIDが0の商品のみを取得
		List<TrProductEntity> items = categoryService.findById(0).get().getTrProductEntity();

		//「商品の表示設定がON」になっている商品の数を示す変数
		int showFlag = 0;

		//「商品の表示設定がON」になっている商品の数を算定
		for (TrProductEntity check : items) {
			if (check.getProductShowFlag() == 0) {
				showFlag++;
			}
		}

		if (4 <= showFlag) {
			mav.addObject("recommendedItems", randomPickupRecommendedItems(items));
		}

		//取得した全販売商品データをランダムで商品ピックアップするメソッドに渡してmodelに保存
		mav.setViewName("index");

		return mav;
	}

	/**
	 * TOPページで商品情報を取得するためのヘルパー関数。
	 * {productCategryId == 0}に属する商品Listを受け取り、
	 * ランダムで４種オススメ商品をピックアップして格納したListを戻り値として返す。
	 *
	 * @param items
	 * 引数。{productCategryId == 0}に属する全商品が格納されているList
	 *
	 * @author SatoYusuke0228
	 */
	private List<TrProductEntity> randomPickupRecommendedItems(List<TrProductEntity> items) {

		//オススメの商品リストのインスタンスを作成
		List<TrProductEntity> recommendedItems = new ArrayList<TrProductEntity>();

		//ランダムクラスのインスタンスを作成
		Random random = new Random();

		//オススメ商品Listのサイズが４つになるまで商品をランダム抽選
		while (recommendedItems.size() < 4) {

			//ランダムに抽出した商品と、すでにオススメ商品Listに含まれる商品の重複数
			int duplication = 0;

			//商品をランダム抽選
			int randomIndex = random.nextInt(items.size() - 1);
			TrProductEntity randomPickupItem = items.get(randomIndex);

			//ランダムに抽出した商品と、すでにオススメ商品Listに含まれる商品が重複していないかチェック
			for (int i = recommendedItems.size(); 0 < i; i--) {
				if (randomPickupItem == recommendedItems.get(i - 1)) {
					duplication++;
				}
			}

			//重複していない場合のみ、オススメ商品Listに格納する
			if (duplication == 0 && randomPickupItem.getProductShowFlag() == 0) {
				recommendedItems.add(randomPickupItem);
			}
		}

		return recommendedItems;
	}

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
