package org.wallet.gateway.client.utils.httpclient;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.wallet.dap.common.utils.EnvironmentUtil;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HttpClient 连接池
 * @author zengfucheng
 */
public class HttpConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(HttpConnectionPool.class);
    private static final ThreadLocal<String> UUID_LOCAL = new ThreadLocal<>();

    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";

    /**
     * 默认客户端总并行链接最大数100
     */
    private static String DEFAULT_MAX_TOTAL = "100";
    /**
     * 默认请求超时时间30秒
     */
    private static String DEFAULT_TIMEOUT_REQUEST = "30000";
    /**
     * 默认等待数据超时时间30秒
     */
    private static String DEFAULT_TIMEOUT_READ = "30000";
    /**
     * 默认清理过期及多余空闲连接线程执行周期 默认30分钟
     */
    private static String DEFAULT_CLEAN_CONNECTION_PERIOD = "300000";
    /**
     * 连接池默认键
     */
    private static String DEFAULT_POOL_KEY = "DCP";
    /**
     * 客户端总并行链接最大数 配置后缀
     */
    private static String KEY_MAX_TOTAL = ".connPoolMaxTotal";
    /**
     * 请求超时时间 配置后缀
     */
    private static String KEY_TIMEOUT_REQUEST = ".connPoolRequestTimeout";
    /**
     * 等待数据超时时间 配置后缀
     */
    private static String KEY_TIMEOUT_READ = ".connPoolReadTimeout";
    /**
     * 清理过期及多余空闲连接线程执行周期 配置后缀
     */
    private static String KEY_CLEAN_CONNECTION_PERIOD = ".connPoolCleanConnPeriod";
    /**
     * 代理IP
     */
    private static String KEY_PROXY_HOST = ".proxyHost";
    /**
     * 代理端口
     */
    private static String KEY_PROXY_PORT = ".proxyPort";

    private static Map<String, CloseableHttpClient> poolMap = new HashMap<>();

    private static ThreadLocal<Charset> charsetLocal = new ThreadLocal<>();

    /**
     * 根据连接池键初始化连接池
     * @param poolKey 连接池键
     * @return HttpClient实体
     */
    private static CloseableHttpClient init(String poolKey){
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Integer.parseInt(getParamOrDefault(poolKey + KEY_TIMEOUT_REQUEST, DEFAULT_TIMEOUT_REQUEST)))
                .setConnectTimeout(Integer.parseInt(getParamOrDefault(poolKey + KEY_TIMEOUT_REQUEST, DEFAULT_TIMEOUT_REQUEST)))
                .setSocketTimeout(Integer.parseInt(getParamOrDefault(poolKey + KEY_TIMEOUT_REQUEST, DEFAULT_TIMEOUT_REQUEST)))
                .setRedirectsEnabled(true)
                .build();

        Registry<ConnectionSocketFactory> registry = null;
        try {
            registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", createSSLConnSocketFactory())
                    .build();
        } catch (Exception ignored) {}

        assert registry != null;
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(Integer.parseInt(getParamOrDefault(poolKey + KEY_MAX_TOTAL, DEFAULT_MAX_TOTAL)));
        cm.setDefaultMaxPerRoute(cm.getMaxTotal());

        String proxyHost = getParamOrDefault(poolKey + KEY_PROXY_HOST, null);
        String proxyPort = getParamOrDefault(poolKey + KEY_PROXY_PORT, null);

        HttpClientBuilder builder = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(cm)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));

        if(!StringUtils.isEmpty(proxyHost)
                && !StringUtils.isEmpty(proxyPort)){
            builder.setProxy(new HttpHost(proxyHost, Integer.parseInt(proxyPort)));
        }

        CloseableHttpClient httpClient = builder.build();

        String threadName = poolKey + "-Connection-Cleaner";

        // 开启定时线程清理HttpClient空闲连接
        ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1, new DaemonThreadFactory(threadName));

        int period = Integer.parseInt(getParamOrDefault(poolKey + KEY_CLEAN_CONNECTION_PERIOD, DEFAULT_CLEAN_CONNECTION_PERIOD));

        int readTime = Integer.parseInt(getParamOrDefault(poolKey + KEY_TIMEOUT_READ, DEFAULT_TIMEOUT_READ));

        scheduler.scheduleAtFixedRate(new IdleConnectionMonitor(cm, readTime), 0, period, TimeUnit.MILLISECONDS);

        poolMap.put(poolKey, httpClient);

        return httpClient;
    }

    /**
     * 创建SSL连接
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() throws NoSuchAlgorithmException, KeyManagementException {
        // 创建TrustManager
        X509TrustManager xtm = new X509TrustManager(){
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            @Override
            public X509Certificate[] getAcceptedIssuers() { return null; }
        };
        // TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
        SSLContext ctx = SSLContext.getInstance("TLS");
        // 使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
        ctx.init(null, new TrustManager[]{xtm}, null);
        return new SSLConnectionSocketFactory(ctx);
    }

    /**
     * 根据name获取配置，如无配置，则返回默认值
     * @param name 配置名称
     * @param defaultValue 如无此配置，返回该值
     * @return 配置值
     */
    private static String getParamOrDefault(String name, String defaultValue) {
        String value = EnvironmentUtil.getProperty(name);
        if(!StringUtils.isEmpty(value)){
            return value;
        }
        return defaultValue;
    }

    private static void setUUID(){
        UUID_LOCAL.set(UUID.randomUUID().toString().replace("-", "").toUpperCase().substring(0,10));
    }

    private static String getUUID(){
        String uuid = UUID_LOCAL.get();
        if(StringUtils.isEmpty(uuid)){
            setUUID();
        }
        return UUID_LOCAL.get();
    }

    private static void clearUUID(){
        UUID_LOCAL.remove();
    }

    /**
     * 设置默认字符集
     * @param charset 字符集
     */
    public static void setCharset(Charset charset){
        charsetLocal.set(charset);
    }

    /**
     * 获取默认字符集
     */
    private static Charset getCharset(){
        Charset charset = charsetLocal.get();
        if(null == charset){
            charset = Charset.forName("UTF-8");
            setCharset(charset);
        }
        return charset;
    }

    private static void clearCharset(){
        charsetLocal.remove();
    }

    /**
     * 获取默认连接池中的连接
     * @return HttpClient实体
     */
    public static CloseableHttpClient getHttpClient() {
        return getHttpClient(DEFAULT_POOL_KEY);
    }

    /**
     * 获取指定KEY的连接池连接
     * @param key 连接池键
     * @return HttpClient实体
     */
    public static CloseableHttpClient getHttpClient(String key) {
        if(null == poolMap.get(key)){
            return init(key);
        }
        return poolMap.get(key);
    }

    public static String get(String path) throws IOException {
        return get(DEFAULT_POOL_KEY, path, StringHttpResponseCovert.getDefault(), null,null);
    }

    public static String get(String path, HttpPoint point) throws IOException {
        return get(DEFAULT_POOL_KEY, path, StringHttpResponseCovert.getDefault(), null, point);
    }

    public static <T> T get(String path, Class<T> clazz) throws IOException {
        return get(DEFAULT_POOL_KEY, path, null, clazz,null);
    }

    public static <T> T get(String path, Class<T> clazz, HttpResponseCovert<T> covert) throws IOException {
        return get(DEFAULT_POOL_KEY, path, covert, clazz,null);
    }

    public static <T> T get(String path, Class<T> clazz, HttpPoint point) throws IOException {
        return get(DEFAULT_POOL_KEY, path, null, clazz, point);
    }

    public static <T> T get(String poolKey, String path, Class<T> clazz) throws IOException {
        return get(poolKey, path, null, clazz,null);
    }

    public static <T> T get(String poolKey, String path, Class<T> clazz, HttpPoint point) throws IOException {
        return get(poolKey, path, null, clazz, point);
    }

    /**
     * 通过指定连接池的连接执行GET请求
     * @param poolKey 连接池键
     * @param path 请求地址，带参数
     * @param covert 响应转换器
     * @param clazz 返回JSON模板
     * @param <T> 类模版
     * @return 实体
     * @throws IOException 抛出请求过程中的IO异常
     */
    public static <T> T get(String poolKey, String path, HttpResponseCovert<T> covert, Class<T> clazz, HttpPoint point) throws IOException {
        return execute(poolKey, new HttpGet(path), covert, clazz, point);
    }

    public static String postJSON(String path, String jsonBody) throws IOException {
        StringEntity jsonEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);

        logReq(DEFAULT_POOL_KEY, path, HTTP_METHOD_POST, jsonBody);

        return post(DEFAULT_POOL_KEY, path, jsonEntity, null, String.class, null);
    }

    public static <T> T postJSON(String path, String jsonBody, Class<T> clazz) throws IOException {
        StringEntity jsonEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);

        logReq(DEFAULT_POOL_KEY, path, HTTP_METHOD_POST, jsonBody);

        return post(DEFAULT_POOL_KEY, path, jsonEntity, null, clazz, null);
    }

    public static <T> T postJSON(String poolKey, String path, String jsonBody, Class<T> clazz) throws IOException {
        StringEntity jsonEntity = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);

        logReq(poolKey, path, HTTP_METHOD_POST, jsonBody);

        return post(poolKey, path, jsonEntity, null, clazz, null);
    }

    public static String post(String path) throws IOException {
        return post(path, String.class);
    }

    public static String post(String path, Map<String, ?> paramMap) throws IOException {
        return post(path, paramMap, String.class);
    }

    public static <T> T post(String path, Class<T> clazz) throws IOException {
        return post(path, null, clazz);
    }

    public static <T> T post(String path, Map<String, ?> paramMap, Class<T> clazz) throws IOException {
        return post(DEFAULT_POOL_KEY, path, paramMap, clazz);
    }

    public static <T> T post(String poolKey, String path, Map<String, ?> paramMap, Class<T> clazz) throws IOException {
        return post(poolKey, path, paramMap, null, clazz);
    }

    public static <T> T post(String poolKey, String path, Map<String, ?> paramMap, HttpResponseCovert<T> covert, Class<T> clazz) throws IOException {
        return post(poolKey, path, paramMap, covert, clazz, null);
    }

    /**
     * 通过指定连接池的连接执行POST请求
     * @param poolKey 连接池键
     * @param path 请求地址，带参数
     * @param paramMap 参数Map
     * @param covert 响应转换器
     * @param clazz 返回JSON模板
     * @param point 请求切点
     * @param <T> 类模版
     * @return 实体
     * @throws IOException 抛出请求过程中的IO异常
     */
    public static <T> T post(String poolKey, String path, Map<String, ?> paramMap, HttpResponseCovert<T> covert, Class<T> clazz, HttpPoint point) throws IOException {
        List<BasicNameValuePair> paramList = new ArrayList<>();

        UrlEncodedFormEntity formEntity = null;

        if(null != paramMap && !paramMap.isEmpty()){
            Iterator<? extends Map.Entry<String, ?>> iterator = paramMap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, ?> elem = iterator.next();
                paramList.add(new BasicNameValuePair(elem.getKey(),null == elem.getValue() ? null : elem.getValue().toString()));
            }
            formEntity = new UrlEncodedFormEntity(paramList, getCharset());
        }

        logReq(poolKey, path, "", paramMap);

        return post(poolKey, path, formEntity, covert, clazz, point);
    }

    /**
     * 记录开始请求日志
     * @param poolKey
     * @param path
     * @param param
     */
    private static void logReq(String poolKey, String path, String method, Object param) {
        if(logger.isInfoEnabled()){
            logger.info("[" + poolKey + "][" + getUUID() + "][" + path + "][" + method + "][BEGIN][PARAM: " + JSON.toJSONString(param) + "]");
        }
    }

    /**
     * 执行POST请求
     * @param poolKey 连接池键
     * @param path 请求地址
     * @param entity 请求体
     * @param covert 响应转换器
     * @param clazz 返回JSON模板
     * @param point 请求切点接口
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T post(String poolKey, String path, HttpEntity entity, HttpResponseCovert<T> covert, Class<T> clazz, HttpPoint point) throws IOException {
        HttpPost httpPost = new HttpPost(path);

        httpPost.setEntity(entity);

        return execute(poolKey, httpPost, covert, clazz, point);
    }

    /**
     * 执行请求
     * @param poolKey 连接池键
     * @param request 请求请求类型
     * @param covert 响应转换器
     * @param clazz 返回JSON模板
     * @param point 请求切点接口
     * @param <T> 类模版
     * @return 实体
     * @throws IOException 抛出请求过程中的IO异常
     */
    public static <T> T execute(String poolKey, HttpUriRequest request, HttpResponseCovert<T> covert, Class<T> clazz, HttpPoint point) throws IOException {
        CloseableHttpClient httpClient = getHttpClient(poolKey);
        HttpResponse response = null;

        String path = request.getURI().toString();

        String method = request.getMethod();

        try {
            long starTime = System.currentTimeMillis();

            Object beforeReturnValue = null;
            if(null != point){
                try{
                    beforeReturnValue = point.beforeExecute(httpClient, request);
                }catch (Exception e){
                    logger.error("[" + poolKey + "][" + getUUID() + "][" + path + "][" + method + "]beforeExecute切点执行失败：" + e.getMessage(), e);
                }
            }

            response = httpClient.execute(request);

            String responseString = EntityUtils.toString(response.getEntity(), getCharset());

            if(null != point){
                try{
                    point.afterExecute(beforeReturnValue, response, responseString);
                }catch (Exception e){
                    logger.error("[" + poolKey + "][" + getUUID() + "][" + path + "][" + method + "]afterExecute切点执行失败：" + e.getMessage(), e);
                }
            }

            if (logger.isInfoEnabled()) {
                responseString = StringUtils.isEmpty(responseString) ? null : responseString.replace("\r", "").replace("\n", "");
                logger.info("[" + poolKey + "][" + getUUID() + "][" + path + "][" + method + "][" + (System.currentTimeMillis() - starTime) + "ms][RESULT: " + responseString + "]");
            }

            if(null == covert){
                covert = HttpResponseCovert.getByResultType(clazz);
            }

            return covert.covertResponse(response, responseString, clazz);
        } catch (SocketTimeoutException e) {
            String errorInfo = "[" + poolKey + "][" + getUUID() + "][" + path + "][" + method + "]请求超时[" + getParamOrDefault(poolKey + KEY_TIMEOUT_REQUEST, DEFAULT_TIMEOUT_REQUEST) + "]";

            if(null != point){
                point.onException(e);
            }

            throw new SocketTimeoutException(errorInfo);
        } finally {
            // 释放连接
            if(null != response){
                EntityUtils.consume(response.getEntity());
            }
            clearUUID();
            clearCharset();
        }
    }

    /**
     * 清理空闲HttpClient线程
     */
    private static final class IdleConnectionMonitor implements Runnable {
        private HttpClientConnectionManager connectionManager;
        private int readTimeout;

        IdleConnectionMonitor(HttpClientConnectionManager connectionManager, int readTimeout) {
            this.connectionManager = connectionManager;
            this.readTimeout = readTimeout;
        }

        @Override
        public void run() {
            // 清理过期连接
            connectionManager.closeExpiredConnections();
            // 清理多余空闲连接
            connectionManager.closeIdleConnections(readTimeout * 2, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 守护线程工厂
     */
    private static class DaemonThreadFactory implements ThreadFactory {
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;
        final String nameSuffix = "]";

        DaemonThreadFactory(String threadName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = threadName + " [Thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group,
                    r,
                    namePrefix + threadNumber.getAndIncrement() + nameSuffix,
                    0);
            t.setDaemon(true);
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}