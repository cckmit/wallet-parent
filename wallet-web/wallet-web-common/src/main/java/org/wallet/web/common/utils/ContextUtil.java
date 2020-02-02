package org.wallet.web.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.wallet.common.dto.SimpleUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zengfucheng
 **/
@Slf4j
public class ContextUtil {
    /**
     * 从请求中获取用户信息
     * @param request 请求
     * @return 用户信息
     */
    public static SimpleUser getUser(HttpServletRequest request){
        return new SimpleUser();
    }

    /**
     * 获取用户ID
     * @param request 请求
     * @return 用户 ID
     */
    public static String getUserId(HttpServletRequest request) {
        SimpleUser user = getUser(request);
        if(null != user && null != user.getId()){
            return user.getId().toString();
        }
        return null;
    }

    /**
     * 获取请求路径
     * @param request 请求
     * @return 用户 ID
     */
    public static String getPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    /**
     * 根据请求类型发送结果信息
     * @param result 结果
     * @param response response
     */
    public static <T> void sendResult(T result, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter()) {
            out.append(JSON.toJSONString(result));
        } catch (IOException e) {
            log.error("发送结果信息异常：" + e.getMessage(), e);
        }
    }
}
