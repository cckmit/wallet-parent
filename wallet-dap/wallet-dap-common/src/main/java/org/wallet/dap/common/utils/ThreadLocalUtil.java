package org.wallet.dap.common.utils;

import java.util.*;

/**
 * 线程变量工具类
 * @author zengfucheng
 **/
public final class ThreadLocalUtil {
    /**
     * 线程变量
     */
    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(() -> new HashMap<>(4));

    public static Map<String, Object> getThreadLocal() {
        return THREAD_LOCAL.get();
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        return (T) map.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key, T defaultValue) {
        Map<String, Object> map = THREAD_LOCAL.get();
        return map.get(key) == null ? defaultValue : (T) map.get(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> void set(String key, T value) {
        Map<String, Object> map = THREAD_LOCAL.get();
        map.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static void set(Map<String, Object> keyValueMap) {
        Map<String, Object> map = THREAD_LOCAL.get();
        map.putAll(keyValueMap);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String, T> fetchVarsByPrefix(String prefix) {
        Map<String, Object> map = THREAD_LOCAL.get();
        Map<String, T> vars = new HashMap<>(map.size());
        if (prefix == null) {
            return vars;
        }
        Set<Map.Entry<String, Object>> set = map.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            String key = entry.getKey();
            if (key.startsWith(prefix)) {
                vars.put(key, (T) entry.getValue());
            }
        }
        return vars;
    }

    @SuppressWarnings("unchecked")
    public static <T> T remove(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        return (T) map.remove(key);
    }

    public static void clear(String prefix) {
        if (prefix == null) {
            return;
        }
        Map<String, Object> map = THREAD_LOCAL.get();
        Set<Map.Entry<String, Object>> set = map.entrySet();
        List<String> removeKeys = new ArrayList<>();

        for (Map.Entry<String, Object> entry : set) {
            String key = entry.getKey();
            if (key.startsWith(prefix)) {
                removeKeys.add(key);
            }
        }
        for (String key : removeKeys) {
            map.remove(key);
        }
    }
}
