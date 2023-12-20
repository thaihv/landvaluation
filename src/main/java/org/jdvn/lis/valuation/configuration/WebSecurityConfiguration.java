package org.jdvn.lis.valuation.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.jdvn.lis.valuation.service.AuthorizedUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		  prePostEnabled = true, 
		  securedEnabled = true, 
		  jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AccessDecisionManager accessDecisionManager;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private PersistentTokenRepository persistentTokenRepository;

	@Override
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {		
		auth
			.userDetailsService(authorizedUserDetailsService())
			.passwordEncoder(passwordEncoder());
	}
   
   @Override
   protected void configure(HttpSecurity http) throws Exception {
		http
		.cors()
		.and()
		.csrf()
		.disable()
		.authorizeRequests()
		.accessDecisionManager(accessDecisionManager)
		.antMatchers(
				"/",
				"/signup/**",
				"/style/**",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js").permitAll()
		.antMatchers("/**").hasAnyRole("ACTIVITI_USER", "ACTIVITI_ADMIN")
		.anyRequest()
		.authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.permitAll()
		.and()
		.logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
		.logoutSuccessUrl("/login")
		.and()
		.rememberMe()
			.tokenRepository(persistentTokenRepository)
			.and()
		.headers()
			.frameOptions().disable()
			.cacheControl().disable()
			.httpStrictTransportSecurity().disable()
			.and()
		.csrf()
			.disable()
		.exceptionHandling()
			.accessDeniedPage("/login");     	      
   }
   @Override
   public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**");
		web.ignoring().mvcMatchers(
				"/swagger-ui.html/**", 
				"/configuration/**", 
				"/swagger-resources/**", 
				"/v2/api-docs",
				"/webjars/**");
   }    
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl repository = new JdbcTokenRepositoryImpl();
		repository.setDataSource(dataSource);
		return repository;
	}
	@Bean
	public UserDetailsService authorizedUserDetailsService() {
		return new AuthorizedUserDetailsService();
	}	

	@Bean
	public AffirmativeBased accessDecisionManager() {
		List<AccessDecisionVoter<?>> accessDecisionVoters = new ArrayList<>();
		accessDecisionVoters.add(roleVoter());
		accessDecisionVoters.add(webExpressionVoter());

		AffirmativeBased accessDecisionManager = new AffirmativeBased(accessDecisionVoters);
		return accessDecisionManager;
	}
	@Bean
	public WebExpressionVoter webExpressionVoter() {
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
		return webExpressionVoter;
	}
	@Bean
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
		DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy());
		return defaultWebSecurityExpressionHandler;
	}
	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
		hierarchy.setHierarchy("ROLE_ACTIVITI_ADMIN > ROLE_ACTIVITI_USER");
		return hierarchy;
	}

	@Bean
	public RoleVoter roleVoter() {
		return new RoleHierarchyVoter(roleHierarchy());
	}	
}
