package net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * エントリーポイント
 * @author SatoYusuke0228
 */
@SpringBootApplication
public class SpringBootWebAppApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebAppApplication.class, args);
	}
}