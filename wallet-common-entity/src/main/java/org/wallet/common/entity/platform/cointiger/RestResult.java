package org.wallet.common.entity.platform.cointiger;

import java.io.Serializable;

/**
 * CoinTiger Rest请求结果
 * @author zengfucheng
 **/
public class RestResult<T> implements Serializable {
    private static final long serialVersionUID = -4501370697979988704L;
    private String code;
    private String msg;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
