package org.jdvn.lis.valuation.exception;

public class DuplicateLoginIdException extends ServiceException {

	private static final long serialVersionUID = -2649423769671399907L;
	private String code;

	public DuplicateLoginIdException(String code) {
		super();
		this.code = code;
	}

	public DuplicateLoginIdException(String code, Throwable cause) {
		super(cause);
		this.code = code;
	}

	public DuplicateLoginIdException(String code, String message) {
		super(message);
		this.code = code;
	}

	public DuplicateLoginIdException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
