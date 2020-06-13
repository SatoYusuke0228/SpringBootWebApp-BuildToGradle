package net.code;

/**
 * カート関係のコントローラー
 * @author SatoYusuke0228
 */
//@Controller
public class CartController2 {
//
//	//商品テーブルに関わる処理のインスタンス
//	@Autowired
//	private TrProductService productService;
//
//	//セッションスコープのインスタンス
//	@Autowired
//	private HttpSession session;
//
//	/**
//	 * カート画面を表示するメソッド
//	 * @author SatoYusuke0228
//	 */
//	@RequestMapping("/cart")
//	public String showCartItem() {
//		return "cart";
//	}
//
//	/**
//	 * カートに商品を追加するメソッド
//	 * @author SatoYusuke0228
//	 */
//	@RequestMapping("/cart/add/{id}")
//	public String addCartItem(@PathVariable String id) {
//
//		Cart cart = (Cart) session.getAttribute("cart");
//
//		if (cart == null) {
//			// セッションにカートの登録がなければ新規作成
//			cart = new Cart();
//		}
//
//		//カートにアイテムを追加
//		TrProductEntity selectedItem = productService.getItemInfo(id);
//		CartItem cartItem = new CartItem(selectedItem);
//		cart.addCartItem(cartItem);
//
//		//カートをsessionスコープに保存
//		session.setAttribute("cart", cart);
//
//		return "redirect:/cart";
//	}
//
//	/**
//	 * カートに商品を削除するメソッド
//	 * @author SatoYusuke0228
//	 */
//	@RequestMapping("/cart/remove/{id}")
//	public String removeCartItem(@PathVariable String id) {
//
//		//カートから商品を削除
//		Cart cart = (Cart) session.getAttribute("cart");
//		cart.removeCartItem(id);
//
//		//カートをsessionスコープに保存
//		session.setAttribute("cart", cart);
//
//		return "redirect:/cart";
//	}
}