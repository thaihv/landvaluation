package org.jdvn.lis.valuation.service;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.jdvn.lis.valuation.domain.User;
import org.jdvn.lis.valuation.exception.DuplicateEmailException;
import org.jdvn.lis.valuation.exception.DuplicateLoginIdException;
import org.jdvn.lis.valuation.exception.ServiceException;
import org.jdvn.lis.valuation.model.SignupRequest;
import org.jdvn.lis.valuation.repository.UserRepository;
import org.jdvn.lis.valuation.support.AuthorizedUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor=Exception.class)
public class SignupService {

	@Resource
	private UserRepository userRepository;


	public AuthorizedUser signup(SignupRequest request, User.Role role) throws ServiceException {
		return signup(request, role, null);
	}

	public AuthorizedUser signup(SignupRequest request, User.Role role, String token) throws ServiceException {

		User duplicate;
		duplicate = userRepository.findOneByLoginId(request.getLoginId());
		if (duplicate != null) {
			throw new DuplicateLoginIdException(request.getLoginId());
		}
		duplicate = userRepository.findOneByEmail(request.getEmail());
		if (duplicate != null) {
			throw new DuplicateEmailException(request.getEmail());
		}
		LocalDateTime now = LocalDateTime.now();

		User user = new User();
		user.setLoginId(request.getLoginId());
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setLoginPassword(passwordEncoder.encode(request.getLoginPassword()));
		user.getName().setFirstName(request.getName().getFirstName());
		user.getName().setLastName(request.getName().getLastName());
		user.setEmail(request.getEmail());
		user.getRoles().add(role);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);
		user = userRepository.saveAndFlush(user);

		AuthorizedUser authorizedUser = new AuthorizedUser(user);

		return authorizedUser;
	}
}
