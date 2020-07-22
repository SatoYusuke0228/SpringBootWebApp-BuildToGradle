package net.purchase;

import org.springframework.stereotype.Controller;

/**
 * カート関係のコントローラーだったもの。
 *
 * ①セッションに保存しつつ
 * ②以下の記入法のthymeleafにデータを渡したい場合
 *
 * 無理やり動作させる為に作成したコントローラー。
 * 解決方法の1つとして記念的に保存しておく。
 *
 ********************************************************************************************
	<!-- 前略 -->
	<tr th:each="cart : ${cart.cartItems}" th:object="${cart}">
		<td>[[*{value.name}]]</td>
		<td>[[*{value.quantity}]]</td>
		<td>[[*{value.quantity} * *{value.price}]]</td>
		<td>
			<button
				th:onclick="|location.href='@{'/cart/remove/'+ *{key} }'|">削除</button>
		</td>
	</tr>
	<!-- 後略 -->
 ********************************************************************************************
 * @author SatoYusuke0228
 */
@Controller
public class OldCartController {
//
//	//商品テーブルに関わる処理のインスタンス
//	@Autowired
//	private TrProductService productService;
//
//	@Autowired
//	private HttpSession session;
//
//	@RequestMapping("/cart")
//	public ModelAndView showCartItem(ModelAndView mav) {
//
//		Cart cart = (Cart) session.getAttribute("cart");
//
//		//カートの登録がなければ新規作成
//		if (cart == null) {
//			cart = new Cart();
//		}
//
//		//カートを保存
//		session.setAttribute("cart", cart);
//		mav.setViewName("cart");
//
//		//カートの中身に商品があればtrue、なければfalse
//		mav.addObject("check", cart.getCartItems().size() != 0);
//
//		return mav;
//	}
//
//	/**
//	 * カート画面を表示するメソッド
//	 *
//	 * @author SatoYusuke0228
//	 */
//	@RequestMapping("/cart/show")
//	public ModelAndView showCartItem(
//			ModelAndView mav,
//			RedirectAttributes redirectAttributes) {
//
//		Cart cart = (Cart) session.getAttribute("cart");
//
//		//カートの登録がなければ新規作成
//		if (cart == null) {
//			cart = new Cart();
//		}
//
//		//カートを保存
//		session.setAttribute("cart", cart);
//
//		//カートの中身に商品があればtrue、なければfalse
//		mav.addObject("check", cart.getCartItems().size() != 0);
//
//		//redirect準備
//		redirectAttributes.addFlashAttribute("cart", cart);
//		mav.setViewName("redirect:/cart");
//
//		return mav;
//	}
//
//	/**
//	 * カートに商品を追加するメソッド
//	 *
//	 * @author SatoYusuke0228
//	 */
//	@RequestMapping("/cart/add/{id}")
//	public ModelAndView addCartItem(
//			@PathVariable String id,
//			ModelAndView mav,
//			RedirectAttributes redirectAttributes) {
//
//		Cart cart = (Cart) session.getAttribute("cart");
//		//Cart cart = (Cart) redirectAttributes.getAttribute("cart");
//
//		//カートの登録がなければ新規作成
//		if (cart == null) {
//			cart = new Cart();
//		}
//
//		//カートにアイテムを追加
//		TrProductEntity selectedItem = productService.getItemInfo(id);
//		CartItem cartItem = new CartItem(selectedItem);
//		cart.addCartItem(cartItem);
//
//		//コンソール表示テスト
//		//System.out.println(cart.getCartItems().get(id).getName());
//
//		//カートを保存
//		session.setAttribute("cart", cart);
//
//		//redirect準備
//		redirectAttributes.addFlashAttribute("cart", cart);
//		mav.setViewName("redirect:/cart");
//
//		//カートの中身に商品があれmav.setViewName("redirect:/cart");ばtrue、なければfalse
//		mav.addObject("check", cart.getCartItems().size() != 0);
//
//		return mav;
//	}
//
//	/**
//	 * カートに商品を削除するメソッド
//	 * @author SatoYusuke0228
//	 */
//	@RequestMapping("/cart/remove/{id}")
//	public ModelAndView removeCartItem(
//			@PathVariable String id,
//			ModelAndView mav,
//			RedirectAttributes redirectAttributes) {
//
//		//カートから商品を削除
//		Cart cart = (Cart) session.getAttribute("cart");
//		cart.removeCartItem(id);
//
//		//カートを保存
//		session.setAttribute("cart", cart);
//
//		//redirect準備
//		redirectAttributes.addFlashAttribute("cart", cart);
//		mav.setViewName("redirect:/cart");
//
//		//カートの中身に商品があればtrue、なければfalse
//		mav.addObject("check", cart.getCartItems().size() != 0);
//
//		return mav;
//	}
}
