package org.wallet.dap.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 无此数据
 */
@Getter
@Setter
public class DataNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private String message;

	public DataNotFoundException() {
	}

	public DataNotFoundException(String code, String message) {
		super();
		this.message = message;
	}

	public DataNotFoundException(String message) {
		super(message);
		this.message = message;
	}

	public DataNotFoundException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
		this.message = message;
	}
	public DataNotFoundException(String message, Throwable throwable) {
		super(message, throwable);
		this.message = message;
	}

	public DataNotFoundException(Throwable throwable) {
		super(throwable);
	}

}
