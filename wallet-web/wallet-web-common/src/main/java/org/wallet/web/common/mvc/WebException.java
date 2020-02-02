package org.wallet.web.common.mvc;

import lombok.Getter;
import lombok.Setter;
import org.wallet.common.enums.ResultCode;
import org.wallet.dap.common.exception.BaseException;

/**
 * @author zengfucheng
 **/
@Getter
@Setter
public class WebException extends BaseException {
    private ResultCode resultCode;

    public WebException(ResultCode resultCode){
        super();
        this.resultCode = resultCode;
        this.setMessage(resultCode.getMessage());
    }

    public WebException(ResultCode resultCode, Throwable e){
        super(e.getMessage(), e);
        this.resultCode = resultCode;
    }
}
