package org.wallet.gateway.client.sms;

/**
 * @author zengfucheng
 * @date 2019/01/20
 */
public class VoiceSmsSendRequest {
    /**
     * 用户账号，必填
     */
    private String account;
    /**
     * 用户密码，必填
     */
    private String password;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 验证码内容
     */
    private String code;
    /**
     * 验证码简介(在验证码之前读)必填
     */
    private String intro;
    /**
     * 结尾(在验证码之后读)
     */
    private String outro;

    public VoiceSmsSendRequest() {
        this.intro = "您的验证码是";
    }

    public VoiceSmsSendRequest(String account, String password, String mobile, String code) {
        this();
        this.account = account;
        this.password = password;
        this.mobile = mobile;
        this.code = code;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getOutro() {
        return outro;
    }

    public void setOutro(String outro) {
        this.outro = outro;
    }
}
