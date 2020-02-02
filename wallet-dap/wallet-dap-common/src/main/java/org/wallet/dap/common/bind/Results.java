package org.wallet.dap.common.bind;

import org.wallet.common.dto.SimpleResult;
import org.wallet.common.enums.ResultCode;
import org.wallet.dap.common.dubbo.ServiceResponse;

/**
 * @author zengfucheng
 **/
public class Results {
    public static <T> SimpleResult<T> by(ServiceResponse response){
        if(null == response){ return success(); }
        if(response.success()){
            return success(response.getResult());
        }else{
            return fail(response.getRespMsg());
        }
    }

    public static <T> SimpleResult<T> success(){
        return new SimpleResult<>();
    }

    public static <T> SimpleResult<T> success(T t){
        return new SimpleResult<>(t);
    }

    public static <T> SimpleResult<T> of(String code, String message) {
        return new SimpleResult<>(code, message);
    }

    public static <T> SimpleResult<T> fail(String msg){
        return new SimpleResult<>(ResultCode.BusinessFail.getCode(), msg);
    }

    public static <T> SimpleResult<T> fatal(String msg){
        return new SimpleResult<>(ResultCode.ServiceFatal.getCode(), msg);
    }

    public static <T> SimpleResult<T> paramInvalid(String msg) {
        return new SimpleResult<>(ResultCode.ParamInvalid.getCode(), msg);
    }

    public static <T> SimpleResult<T> byCode(ResultCode resultCode){
        return new SimpleResult<>(resultCode.getCode(), resultCode.getMessage());
    }
}
