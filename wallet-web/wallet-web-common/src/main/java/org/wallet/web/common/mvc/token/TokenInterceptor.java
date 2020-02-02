package org.wallet.web.common.mvc.token;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.wallet.common.constants.WebConstants;
import org.wallet.common.dto.SimpleToken;
import org.wallet.common.enums.ResultCode;
import org.wallet.dap.cache.Cache;
import org.wallet.dap.common.crypto.Crypto;
import org.wallet.dap.common.crypto.symmetric.AES;
import org.wallet.dap.common.crypto.symmetric.Mode;
import org.wallet.dap.common.crypto.symmetric.Padding;
import org.wallet.dap.common.utils.ThreadLocalUtil;
import org.wallet.web.common.utils.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Token 拦截
 * @author zengfucheng
 **/
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    Cache cache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws MissingRequestHeaderException {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        if (handlerMethod.getBeanType().isAnnotationPresent(PassToken.class)) {
            return true;
        }

        Method method = handlerMethod.getMethod();
        //检查是否有PassToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        String token = TokenUtil.getToken(request);

        // 执行认证
        if (StringUtils.isEmpty(token)) {
            throw new TokenException(ResultCode.MissingToken);
        }

        try {
            SimpleToken simpleToken = TokenUtil.verifyToken(token, SimpleToken.class);

            String clientId = request.getHeader(WebConstants.HEADER_CLIENT_ID);
            if(StringUtils.isEmpty(clientId)){
                MethodParameter methodParameter = new MethodParameter(method, 0);
                throw new MissingRequestHeaderException(WebConstants.HEADER_CLIENT_ID, methodParameter);
            }

            if(!clientId.equals(simpleToken.getClientId())){
                throw new TokenException(ResultCode.TokenInvalid);
            }

            SimpleToken cacheToken = cache.get(WebConstants.CACHE_APP_TOKEN, clientId, SimpleToken.class);

            if(null == cacheToken){
                throw new TokenException(ResultCode.TokenExpired);
            }

            if(!token.equals(cacheToken.getToken())){
                throw new TokenException(ResultCode.TokenInvalid);
            } else if(!clientId.equals(cacheToken.getClientId())){
                throw new TokenException(ResultCode.TokenInvalid);
            }

            Crypto crypto = new AES(Mode.CBC, Padding.PKCS5Padding, simpleToken.getAesIv().getBytes(), simpleToken.getCryptoKey().getBytes());

            ThreadLocalUtil.set(WebConstants.PARAM_CRYPTO, crypto);
            return true;
        } catch (TokenExpiredException e) {
            throw new TokenException(ResultCode.TokenExpired);
        } catch (JWTVerificationException e){
            log.warn("无效Token[{}]:{}", token, e.getMessage());
            throw new TokenException(ResultCode.TokenInvalid);
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
