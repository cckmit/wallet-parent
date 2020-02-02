package org.wallet.common.entity.platform.bitz;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * @author zengfucheng
 **/
public class BitZResult<T> implements Serializable {
    private static final long serialVersionUID = 5624719974992146804L;
    private Integer status;
    private String msg;
    private T data;
    private Long time;
    @JSONField(name = "microtime")
    private String microTime;
    private String source;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMicroTime() {
        return microTime;
    }

    public void setMicroTime(String microTime) {
        this.microTime = microTime;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
