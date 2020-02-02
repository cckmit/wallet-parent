package org.wallet.common.dto;

import lombok.Data;
import org.wallet.common.enums.ResultCode;

@Data
public class SimpleResult<T>{
    private String code;
    private String msg;
    private T data;

    public SimpleResult() {
        code = ResultCode.Success.getCode();
        msg = ResultCode.Success.getMessage();
    }

    public SimpleResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public SimpleResult(T data) {
        this();
        this.data = data;
    }

    public boolean getSuccess(){
        return ResultCode.Success.getCode().equalsIgnoreCase(code);
    }
}
