package org.wallet.service.common.exception;

import org.wallet.dap.common.exception.BaseException;

/**
 * 如果在service层 需要抛出异常 使用这个异常
 */
public class ServiceException extends BaseException {

	private static final long serialVersionUID = 1760277946273198000L;

	public ServiceException() {
	}

	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String code,String message) {
		super(code,message);
	}

	public ServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ServiceException(Throwable throwable) {
		super(throwable);
	}
	
}
