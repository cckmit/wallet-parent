package org.wallet.dap.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class RedisScriptExecutor implements InitializingBean {
    private static RedisTemplate<String, Object> redisTemplate;
    private static Map<String, String> scriptMap;

    public RedisScriptExecutor() {
    }

    public RedisScriptExecutor(RedisTemplate<String, Object> redisTemplate) {
        RedisScriptExecutor.redisTemplate = redisTemplate;
    }

    public static void execute(String scriptFileName, List<String> keys, List<Object> args) {
        executeScript(scriptFileName, keys, args, Object.class);
    }

    public static <T> T execute(String scriptFileName, List<String> keys, List<Object> args, Class<T> clazz) {
        if(clazz == null) { throw new IllegalArgumentException("aguament clazz can not be null"); }
        return executeScript(scriptFileName, keys, args, clazz);
    }

    public static <T> T executeScript(String scriptFileName, List<String> keys, List<Object> args, Class<T> clazz) {
        if(StringUtils.isEmpty(scriptFileName)) { throw new IllegalArgumentException("scriptFileName can not be null"); }
        if(keys == null || keys.isEmpty()) { throw new IllegalArgumentException("keys can not be null"); }
        String script = scriptMap.get(scriptFileName);
        if (script == null) { throw new IllegalArgumentException(String.format("File [%s] is not exist", scriptFileName)); }
        DefaultRedisScript<T> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(script);
        redisScript.setResultType(clazz);
        return redisTemplate.execute(redisScript, keys, args.toArray());
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisScriptExecutor.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scriptMap = readAllScriptFiles();
    }

    private Map<String, String> readAllScriptFiles() throws IOException {
        Map<String, String> scriptMap = new HashMap<String, String>();
        PathMatchingResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources("classpath*:script/*.lua");
        for(Resource resource : resources) {
            long len = resource.contentLength();
            byte[] bytes = new byte[(int) len];
            InputStream inputStream = resource.getInputStream();
            inputStream.read(bytes);
            String data = new String(bytes, Charset.defaultCharset());
            String fileName = resource.getFilename();
            scriptMap.put(fileName, data);
            log.info("Load file[{}] success!!!", fileName);
        }
        return scriptMap;
    }
}
