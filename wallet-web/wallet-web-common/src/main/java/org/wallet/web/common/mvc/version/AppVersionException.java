package org.wallet.web.common.mvc.version;

import org.wallet.common.enums.ResultCode;

/**
 * 接口版本异常
 * @author zengfucheng
 */
public class AppVersionException extends RuntimeException {

	private static final long serialVersionUID = 6889362984267239541L;

	private String code;
	private MatchModelDTO matchModel;
	private Integer appVersion;

	public AppVersionException() {
		super();
	}

	public AppVersionException(String message, Throwable cause,
                               boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AppVersionException(String message, Throwable cause) {
		super(message, cause);
	}

	public AppVersionException(String message) {
		super(message);
	}

	public AppVersionException(Throwable cause) {
		super(cause);
	}

	public AppVersionException(String code, String message, MatchModelDTO matchModel, Integer appVersion) {
		super(message);
		this.code = code;
		this.matchModel = matchModel;
		this.appVersion = appVersion;
	}

	public AppVersionException(ResultCode resultCode, MatchModelDTO matchModel, Integer appVersion) {
		super(resultCode.getMessage());
		this.code = resultCode.getCode();
		this.matchModel = matchModel;
		this.appVersion = appVersion;
	}

	public String getCode() {
		return code;
	}

	public MatchModelDTO getMatchModel() {
		return matchModel;
	}

	public Integer getAppVersion() {
		return appVersion;
	}
}
