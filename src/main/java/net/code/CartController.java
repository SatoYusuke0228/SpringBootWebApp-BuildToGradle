package net.code;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

	/**
	 * カート画面を表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/cart")
	public String showCartItem() {
		return "cart";
	}

	/**
	 * カートに商品を追加するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/cart/add/{id}")
	public String addCartItem(
			@PathVariable String id,
//			Model model,
			RedirectAttributes redirectAttributes) {

		Cart cart = (Cart) session.getAttribute("cart");
//		Cart cart = (Cart) redirectAttributes.getAttribute("cart");

		//modelにカートの登録がなければ新規作成
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

	    //redirect
		redirectAttributes.addFlashAttribute("cart", cart);
		
		return "redirect:/cart";
	}

	/**
	 * カートに商品を削除するメソッド
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/cart/remove/{id}")
	public String removeCartItem(
			@PathVariable String id,
//			Model model,
			RedirectAttributes redirectAttributes) {

		//カートから商品を削除
		Cart cart = (Cart) session.getAttribute("cart");
		cart.removeCartItem(id);

		//カートを保存
		session.setAttribute("cart", cart);
		
		 //redirect
		redirectAttributes.addFlashAttribute("cart", cart);
		return "redirect:/cart";
	}
}
