package org.wallet.dap.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 异常基类
 */
@Getter
@Setter
public class BaseException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private String message;

	public BaseException() {
	}

	public BaseException(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	
	public BaseException(String message) {
		super(message);
		this.message = message;
	}
	
	public BaseException(String code, String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
		super(message, throwable, enableSuppression, writableStackTrace);
		this.code = code;
		this.message = message;
	}
	public BaseException(String message, Throwable throwable) {
		super(message, throwable);
		this.message = message;
	}

	public BaseException(Throwable throwable) {
		super(throwable);
	}

}
