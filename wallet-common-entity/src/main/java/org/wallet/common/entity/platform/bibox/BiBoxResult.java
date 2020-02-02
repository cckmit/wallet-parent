package org.wallet.common.entity.platform.bibox;

import java.io.Serializable;

/**
 * @author zengfucheng
 **/
public class BiBoxResult<T> implements Serializable {
    private static final long serialVersionUID = -1145710416206893895L;
    private T result;
    private String cmd;
    private String ver;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }
}
