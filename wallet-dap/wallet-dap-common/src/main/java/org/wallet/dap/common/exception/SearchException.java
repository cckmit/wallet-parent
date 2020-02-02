package org.wallet.dap.common.exception;

import org.wallet.dap.common.dubbo.ResponseCode;

/**
 * 绑定参数时 报错
 */
public class SearchException extends BaseException {

	private static final long serialVersionUID = 154877165008205121L;

	public SearchException() {
		super(ResponseCode.ILLEGAL_PARAM);
	}

	public SearchException(String message) {
		super(message);
	}

	public SearchException(String code,String message) {
		super(code,message);
	}

	public SearchException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public SearchException(Throwable throwable) {
		super(throwable);
	}

	
}
