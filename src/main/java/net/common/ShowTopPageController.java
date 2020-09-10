package net.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import net.product.MsProductCategoryInventoryService;
import net.product.TrProductEntity;

@Controller
public class ShowTopPageController {

	@Autowired
	MsProductCategoryInventoryService categoryService;

	/**
	 * TOPページでオススメ商品を「productCategoryId == 0」から４種取得するためのメソッド。
	 *
	 * 「商品の表示設定がON」になっている商品の数を算定し、
	 * その数が４以上の場合のみ、４種類のオススメ商品をピックアップして、
	 * TOPページのスライド写真として掲載する。
	 *
	 * @author SatoYusuke0228
	 */
	@GetMapping("/")
	public String index(Model model) {

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
			model.addAttribute("recommendedItems", randomPickupRecommendedItems(items));
		}

		return "index";
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
}
