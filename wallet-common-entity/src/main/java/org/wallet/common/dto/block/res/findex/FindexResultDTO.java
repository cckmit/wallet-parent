package org.wallet.common.dto.block.res.findex;

import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class FindexResultDTO<T> {
    private String code;
    private String msg;
    private T data;

    public boolean success(){
        return "200".equals(code);
    }
}
