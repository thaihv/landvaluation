package org.jdvn.lis.valuation.service;
import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdvn.lis.valuation.domain.User;
import org.jdvn.lis.valuation.repository.UserRepository;
import org.jdvn.lis.valuation.support.AuthorizedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(rollbackFor=Exception.class)
public class AuthorizedUserDetailsService extends SavedRequestAwareAuthenticationSuccessHandler implements UserDetailsService {
	
	@Resource
	private UserRepository userRepository;
	
	private static Logger logger = LoggerFactory.getLogger(AuthorizedUserDetailsService.class); 
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException, DataAccessException {
		if (!StringUtils.hasText(username)) {
			throw new UsernameNotFoundException("Username is empty");
		}

		User user;
		if (!username.contains("@")) {
			user = userRepository.findOneByLoginId(username);
		} else {
			user = userRepository.findOneByEmail(username);
		}

		if (user == null) {
			throw new UsernameNotFoundException("User ID not existing. [" + username + "]");
		}
		
		return new AuthorizedUser(user);
	}
	
	@Transactional(readOnly=false)
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		AuthorizedUser authorizedUser = (AuthorizedUser) authentication.getPrincipal();
		userRepository.updateLastLoginTime(authorizedUser.getId(), new Date());
		
		logger.info("\"{}\" logged in. IP: [{}]", authorizedUser.toString(), request.getRemoteAddr());
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
}