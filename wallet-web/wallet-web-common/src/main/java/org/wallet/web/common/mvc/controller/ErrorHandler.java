package org.wallet.web.common.mvc.controller;

import javax.servlet.http.HttpServletRequest;

public interface ErrorHandler {

    /**
     * 处理异常
     * @param request 请求
     * @param e 异常
     */
    void handleException(HttpServletRequest request, Throwable e);
}
