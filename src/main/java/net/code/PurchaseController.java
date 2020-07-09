package net.code;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 商品購入手続き関係のコントローラー
 * @author SatoYusuke0228
 */
@Controller
public class PurchaseController {

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
	 * checkout画面を表示するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	@RequestMapping("/showform")
	public ModelAndView showForm(ModelAndView mav) {

		//カートの中身に商品があればtrue、なければfalse
		Cart cart = (Cart) session.getAttribute("cart");
		mav.addObject("check", cart.getCartItems().size() != 0);

		//購入情報オブジェクトを作成してモデルに登録
		mav.addObject("checkout", new Checkout());
		mav.setViewName("checkout");

		return mav;
	}

	@GetMapping("/checkout")
    public String getCheckoutPage(Checkout checkaout){
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
	@PostMapping("/purchase")
	public ModelAndView postPurchasePage(
//			@Valid @ModelAttribute("checkout"),
			@Validated Checkout checkout,
			BindingResult result,
			ModelAndView mav) {

		if (result.hasErrors()) {

			// カートの中身に商品があればtrue、なければfalse
			Cart cart = (Cart) session.getAttribute("cart");
			mav.addObject("check", cart.getCartItems().size() != 0);

			// 元の画面に戻りエラーメッセージを表示
			mav.setViewName("checkout");

		} else {

			// カートの中身を初期化
			Cart cart = new Cart();
			session.setAttribute("cart", cart);

			// 購入完了画面を表示
			mav.setViewName("purchase");
		}

		return mav;
	}
}