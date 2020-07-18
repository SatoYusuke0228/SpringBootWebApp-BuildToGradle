package net.code;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

/*
 * 管理者の商品追加機能をまとめたControllerクラス
 *
 * 以下のように遷移させる予定である
 *
 * 入力FORMページ→[送信ボタン]→商品登録内容を確認ページ→[登録ボタン]→商品登録完了ページ
 *
 * @auther SatoYusuke0228
 */
@Controller
public class ItemRegistrationAdminController {

	@Autowired
	private TrProductInsertService productInsertService;

	@Autowired
	private TrProductSelectService productSelectService;

	@Autowired
	private HttpSession session;

	//Error情報が格納されるobject名とViewで使用するためのObject名を統一させる
	final private String OBJECT_NAME = "trProductEntity";

	//Error情報が格納されるobject名を確認する
	//直接ErrorInterfaceのメソッドで設定すると
	//このアルゴリズムだとObject名に設定される前にnullになる為、直接文字列を設定
	//BindingResult result;
	//System.out.println(result.getObjectName());

	/**
	 * 商品登録フォーム画面に遷移するメソッド
	 * 登録予定の商品のインスタンスを作成して新規登録の準備を行う
	 *
	 * @auther SatoYusuke0228
	 **/
	@GetMapping("/admin/registration")
	public ModelAndView getItemRegistrationPage(ModelAndView mav) {

		mav.addObject(OBJECT_NAME, new TrProductEntity());
		mav.setViewName("registration");

		return mav;
	}

	/**
	 * 商品登録フォーム画面の入力内容に不備があれば元の画面に戻りvalidationエラーメッセージを表示
	 * もし入力内容に不備がなければログインユーザーネームと現在時刻を取得し入力内容を確認する画面に遷移
	 *
	 * @auther SatoYusuke0228
	 **/
	@PostMapping("/admin/registration/confirmation")
	public ModelAndView postItemRegistrationPage(
			@Validated TrProductEntity productEntity,
			BindingResult result,
			ModelAndView mav) {

		//Error情報が格納されるobject名を確認する
		//System.out.println(result.getObjectName());

		if (result.hasErrors()) { //入力FORMに不備があれば

			//error詳細をコンソールに表示
			//System.out.println(result.getFieldError());

			//元のページに戻る
			mav.setViewName("registration");

		} else { //入力FORMに不備がなければ

			//確認画面に進む
			mav.setViewName("registration_confirmation");
		}

		//バックエンド側で扱う商品オブジェクトをsessionに預けて処理準備
		session.setAttribute(OBJECT_NAME, productEntity);

		//View側で扱う商品オブジェクト
		mav.addObject(OBJECT_NAME, productEntity);

		//商品表示設定のオブジェクト
		boolean twitFlag = false;

		if (productEntity.getProductShowFlag() == 0) {
			twitFlag = true;
		}
		mav.addObject("twitFlag", twitFlag);

		return mav;
	}

	/**
	 * 新規商品登録を確定させる処理を記述したメソッド
	 *
	 * sessionScopeの新規登録予定の商品情報を取得して、
	 * 既存のDB内の商品IDもしくは商品名と重複していないか確認。
	 *
	 * 既存のDBの商品と重複していない場合は、
	 * ServiceインターフェースやRepositoryインターフェースを経由して
	 * JpaRepositoryのsave()メソッドを実行し、DBに新規商品を追加する処理を実行する。
	 *
	 * 最後にsessionScopeの中の商品情報を初期化。
	 * @throws FileNotFoundException
	 *
	 * @auther SatoYusuke0228
	 **/
	@RequestMapping("/admin/registration/result")
	public ModelAndView showItemRegistrationResultPage(
			@SessionAttribute(OBJECT_NAME) TrProductEntity productEntity,
			boolean twitExcecute,
			ModelAndView mav) {

		//DBから登録済みの商品をListとして取得
		List<TrProductEntity> productListInDB = productSelectService.findAll();

		//DBから取得した商品リストと、新規で登録しようとしている商品を比較
		for (int i = productListInDB.size(); 0 < i; i--) {

			//もしDBの商品リストと、新規で登録商品の商品ID or 商品名が重複した場合は登録処理はしない
			if (productListInDB.get(i - 1).getProductId().equals(productEntity.getProductId()) ||
					productListInDB.get(i - 1).getProductName().equals(productEntity.getProductName())) {

				//View側で使用する商品登録クエリ実行のflag
				mav.addObject("Result", "商品登録失敗");

				//商品登録失敗のresult画面に遷移
				mav.setViewName("admin_result");

				return mav;
			}
		}

		//ログイン中のユーザー名を取得してInsertUserとUpdateUserに設定
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		final String loginUserName = auth.getName();
		productEntity.setInsertUser(loginUserName);
		productEntity.setUpdateUser(loginUserName);

		//現在時刻を取得してInsertDateとUpdateDateに設定
		final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		productEntity.setInsertDate(timestamp);
		productEntity.setUpdateDate(timestamp);

		//商品を新規でDBにInsertして、sessionScopeの中身を初期化
		productInsertService.insert(productEntity);

		//View側で使用する商品登録クエリ実行のflag
		mav.addObject("Result", "商品登録成功");

		//result画面に遷移
		mav.setViewName("admin_result");

		//ツイート実行Flagがtrueの場合は商品をツイートする
		if (twitExcecute) {
			productPropagandaTweet();
		}

		return mav;
	}

	/**
	 * tweetTemplate.propertiesからテンプレート文を読み込み
	 * Tweetを実行するメソッド
	 *
	 * @author SatoYusuke0228
	 */
	private void productPropagandaTweet() {

		//ファイルのpathを設定
		final String FILE_PATH = "src/main/resources/tweetTemplate.properties";

		//インスタンスと初期化
		Properties properties = new Properties();
		InputStreamReader inputStream = null;
		UseTwitter4jTest twitter = new UseTwitter4jTest();

		try {
			//pathを設定
			inputStream = new InputStreamReader(new FileInputStream(FILE_PATH), "UTF-8");

			//取得したpathを元にpropaertiesファイルを取得
			properties.load(inputStream);

			//Tweet
			String tweet1 = properties.getProperty("tweetTemplateText1");
			String tweet2 = properties.getProperty("tweetTemplateText2");
			twitter.twitter4jTest(tweet1 + "\b" + tweet2 );

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//InputStreamReaderを閉じる
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}