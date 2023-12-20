package org.jdvn.lis.valuation.exception;

public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 6655277462302818544L;

	public ServiceException() {
		super();
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
