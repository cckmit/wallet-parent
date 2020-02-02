package org.wallet.common.dto.block.res.newdex;

import lombok.Data;

/**
 * @author zengfucheng
 **/
@Data
public class NewdexResultDTO<T> {
    private String code;
    private String msg;
    private T data;

    public boolean success(){
        return "200".equals(code);
    }
}
