package org.wallet.common.dto.block;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlockResult<T> implements Serializable {
    private String code;
    private String msg;
    private T result;

    public BlockResult() {
        this.code = BlockResultCode.SUCCESS;
        this.msg = BlockResultCode.OK;
    }

    public BlockResult(String code) {
        this.code = code;
    }

    public BlockResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BlockResult(String code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public BlockResult(T result) {
        this();
        this.result = result;
    }

    public static BlockResult of(String code){
        return new BlockResult(code);
    }

    public static BlockResult of(String code, String msg){
        return new BlockResult(code, msg);
    }

    public static <E> BlockResult<E> of(String code, String msg, E data){
        return new BlockResult<>(code, msg, data);
    }

    public static BlockResult paramInvalid(String msg){
        return BlockResult.of(BlockResultCode.ILLEGAL_PARAM, msg);
    }

    public boolean success(){
        return BlockResultCode.SUCCESS.equalsIgnoreCase(code);
    }
}
