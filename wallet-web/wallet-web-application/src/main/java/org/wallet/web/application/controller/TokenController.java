package org.wallet.web.application.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wallet.common.constants.WebConstants;
import org.wallet.common.dto.ClientData;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SimpleToken;
import org.wallet.common.enums.ResultCode;
import org.wallet.dap.common.bind.Results;
import org.wallet.web.application.service.TokenService;
import org.wallet.web.common.mvc.controller.BaseController;
import org.wallet.web.common.mvc.token.PassToken;

/**
 * @author zengfucheng
 **/
@Slf4j
@RestController
@RequestMapping("token")
public class TokenController extends BaseController {

    @Autowired
    TokenService tokenService;

    /** 获取Token */
    @GetMapping
    @PassToken
    public SimpleResult<SimpleToken> get(@RequestHeader(WebConstants.HEADER_CLIENT_DATA) String clientDataString, ClientData clientData){
        ClientData headerClientData = JSON.parseObject(clientDataString, ClientData.class);

        BeanUtil.copyProperties(headerClientData, clientData, CopyOptions.create().setIgnoreNullValue(true));

        if(StringUtils.isEmpty(clientData.getClientId())){
            return Results.byCode(ResultCode.MissingParameter);
        }

        return Results.success(tokenService.createToken(clientData));
    }
}
