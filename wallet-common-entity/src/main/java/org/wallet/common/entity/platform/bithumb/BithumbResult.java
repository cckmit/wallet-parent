package org.wallet.common.entity.platform.bithumb;

import java.io.Serializable;

/**
 * Bithumb 数据
 * @author zengfucheng
 **/
public class BithumbResult<T> implements Serializable {
    private static final long serialVersionUID = -1851026727794591485L;
    private String code;
    private String msg;
    private Long timestamp;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
