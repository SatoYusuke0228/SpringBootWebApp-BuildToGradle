package net.code;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * カート関係のコントローラー
 * @author SatoYusuke0228
 */
@Controller
public class CartController {

	//商品テーブルに関わる処理のインスタンス
	@Autowired
	private TrProductService productService;

	@Autowired
	private HttpSession session;

	@RequestMapping("/cart")
	public ModelAndView showCartItem(ModelAndView mav) {

		Cart cart = (Cart) session.getAttribute("cart");

		//カートの登録がなければ新規作成
		if (cart == null) {
			cart = new Cart();
		}

		//カートを保存
		session.setAttribute("cart", cart);
		mav.setViewName("cart");

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", cart.getCartItems().size() != 0);

		return mav;
	}

	/**
	 * カート画面を表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/cart/show")
	public ModelAndView showCartItem(
			ModelAndView mav,
			RedirectAttributes redirectAttributes) {

		Cart cart = (Cart) session.getAttribute("cart");

		//カートの登録がなければ新規作成
		if (cart == null) {
			cart = new Cart();
		}

		//カートを保存
		session.setAttribute("cart", cart);

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", cart.getCartItems().size() != 0);

		//redirect準備
		redirectAttributes.addFlashAttribute("cart", cart);
		mav.setViewName("redirect:/cart");

		return mav;
	}

	/**
	 * カートに商品を追加するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/cart/add/{id}")
	public ModelAndView addCartItem(
			@PathVariable String id,
			ModelAndView mav,
			RedirectAttributes redirectAttributes) {

		Cart cart = (Cart) session.getAttribute("cart");
		//Cart cart = (Cart) redirectAttributes.getAttribute("cart");

		//カートの登録がなければ新規作成
		if (cart == null) {
			cart = new Cart();
		}

		//カートにアイテムを追加
		TrProductEntity selectedItem = productService.getItemInfo(id);
		CartItem cartItem = new CartItem(selectedItem);
		cart.addCartItem(cartItem);

		//コンソール表示テスト
		//System.out.println(cart.getCartItems().get(id).getName());

		//カートを保存
		session.setAttribute("cart", cart);

		//redirect準備
		redirectAttributes.addFlashAttribute("cart", cart);
		mav.setViewName("redirect:/cart");

		//カートの中身に商品があれmav.setViewName("redirect:/cart");ばtrue、なければfalse
		mav.addObject("check", cart.getCartItems().size() != 0);

		return mav;
	}

	/**
	 * カートに商品を削除するメソッド
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/cart/remove/{id}")
	public ModelAndView removeCartItem(
			@PathVariable String id,
			ModelAndView mav,
			RedirectAttributes redirectAttributes) {

		//カートから商品を削除
		Cart cart = (Cart) session.getAttribute("cart");
		cart.removeCartItem(id);

		//カートを保存
		session.setAttribute("cart", cart);

		//redirect準備
		redirectAttributes.addFlashAttribute("cart", cart);
		mav.setViewName("redirect:/cart");

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", cart.getCartItems().size() != 0);

		return mav;
	}
}
