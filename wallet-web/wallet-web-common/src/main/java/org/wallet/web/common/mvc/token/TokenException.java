package org.wallet.web.common.mvc.token;

import org.wallet.common.enums.ResultCode;
import org.wallet.web.common.mvc.WebException;

/**
 * @author zengfucheng
 **/
public class TokenException extends WebException {
    public TokenException(ResultCode resultCode) {
        super(resultCode);
    }
}
