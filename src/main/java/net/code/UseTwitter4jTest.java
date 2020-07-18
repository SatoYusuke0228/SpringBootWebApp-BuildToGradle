package net.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Twitter4J APIを使用するためのクラス
 *
 * @author SatoYusuke0228
 * @parm args ツイートする文字列
 */
public class UseTwitter4jTest {

	public void twitter4jTest(String twit) {

		if (twit != null) {
			System.out.println("Usage: java twitter4j.examples.tweets.UpdateStatus [text]");
			System.exit(-1);
		}
		try {
			//ルート構成を使用してTwitterFactoryを作成
			Twitter twitter = new TwitterFactory().getInstance();

			try {

				//リクエストトークン取得（Auth認可方式）
				//トークンがすでに利用可能な場合、IllegalStateExceptionをスロー
				RequestToken requestToken = twitter.getOAuthRequestToken();

				System.out.println("Got request token.");
				System.out.println("Request token: " + requestToken.getToken());
				System.out.println("Request token secret: " + requestToken.getTokenSecret());

				//アクセストークン
				AccessToken accessToken = null;

				//テキストファイルを読み込むためのクラスBufferedReaderを用意
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

				//もしアクセストークンが設定されていなければ、BufferedReaderを使用してtwitter4j.propertiesから取得
				while (null == accessToken) {

					System.out.println("Open the following URL and grant access to your account:");
					System.out.println(requestToken.getAuthorizationURL());
					System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");

					//BufferedReaderを使用してtwitter4j.propertiesから取得した文字列
					String pin = br.readLine();

					try {
						//トークンの文字列が存在する場合はtwitter4j.propertiesから取得した文字列をトークンに設定
						if (pin.length() > 0) {
							accessToken = twitter.getOAuthAccessToken(requestToken, pin);
						} else {
							accessToken = twitter.getOAuthAccessToken(requestToken);
						}
					//アクセストークンが取得できなかった場合のエラー
					} catch (TwitterException te) {
						if (401 == te.getStatusCode()) {
							System.out.println("Unable to get the access token.");
						} else {
							te.printStackTrace();
						}
					}
				}

				System.out.println("Got access token.");
				System.out.println("Access token: " + accessToken.getToken());
				System.out.println("Access token secret: " + accessToken.getTokenSecret());

			} catch (IllegalStateException ie) {
				//アクセストークンがすでに利用可能であるか、もしくはコンシューマキー/シークレットが設定されていない場合
				if (!twitter.getAuthorization().isEnabled()) {
					System.out.println("OAuth consumer key/secret is not set.");
					System.exit(-1);
				}
			}

			//ツイート
			Status status = twitter.updateStatus(twit);

			System.out.println("Successfully updated the status to [" + status.getText() + "].");
			System.exit(0);

		} catch (TwitterException te) {

			te.printStackTrace();

			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);

		} catch (IOException ioe) {

			ioe.printStackTrace();

			System.out.println("Failed to read the system input.");
			System.exit(-1);
		}
	}
}