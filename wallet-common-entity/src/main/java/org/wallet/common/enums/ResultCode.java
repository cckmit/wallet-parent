package org.wallet.common.enums;


/**
 * 响应码
 * @author zengfucheng
 **/
public enum ResultCode {

    /**
     * 成功
     */
    Success("00000", "ok"),

    /**
     * -------------------------- 请求无效
     */
    RequestMethodNotSupported("40000", "不支持此请求方式"),
    MessageNotReadable("40001", "缺少请求体"),
    JsonFormatInvalid("40002", "JSON 格式不正确"),
    MissingParameter("40003", "缺少必要参数"),
    ParamInvalid("40004", "请求参数不正确"),
    ListTooLong("40005", "列表长度超限"),
    RequestRepeatExecution("40010", "请勿重复提交请求"),

    /**
     * App 版本
     */
    AppVersionLow("40080", "App版本过低"),
    AppVersionNotEqual("40081", "App版本不正确"),
    AppVersionHigh("40082", "App版本过高"),

    MissingToken("40100", "Token为空"),
    TokenInvalid("40101", "Token无效"),
    TokenExpired("40102", "Token过期"),

    CryptoFail("40110", "解密请求内容失败"),

    /**
     * -------------------------- 404
     */
    InterfaceNotFound("40400", "无此接口"),
    InformationNotFound("40401", "未找到相关信息"),

    ServiceFatal("50000", "服务繁忙，请稍后再试"),
    ServiceException("50100", "服务异常，请稍后再试"),
    ServiceTimeout("50200", "服务超时，请稍后再试"),

    /**
     * -------------------------- 后台业务
     */
    AdminBusinessFail("60000", "后台业务异常，请稍后再试"),
    AdminNeedLogin("60100", "请登录后再访问该请求"),
    AdminUsernameInvalid("60101", "无此用户"),
    AdminPasswordInvalid("60102", "密码错误"),
    AdminLockedAccount("60103", "账户已冻结"),
    AdminUnauthorized("60104", "无权访问"),

    /**
     * -------------------------- 业务
     */
    BusinessFail("70000", "业务异常，请稍后再试"),

    ServiceUnavailable("99999", "服务宕机");

    private String code;
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
