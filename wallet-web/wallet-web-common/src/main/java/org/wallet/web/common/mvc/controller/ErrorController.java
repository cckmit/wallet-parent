package org.wallet.web.common.mvc.controller;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.common.dto.SimpleResult;
import org.wallet.dap.common.bind.Results;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@RestController
public class ErrorController extends BasicErrorController {

    public ErrorController() {
        super(new DefaultErrorAttributes(true), new ErrorProperties());
    }

    @Override
    @RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, true);
        HttpStatus status = getStatus(request);
        SimpleResult result = Results.fail((String) body.get("message"));
        return new ResponseEntity<>(BeanUtil.beanToMap(result), status);
    }

    @Override
    public String getErrorPath() {
        return "error/error";
    }

}
