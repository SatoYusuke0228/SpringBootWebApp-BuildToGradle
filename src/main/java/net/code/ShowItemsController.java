package net.code;

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
	private TrProductSelectService productSelect;

	//商品カテゴリーテーブルに関わる処理のインスタンス(MsProductCategoryInventoryServiceインターフェース)
	@Autowired
	private MsProductCategoryInventoryService categoryService;

	//セッションスコープのインスタンス
	//	@Autowired
	//	private HttpSession session;

	/**
	 * TOPページで商品情報を取得するためのメソッド。
	 * このメソッドで取得した商品情報は、TOPページのスライド写真として掲載される。
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/")
	public ModelAndView showRecommendedItems(ModelAndView mav) {

		//カテゴリーIDが0の商品のみを取得
		List<TrProductEntity> items = categoryService.findById(0).get().getTrProductEntity();

		//取得した全販売商品データをランダムで商品ピックアップするメソッドに渡してmodelに保存
		mav.setViewName("index");
		mav.addObject("recommendedItems", randomPickupRecommendedItems(items));
		return mav;
	}

	/**
	 * TOPページで商品情報を取得するためのヘルパー関数。
	 * {productCategryId == 0}に属する全商品Listを受け取り、ランダムで４種オススメ商品をピックアップして格納したListを戻り値として返す。
	 *
	 * @param items
	 * 引数。全商品が格納されているList
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

			//ランダムに抽出した商品と、すでにオススメ商品Listに含まれる商品が重複していないかチェック
			for (int i = recommendedItems.size(); 0 < i; i--) {
				if (items.get(randomIndex) == recommendedItems.get(i - 1)) {
					duplication++;
				}
			}
			//重複していない場合のみ、オススメ商品Listに格納する
			if (duplication == 0) {
				recommendedItems.add(items.get(randomIndex));
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

		if (0 < items.size()) {
			viewName = "item-list";
			mav.addObject("items", items);
		} else {
			//			viewName = notFoundItemPage();
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

		List<TrProductEntity> items = productSelect.findByKeyword(keyword);

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
	public ModelAndView showItemPage(@PathVariable String id, ModelAndView mav) {

		//指定されたIDの商品を取得
		TrProductEntity item = productSelect.getItemInfo(id);

		//EntityをModelに登録
		mav.addObject("item", item);
		mav.setViewName("item");

		return mav;
	}
}
