package indep.vafl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import indep.vafl.service.AuthentificationComponent;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthentificationComponent authProvider;


	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers("/home","/**").permitAll()
				.and()
				.formLogin().loginPage("/home").loginProcessingUrl("/loginReq").failureUrl("/home?error")
				.usernameParameter("uName").passwordParameter("uPass").defaultSuccessUrl("/home").and().logout().invalidateHttpSession(true)
				.logoutUrl("/logoutReq").logoutSuccessUrl("/home").and().exceptionHandling()
				.accessDeniedPage("/error");

	}

}
