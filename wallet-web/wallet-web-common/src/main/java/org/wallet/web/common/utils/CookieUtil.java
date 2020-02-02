package org.wallet.web.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Cookie工具类
 *
 * @author zengfucheng
 */
@Slf4j
public class CookieUtil {
    private static final int DEFAULT_COOKIE_TIME = 1000 * 60 * 5;
    private static final int DEFAULT_OBJECT_COOKIE_TIME = 1000 * 60 * 10;
    public static CookieUtil instance;
    public static CookieUtil getInstance() {
        if (instance == null) {
            instance = new CookieUtil();
        }
        return instance;
    }

    /**
     * 保存值到cookie
     *
     * @param response response
     * @param key key
     * @param value value
     */
    public static void writeCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(DEFAULT_COOKIE_TIME);
        cookie.setPath("/");
        log.info("设置cookie[" + key + "],value[" + value + "],maxAge[" + DEFAULT_COOKIE_TIME + "]");
        response.addCookie(cookie);
    }

    /**
     * 从cookie获取值
     *
     * @param request request
     * @param key key
     */
    public static synchronized String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equalsIgnoreCase(key)) {
                    log.info("获取cookie[" + key + "],value[" + cookies[i].getValue() + "]");
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    /**
     * 清除cookie里面数据
     *
     * @param request request
     * @param response response
     * @param key key
     */
    public static void clearCookie(HttpServletRequest request, HttpServletResponse response, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase(key)) {
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    log.info("清除cookie[" + key + "]");
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * 保存对象到cookie
     *
     * @param response response
     * @param key key
     * @param value value
     */
    public static <T> void writeObjectToCookie(HttpServletResponse response, String key, T value) {
        if(null == value){
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(byteArrayOutputStream);
            oos.writeObject(value);
            String cookieValue = byteArrayOutputStream.toString("ISO-8859-1");
            String encodedCookieValue = URLEncoder.encode(cookieValue, "UTF-8");
            Cookie cookie = new Cookie(key, encodedCookieValue);
            cookie.setMaxAge(DEFAULT_OBJECT_COOKIE_TIME);
            cookie.setPath("/");
            log.info("设置cookie[" + key + "],value[" + JSON.toJSONString(value) + "]");
            response.addCookie(cookie);
        } catch (IOException e) {
            log.error("设置cookie[" + key + "],value[" + JSON.toJSONString(value) + "]失败：" + e.getMessage(), e);
        }
    }

    /**
     * 从Cookie中获取对象
     *
     * @param request request
     * @param key key
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObjectFromCookie(HttpServletRequest request, String key, Class<T> clazz) {
        String encodedCookieValue = getCookie(request, key);
        if (null != encodedCookieValue && !"".equals(encodedCookieValue)) {
            T object = null;
            try {
                String decoderCookieValue = java.net.URLDecoder.decode(encodedCookieValue, "UTF-8");
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decoderCookieValue.getBytes("ISO-8859-1"));
                ObjectInputStream iso = new ObjectInputStream(byteArrayInputStream);
                object = (T) iso.readObject();
                log.info("获取cookie[" + key + "],value[" + JSON.toJSONString(object) + "]");
                return object;
            } catch (Exception e) {
                log.error("获取cookie[" + key + "],value[" + JSON.toJSONString(object) + "]失败：" + e.getMessage(), e);
                return null;
            }
        } else {
            return null;
        }
    }
}
  