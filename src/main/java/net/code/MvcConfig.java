package net.code;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *「spring-boot-starter-security」が自動的に管理者画面へ遷移させてくれるため、
 * 遷移先がどの画面なのかを「spring-boot-starter-security」に認識させる役割のクラス
 *
 * @author SatoYusuke0228
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/admin").setViewName("admin");
	}
}