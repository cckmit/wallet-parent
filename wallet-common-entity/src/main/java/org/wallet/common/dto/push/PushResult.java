package org.wallet.common.dto.push;

import lombok.Data;

/**
 * 推送目标
 * @author zengfucheng
 **/
@Data
public class PushResult {
    /** 响应码 */
    private Integer code;
    /** 内容 */
    private String content;
    /** 错误 */
    private Error error;
    /** API每分钟限制次数 */
    private Integer quota;
    /** API剩余调用次数 */
    private Integer remain;
    /** 限制重置时间（秒） */
    private Integer reset;

    @Data
    public static class Error{
        /** 消息ID */
        private Long msgId;
        /** 响应码 */
        private Integer code;
        /** 响应消息 */
        private String msg;
    }
}
