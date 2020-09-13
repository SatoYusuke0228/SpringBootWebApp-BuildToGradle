package net.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	/**
	 *「spring-boot-starter-security」が自動的に管理者画面へ遷移させてくれるため、
	 * 遷移b先がどの画面なのかを「spring-oot-starter-security」に認識させる役割のクラス
	 *
	 * @author SatoYusuke0228
	 * @param ViewControllerRegistry registry
	 *        引数 ステータスコードやビューで事前に設定された簡単な自動コントローラーの登録を支援
	 */
	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/admin"). //URLの指定
				setViewName("admin"); //ファイル名の指定
	}
}