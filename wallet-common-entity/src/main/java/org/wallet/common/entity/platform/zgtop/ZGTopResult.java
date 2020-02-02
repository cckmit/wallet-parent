package org.wallet.common.entity.platform.zgtop;

import java.io.Serializable;

/**
 * ZG.TOP 数据
 * @author zengfucheng
 **/
public class ZGTopResult<T> implements Serializable {
    private static final long serialVersionUID = -1851026727794591485L;
    private String code;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
