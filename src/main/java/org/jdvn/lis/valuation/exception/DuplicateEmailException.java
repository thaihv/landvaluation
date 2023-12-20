package org.jdvn.lis.valuation.exception;

public class DuplicateEmailException extends ServiceException {

	private static final long serialVersionUID = -7795003212163378665L;
	private String code;

	public DuplicateEmailException(String code) {
		super();
		this.code = code;
	}

	public DuplicateEmailException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public DuplicateEmailException(String code, String message) {
		super(message);
		this.code = code;
	}

	public DuplicateEmailException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
