package org.wallet.web.admin.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.enums.ResultCode;
import org.wallet.dap.common.bind.Results;
import org.wallet.web.admin.utils.UserUtil;
import org.wallet.web.common.utils.ContextUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Shiro异常处理
 * @author zengfucheng
 */
@Slf4j
@RestControllerAdvice
public class ShiroExceptionHandler {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public SimpleResult unauthorized(HttpServletRequest request, UnauthorizedException e) {
        log.warn("[{}]访问非授权接口[{}]", UserUtil.getUser().getUsername(), ContextUtil.getPath(request));
        return Results.byCode(ResultCode.AdminUnauthorized);
    }
}
