package org.wallet.dap.cache;

import java.util.List;
import java.util.Map;

/**
 * @author zengfucheng
 * @date 2018年4月4日
 */
public class RedisCacheTemplate{

	private static Cache cache;
	
	public RedisCacheTemplate(Cache cache) {
		RedisCacheTemplate.cache = cache;
	}

	public static Map<String, Object> get(String cacheName) {
		return cache.get(cacheName);
	}

	public static <T> Map<String, T> get(String cacheName, Class<T> clazz) {
		return cache.get(cacheName, clazz);
	}

	public static Object get(String cacheName, String key) {
		return cache.get(cacheName, key);
	}

	public static <T> T get(String cacheName, String key, Class<T> clazz) {
		return cache.get(cacheName, key, clazz);
	}

	public static void put(String cacheName, String key, Object value) {
		cache.put(cacheName, key, value);
	}

	public static void put(String cacheName, String key, Object value, long expire) {
		cache.put(cacheName, key, value, expire);
	}

	public static void put(String cacheName, Map<String, Object> map) {
		cache.put(cacheName, map);
	}

	public static void put(String cacheName, Map<String, Object> map, long expire) {
		cache.put(cacheName, map, expire);
	}

	public static boolean putIfAbsent(String cacheName, String key, Object value) {
		return cache.putIfAbsent(cacheName, key, value);
	}

	public static boolean putIfAbsent(String cacheName, String key, Object value, long expire) {
		return cache.putIfAbsent(cacheName, key, value, expire);
	}

	public static boolean putIfAbsent(String cacheName, Map<String, Object> map) {
		return cache.putIfAbsent(cacheName, map);
	}

	public static boolean putIfAbsent(String cacheName, Map<String, Object> map, long expire) {
		return cache.putIfAbsent(cacheName, map, expire);
	}

	public static void evict(String cacheName, String key) {
		cache.evict(cacheName, key);
	}
	
	public static void evict(String cacheName, List<String> keys) {
		cache.evict(cacheName, keys);
	}

	public static void evict(String cacheName) {
		cache.evict(cacheName);
	}

	public static void clear() {
		cache.clear();
	}

}
