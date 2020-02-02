package org.wallet.dap.mq;

/**
 * @author zengfucheng
 * @date 2018年7月19日
 */
public class MQException extends RuntimeException {

	private static final long serialVersionUID = -7366082795929769275L;

	public MQException() {
		super();
	}

	public MQException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MQException(String message, Throwable cause) {
		super(message, cause);
	}

	public MQException(String message) {
		super(message);
	}

	public MQException(Throwable cause) {
		super(cause);
	}

	
}
