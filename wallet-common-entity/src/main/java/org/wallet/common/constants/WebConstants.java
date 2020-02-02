package org.wallet.common.constants;

/**
 * @author zengfucheng
 */
public interface WebConstants {
	/** 签名验证 - 时间戳参数 */
	String PARAM_TIME = "t_";
	/** 签名验证 - 验证签名Key参数 */
	String PARAM_KEY = "k_";
    /** 签名验证 - 签名参数 */
	String PARAM_SIGN = "s_";
    /** 签名验证 - 替换字符参数 */
	String PARAM_CHAR = "c_";
    /** 签名验证 - 排除签名的字段名 */
	String PARAM_EXCLUDE = "e_";
    /** 数据收集 - 客户端标识 */
    String HEADER_CLIENT_ID = "Client-Id";
    /** 数据收集 - 客户端数据 */
    String HEADER_CLIENT_DATA = "Client-Data";
    /** Token - 授权Token */
	String HEADER_AUTHORIZATION = "Authorization";
    /** Token - Token前缀 */
	String AUTHORIZATION_BEARER = "Bearer ";
    /** Token */
	String PARAM_TOKEN = "token";
    /** 缓存 - App所属Token */
	String CACHE_APP_TOKEN = "app:token";
    /** 缓存 - Admin所属Token */
	String CACHE_ADMIN_TOKEN = "admin:token";
    /** crypto */
    String PARAM_CRYPTO = "crypto";

}
