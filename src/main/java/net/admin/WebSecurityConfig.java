package net.admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * 「css」や「image」配下のファイルにアクセスする際に
	 * セキュリティ認証する必要がないようにする設定
	 *
	 * ignoring()を使用することでアクセス制御を無視できる
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				"/image/**",
				"/css/**",
				"/js/**");
	}

	/**
	 * トップページや商品詳細ページはログインしなくても見せたいページなので、
	 *  これらのページは引き続き誰でも自由にアクセスすることを可能にする
	 *
	 * @param  .antMatchers("/admin/**")
	 *         .authenticated()
	 *         「/adminのパスのページは全て認証が必要」と設定
	 *
	 * @param  .formLogin()
	 *         .permitAll();
	 *          ログインページをだれでも閲覧できるように設定
	 *
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/admin/**")
				.authenticated()
				.and()
				.formLogin()
				.permitAll();
	}

	/**
	 * ログインユーザとパスワードを静的に登録しているmethod
	 *
	 * @return
	 *「ユーザー名 = admin/パスワード = admin」でログインできるようユーザ情報をまとめた処理
	 */
	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("admin")
				.password("admin")
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
}
