package net.product;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import net.common.Flag;
import net.common.UseTwitter4jTest;

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
public class ProductRegistrationAdminController {

	@Autowired
	private TrProductInsertService productInsertService;

	@Autowired
	private TrProductSelectService productSelectService;

	@Autowired
	private MsProductCategoryInventoryService productCategoryService;

	@Autowired
	private HttpSession session;

	//Error情報が格納されるobject名とViewで使用するためのObject名を統一させる
	final private String OBJECT_NAME = "trProductEntity";

	//viewに渡す命名を統一
	final private String PAGE_TITLE_NAME = "pageTitle";
	final private String BUTTON_NAME = "buttonName";
	final private String URL_CONPONENT = "urlComponent";
	final private String TWEET_FLAG = "flag";

	//Error情報が格納されるobject名を確認する
	//直接ErrorInterfaceのメソッドで設定すると
	//このアルゴリズムだとObject名に設定される前にnullになる為、直接文字列を設定
	//BindingResult result;
	//System.out.println(result.getObjectName());

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

			//商品カテゴリーIDから商品カテゴリー名を取得
			Optional<MsProductCategoryInventoryEntity> category = productCategoryService
					.findById(productEntity.getProductCategoryId());
			String categoryName = new String(category.get().getProductCategoryName());
			mav.addObject("categoryName", categoryName);

			//ページ名と送信ボタン名とURLの構成要素をセット
			mav.addObject(PAGE_TITLE_NAME, "商品登録確認画面");
			mav.addObject(BUTTON_NAME, "登録");
			mav.addObject(URL_CONPONENT, "/registration");

			//確認画面に進む
			mav.setViewName("details");
		}

		//バックエンド側で扱う商品オブジェクトをsessionに預けて処理準備
		session.setAttribute(OBJECT_NAME, productEntity);

		//View側で扱う商品オブジェクト
		mav.addObject(OBJECT_NAME, productEntity);

		//商品を表示設定する場合にtweetするかどうかの決定をするFlagを立てる
		Flag flag = new Flag();

		if (productEntity.getProductShowFlag() == 0) {
			flag.setTweetExcecute(true);
		}

		mav.addObject(TWEET_FLAG, flag);

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
	@PostMapping("/admin/registration/result")
	public ModelAndView showItemRegistrationResultPage(
			@SessionAttribute(OBJECT_NAME) TrProductEntity productEntity,
			@ModelAttribute(TWEET_FLAG) Flag flag,
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

		//商品を新規でDBにInsertQueryを実行
		productInsertService.insert(productEntity);

		//View側で使用する商品登録クエリ実行のflag
		mav.addObject("Result", "商品登録成功");

		//Viewファイル名をセット
		mav.setViewName("admin-result");

		//ツイート実行Flagがtrueの場合は商品をツイートする
		if (flag.isTweetExcecute()) {

			boolean tweetResult = productAdvertisingTweet(productEntity);

			if (tweetResult) {
				mav.addObject("tweetResultMessage", "※ツイートに成功しました");
			} else {
				mav.addObject("tweetResultMessage", "※ツイートに失敗しました");
			}
		}
		return mav;
	}

	/**
	 * tweetTemplate.propertiesからテンプレート文を読み込み
	 * UseTwitter4Jクラスのメソッドを使用してTweetを実行する
	 *
	 * @author SatoYusuke0228
	 * @parm 新規登録処理中の商品オブジェクト
	 * @return tweetResult ツイート実行の結果
	 */
	private boolean productAdvertisingTweet(TrProductEntity productEntity) {

		//ファイルのpathを設定
		final String FILE_PATH = "src/main/resources/tweetTemplate.properties";

		//インスタンス
		final Properties properties = new Properties();
		final UseTwitter4jTest twitter = new UseTwitter4jTest();

		//宣言と初期化
		boolean tweetResult = false;
		InputStreamReader inputStreamReader = null;

		try {

			//pathとファイルの文字コードを設定
			inputStreamReader = new InputStreamReader(new FileInputStream(FILE_PATH), "UTF-8");

			//取得したpathを元にpropaertiesファイルを取得
			properties.load(inputStreamReader);

			//取得したファイルからTweetテンプレート文を取得
			final String tweet1 = properties.getProperty("tweet1");
			final String tweet2 = properties.getProperty("tweet2");
			final String itemLink = properties.getProperty("itemLink");

			//Tweet
			twitter.twitter4jTest(tweet1 + "\b" + tweet2 + "\b" +
					itemLink + productEntity.getProductId());

			System.out.println(tweet1 + "\b" + tweet2 + "\b" +
					itemLink + productEntity.getProductId());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			//InputStreamReaderを閉じる
			try {
				inputStreamReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Tweet実行の結果を返す
		return tweetResult;
	}
}