package org.wallet.web.common.mvc.controller;

import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSONException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.enums.ResultCode;
import org.wallet.dap.common.bind.Results;
import org.wallet.dap.common.crypto.CryptoException;
import org.wallet.dap.common.exception.DataNotFoundException;
import org.wallet.web.common.mvc.WebException;
import org.wallet.web.common.mvc.token.TokenException;
import org.wallet.web.common.mvc.version.AppVersionException;
import org.wallet.web.common.utils.ContextUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 * @author zengfucheng
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler{
    @Autowired(required = false)
    private ErrorHandler errorHandler;

    /**
     * App版本
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(AppVersionException.class)
	public SimpleResult appVersion(HttpServletRequest request, AppVersionException e) {
        return Results.of(e.getCode(), e.getMessage());
	}

    /**
     * 缺少参数
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public SimpleResult missingParameter(HttpServletRequest request, MissingServletRequestParameterException e){
        String msg = String.format("缺少参数[%s]", e.getParameterName());
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        SimpleResult result = Results.byCode(ResultCode.MissingParameter);
        result.setMsg(msg);
        return result;
    }

    /**
     * 参数格式错误
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public SimpleResult covertParameter(HttpServletRequest request, MethodArgumentTypeMismatchException e){
        String msg = String.format("参数[%s]需要类型[%s]", e.getParameter().getParameterName(), e.getRequiredType().getName());
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        SimpleResult result = Results.byCode(ResultCode.ParamInvalid);
        result.setMsg(msg);
        return result;
    }

    /**
     * 缺少Header参数
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MissingRequestHeaderException.class)
    public SimpleResult missingHeader(HttpServletRequest request, MissingRequestHeaderException e){
        String msg = String.format("缺少Header参数[%s]", e.getHeaderName());
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        SimpleResult result = Results.byCode(ResultCode.MissingParameter);
        result.setMsg(msg);
        return result;
    }

    /**
     * 缺少Header参数
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentConversionNotSupportedException.class)
    public SimpleResult covertHeader(HttpServletRequest request, MethodArgumentConversionNotSupportedException e){
        String msg = String.format("Header参数[%s]需要类型[%s]", e.getParameter().getParameterName(), e.getRequiredType().getName());
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        SimpleResult result = Results.byCode(ResultCode.MissingParameter);
        result.setMsg(msg);
        return result;
    }

    /**
     * 缺少方法体
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public SimpleResult messageNotReadable(HttpServletRequest request, HttpMessageNotReadableException e) {
        String msg = ResultCode.MessageNotReadable.getMessage();
        SimpleResult result;
        if(e.getCause() instanceof JSONException){
            msg = String.format("%s：%s", ResultCode.JsonFormatInvalid.getMessage(), e.getCause().getMessage());
            result = Results.byCode(ResultCode.JsonFormatInvalid);
        }else{
            result = Results.byCode(ResultCode.MessageNotReadable);
        }
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        result.setMsg(msg);
        return result;
    }

    /**
     * 参数验证失败
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public SimpleResult handleMethodArgumentNotValid(HttpServletRequest request, MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg;
        if(!StringUtils.isEmpty(fieldError.getRejectedValue())){
            msg = String.format("[%s:%s]%s", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
        }else{
            msg = String.format("[%s]%s", fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        return Results.paramInvalid(msg);
    }

    /**
     * 参数验证失败
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public SimpleResult bindError(HttpServletRequest request, BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String msg = String.format("[%s]%s", fieldError.getField(), fieldError.getDefaultMessage());
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        return Results.paramInvalid(msg);
    }

    /**
     * 不支持此请求方式
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public SimpleResult methodNotSupported(HttpServletRequest request, HttpRequestMethodNotSupportedException e){
        String msg = String.format("不支持该请求方式[%s]，仅支持[%s]", e.getMethod(), String.join("|", e.getSupportedMethods()));
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        SimpleResult result = Results.byCode(ResultCode.RequestMethodNotSupported);
        result.setMsg(msg);
        return result;
    }

    /**
     * 不支持上传请求
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public SimpleResult mediaTypeNotSupported(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        List<String> mediaTypeList = e.getSupportedMediaTypes().stream().map(MimeType::toString).collect(Collectors.toList());
        String msg = String.format("不支持该Content-Type[%s]，仅支持[%s]", e.getContentType(), String.join("|", mediaTypeList));
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        SimpleResult result = Results.byCode(ResultCode.RequestMethodNotSupported);
        result.setMsg(msg);
        return result;
    }

    /**
     * Token异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = TokenException.class)
    public SimpleResult tokenException(HttpServletRequest request, TokenException e){
        return Results.byCode(e.getResultCode());
    }

    /**
     * 加解密异常
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = CryptoException.class)
    public SimpleResult tokenException(HttpServletRequest request, CryptoException e){
        String msg = String.format("解密失败：%s", e.getMessage());
        log.warn("[{}]{}", ContextUtil.getPath(request), msg);
        return Results.byCode(ResultCode.CryptoFail);
    }
    
	/**
	 * 无此接口
	 */
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(value = NoHandlerFoundException.class)
	@ResponseBody
	public SimpleResult interfaceNotFound(HttpServletRequest request, Exception e){
		return Results.byCode(ResultCode.InterfaceNotFound);
	}

	/**
	 * 无此数据
	 */
	@ResponseStatus(HttpStatus.GONE)
	@ExceptionHandler(value = DataNotFoundException.class)
	@ResponseBody
	public SimpleResult dataNotFound(HttpServletRequest request, Exception e){
		return Results.byCode(ResultCode.InformationNotFound);
	}

    /**
     * Dubbo 异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RpcException.class)
    public SimpleResult rpcException(HttpServletRequest request, RpcException e){
        Throwable t = e.getCause();
        if(t instanceof TimeoutException){
            return Results.byCode(ResultCode.ServiceTimeout);
        }
        log.error("Dubbo异常：" + e.getMessage(), e);
        return Results.fatal(e.getMessage());
    }

    /**
     * Web 异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = WebException.class)
    public SimpleResult webException(HttpServletRequest request, WebException e){
        log.error("Web异常：" + e.getMessage(), e);
        if(null != errorHandler){
            errorHandler.handleException(request, e);
        }
        return Results.byCode(e.getResultCode());
    }

    /**
     * 服务器异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public SimpleResult exception(HttpServletRequest request, Exception e){
        log.error("应用异常：" + e.getMessage(), e);
        if(null != errorHandler){
            errorHandler.handleException(request, e);
        }
        return Results.fatal(e.getMessage());
    }
}
