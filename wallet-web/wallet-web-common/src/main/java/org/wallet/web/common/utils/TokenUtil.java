package org.wallet.web.common.utils;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.StringUtils;
import org.wallet.common.constants.WebConstants;
import org.wallet.common.dto.SimpleResult;
import org.wallet.common.dto.SimpleToken;
import org.wallet.common.enums.ResultCode;
import org.wallet.dap.common.bind.Results;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

/**
 * @author zengfucheng
 **/
@Slf4j
public class TokenUtil {
    private static final String SECRET = "z31ZtcBM5eVpRLD7VXVC13ZdwvRUeqtG";
    private static final String ISSUER = "wallet";

    /**
     * 生成token
     *
     * @param claims 声明
     * @param expire 过期时间
     * @return
     */
    public static <T> String createToken(T claims, Duration expire) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        JWTCreator.Builder builder = JWT.create()
                .withIssuer(ISSUER)
                .withExpiresAt(DateUtils.addSeconds(new Date(), (int) expire.getSeconds()));
        builder.withClaim("data", JSON.toJSONString(claims));
        return builder.sign(algorithm);
    }

    /**
     * 验证jwt，并返回数据
     *
     * @param token token
     */
    public static <T> T verifyToken(String token, Class<T> clazz) {
        Algorithm algorithm;
        Map<String, Claim> map;
        algorithm = Algorithm.HMAC256(SECRET);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(token);
        return JSON.parseObject(jwt.getClaim("data").asString(), clazz);
    }

    /**
     * 获取Token
     * @param request 请求
     * @return Token
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader(WebConstants.HEADER_AUTHORIZATION);

        if(StringUtils.isEmpty(token)){
            token = request.getParameter(WebConstants.PARAM_TOKEN);
        }

        if(StringUtils.isEmpty(token)){
            token = CookieUtil.getCookie(request, WebConstants.PARAM_TOKEN);
        }

        if(StringUtils.isEmpty(token)){
            return null;
        }

        if(token.startsWith(WebConstants.AUTHORIZATION_BEARER)){
            token = token.replace(WebConstants.AUTHORIZATION_BEARER, "");
        }

        return token;
    }

    /**
     * 校验Token
     * @param token token
     * @return 结果
     */
    public static SimpleResult<SimpleToken> verifyToken(String token){
        try {
            SimpleToken simpleToken = TokenUtil.verifyToken(token, SimpleToken.class);
            simpleToken.setCryptoKey(null);
            simpleToken.setAesIv(null);
            return Results.success(simpleToken);
        } catch (TokenExpiredException e) {
            return Results.byCode(ResultCode.TokenExpired);
        } catch (JWTVerificationException e){
            log.warn("无效Token[{}]:{}", token, e.getMessage());
            return Results.byCode(ResultCode.TokenInvalid);
        } catch (Exception e){
            log.warn("校验Token[{}]失败:{}", token, e.getMessage());
            return Results.byCode(ResultCode.MissingToken);
        }
    }
}
