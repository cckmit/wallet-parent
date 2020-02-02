package org.wallet.gateway.client.sms;

/**
 * @author zengfucheng
 * @date 2018/4/14.
 */
public class SmsSendResponse {
    /**
     * 响应时间
     */
    private String time;
    /**
     * 消息id
     */
    private String msgId;
    /**
     * 状态码说明（成功返回空）
     */
    private String error;
    /**
     * 状态码（详细参考提交响应状态码）
     */
    private String code;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SmsSingleResponse [time=" + time + ", msgId=" + msgId + ", error=" + error + ", code=" + code
                + "]";
    }
}
