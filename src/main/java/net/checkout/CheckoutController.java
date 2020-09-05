package net.checkout;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import net.cart.Cart;
import net.charge.ChargeRequest;
import net.charge.StripeService;

/**
 * 商品購入手続き関係のコントローラー
 * @author SatoYusuke0228
 */
@Controller
public class CheckoutController {

	@Autowired
	private StripeService stripeService;

	//StripeAPIパブリックキー
	@Value("${STRIPE__PUBLIC__KEY}")
	private String stripePublicKey;

	//セッションスコープのインスタンス
	@Autowired
	private HttpSession session;

	/**
	 * Form入力欄にスペース等が入れられた場合にトリミングするメソッド
	 * @author SatoYusuke0228
	 */
	@InitBinder
	public void InitBinder(WebDataBinder dataBinder) {

		StringTrimmerEditor editor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, editor);
	}

	/**
	 * form画面を表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@GetMapping("/showform")
	public ModelAndView showForm(
			@SessionAttribute("cart") Cart cart,
			ModelAndView mav) {

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", 0 < cart.getCartItems().size());

		//購入情報オブジェクトを作成してモデルに登録
		mav.addObject("checkout", new Checkout());
		mav.setViewName("form");

		session.setAttribute("cart", cart);

		return mav;
	}

	@GetMapping("/checkout")
	public String getCheckoutPage() {
		return "checkout";
	}

	/**
	 * 「購入」ボタンがクリックされた時の処理メソッド
	 *
	 * @parm checkout
	 * 引数1
	 * 名前やクレジットカードなど
	 * 入力されたデータを持っているオブジェクト.
	 *
	 * @parm result
	 * 引数2
	 * バリデーションチェックの結果が格納されています。
	 * hasErrors()メソッドの戻り値がtrueの場合は、
	 * バリデーションチェックがエラーということで、
	 * もういちどcheckout画面へ遷移してエラーメッセージを表示。
	 * OKの場合はカートを空にして購入完了画面purchase.htmlへ遷移。
	 *
	 * @author SatoYusuke0228
	 */
	@PostMapping("/checkout")
	public ModelAndView postPurchasePage(
			@SessionAttribute("cart") Cart cart,
			@Validated Checkout checkout,
			BindingResult result,
			ModelAndView mav) {

		//カートの中身に商品があればtrue、なければfalse
		mav.addObject("check", 0 < cart.getCartItems().size());

		if (result.hasErrors()) { //FROMに不備があれば入力画面を再表示する

			// 元の画面に戻りエラーメッセージを表示
			mav.setViewName("form");

		} else { //FORMに不備がなければ販売処理をする

			session.setAttribute("checkout", checkout);

			//請求金額と通貨単位のセット
			mav.addObject("amount", cart.getGrandTotal()); //in JPY
			mav.addObject("currency", ChargeRequest.Currency.JPY);
			//mav.addObject("billing_details", stripeService.setBillingDetails(checkout));

			//StripePublicキー
			mav.addObject("stripePublicKey", stripePublicKey);

			// 購入確認画面を表示
			mav.setViewName("checkout");
		}
		return mav;
	}
}