package net.purchase;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.product.TrProductEntity;
import net.product.TrProductSelectService;

/**
 * カート関係のコントローラー
 * @author SatoYusuke0228
 */
@Controller
public class CartController {

	//商品テーブルに関わる処理のインスタンス
	@Autowired
	private TrProductSelectService productService;

	//セッションスコープのインスタンス
	@Autowired
	private HttpSession session;

	/**
	 * カート画面を表示するメソッド
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/cart")
	public ModelAndView showCartItem(ModelAndView mav) {

		//cart情報をセッションから取得
		Cart cart = (Cart) session.getAttribute("cart");

		//セッションにカートの登録がなければ新規作成
		if (cart == null) {
			cart = new Cart();
		}

		//カートを保存
		session.setAttribute("cart", cart);

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", cart.getCartItems().size() != 0);

		//HTMLファイル名を指定
		mav.setViewName("cart");

		return mav;
	}

	/**
	 * カートに商品を追加するメソッド
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/cart/add/{id}")
	public ModelAndView addCartItem(
			@PathVariable String id,
			ModelAndView mav) {

		//cart情報をセッションから取得
		Cart cart = (Cart) session.getAttribute("cart");

		//セッションにカートの登録がなければ新規作成
		if (cart == null) {
			cart = new Cart();
		}

		//カートにアイテムを追加
		TrProductEntity selectedItem = productService.getItemInfo(id);
		CartItem cartItem = new CartItem(selectedItem);
		cart.addCartItem(cartItem);

		//カートをsessionスコープに保存
		session.setAttribute("cart", cart);

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", cart.getCartItems().size() != 0);

		//HTMLファイル名を指定
		mav.setViewName("cart");

		return mav;
	}

	/**
	 * カートに商品を削除するメソッド
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/cart/remove/{id}")
	public ModelAndView removeCartItem(
			@PathVariable String id,
			ModelAndView mav) {

		//cart情報をセッションから取得
		Cart cart = (Cart) session.getAttribute("cart");

		//商品IDを元にカートから商品を削除
		cart.removeCartItem(id);

		//カートをsessionスコープに保存
		session.setAttribute("cart", cart);

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", cart.getCartItems().size() != 0);

		//HTMLファイル名を指定
		mav.setViewName("cart");

		return mav;
	}
}