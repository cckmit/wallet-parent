package org.wallet.common.dto.block.res.whaleex;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author zengfucheng
 **/
@Data
public class WhaleExResultDTO {

    private String returnCode;
    private String message;
    private String errorCode;
    private Object result;

    public boolean success(){
        return StringUtils.isEmpty(returnCode) && StringUtils.isEmpty(errorCode);
    }
}
