package org.jdvn.lis.valuation.model;

import java.io.Serializable;

import org.jdvn.lis.valuation.domain.PersonalName;

@SuppressWarnings("serial")
public class SignupRequest implements Serializable {

	private String loginId;
	private String loginPassword;
	private PersonalName name;
	private String email;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public PersonalName getName() {
		return name;
	}

	public void setName(PersonalName name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
