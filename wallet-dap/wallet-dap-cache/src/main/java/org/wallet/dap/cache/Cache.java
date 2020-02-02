package org.wallet.dap.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存接口
 * @author zengfucheng
 * @date 2018年3月23日
 */
public interface Cache {

	/**
	 * 获得缓存中的数据集合
	 * @param cacheName 缓存名称
	 * @return 缓存键值对Map
	 */
	Map<String, Object> get(String cacheName);
	
	/**
	 * 获取指定缓存前缀的所有键值对
	 * @param cacheName 缓存名称
	 * @param clazz 类模版
	 * @return 缓存键值对Map
	 */
	<T> Map<String, T> get(String cacheName, Class<T> clazz);
	
	/**
	 * 获得缓存中的数据
	 * @param cacheName 缓存名称
	 * @param key 缓存键
	 * @return 值
	 */
	Object get(String cacheName, String key);
	
	/**
	 * 
	 * @param cacheName 缓存名称
	 * @param key 缓存键
	 * @param clazz 类模版
	 * @return 值
	 */
	<T> T get(String cacheName, String key, Class<T> clazz);
	
	/**
	 * 批量获取数据
	 * @param cacheName 缓存名称
	 * @param keys 键数组
	 * @return 缓存键值对Map
	 */
	Map<String, Object> get(String cacheName, String[] keys);
	
	/**
	 * 批量获取数据
	 * @param cacheName 缓存名称
	 * @param keys 键数组
	 * @return 缓存键值对Map
	 */
	<T> Map<String, T> get(String cacheName, String[] keys, Class<T> clazz);
	
	/**
	 * 添加数据到缓存
	 * @param cacheName 缓存名称
	 * @param key 缓存键
	 * @param value 值
	 */
	void put(String cacheName, String key, Object value);
	
	/**
	 * 添加数据到缓存，具有过期时间（单位：秒）
	 * @param cacheName 缓存名称
	 * @param key 缓存键
	 * @param value 值
	 * @param expire 过期时间（秒）
	 */
	void put(String cacheName, String key, Object value, long expire);
	
	/**
	 * 添加MAP数据到缓存
	 * @param cacheName 缓存名称
	 * @param map
	 */
	void put(String cacheName, Map<String, Object> map);
	
	/**
	 * 添加MAP数据到缓存，具有过期时间（单位：秒）
	 * @param cacheName 缓存名称
	 * @param map
	 * @param expire 过期时间（秒）
	 */
	void put(String cacheName, Map<String, Object> map, long expire);
	
	/**
	 * 如果缓存中不存在，就把数据设入到缓存中
	 * @param cacheName 缓存名称
	 * @param key 缓存键
	 * @param value 值
	 * @return 缓存是否存在
	 */
	boolean putIfAbsent(String cacheName, String key, Object value);
	
	/**
	 *  如果缓存中不存在，就把数据设入到缓存中，具有过期时间（单位：秒）
	 * @param cacheName 缓存名称
	 * @param key 缓存键
	 * @param value 值
	 * @param expire 过期时间（秒）
	 * @return 缓存是否存在
	 */
	boolean putIfAbsent(String cacheName, String key, Object value, long expire);
	
	/**
	 * 如果缓存中不存在，就把数据设入到缓存中
	 * @param cacheName 缓存名称
	 * @param map 键值对
	 * @return 缓存是否存在
	 */
	boolean putIfAbsent(String cacheName, Map<String, Object> map);
	/**
	 *  如果缓存中不存在，就把数据设入到缓存中，具有过期时间（单位：秒）
	 * @param cacheName 缓存名称
     * @param map 键值对
	 * @param expire 过期时间（秒）
	 * @return 缓存是否存在
	 */
	boolean putIfAbsent(String cacheName, Map<String, Object> map, long expire);

	/**
	 * 从缓存中删除key的数据
	 * @param cacheName 缓存名称
	 * @param key 缓存键
	 */
	void evict(String cacheName, String key);
	
	/**
	 * 批量删除数据
	 * @param cacheName 缓存名称
	 * @param keys 键数组
	 */
	void evict(String cacheName, List<String> keys);
	
	/**
	 * 从缓存中删除cacheName下的数据
	 * @param cacheName 缓存名称
	 */
	void evict(String cacheName);

	/**
	 * 添加集合
	 * @param cacheName 缓存名称
	 * @param values 值数组
     * @return 添加成功的数量
	 */
    long sAdd(String cacheName, Object... values);

    /**
     * 删除集合值
     * @param cacheName 缓存名称
     * @param values 值数组
     * @return 删除成功的数量
     */
    long sRem(String cacheName, Object... values);

    /**
     * 判断集合是否包含该值
     * @param cacheName 缓存名称
     * @param value 值
     * @return 是否包含该值
     */
	boolean sIsMember(String cacheName, Object value);

    /**
     * 获取集合长度
     * @param cacheName 缓存名称
     * @return 集合长度（0表示无此集合）
     */
	long sCard(String cacheName);

    /**
     * 获取集合内所有值
     * @param cacheName 缓存名称
     * @param clazz 类模版
     * @return 集合
     */
	<T> Set<T> sMembers(String cacheName, Class<T> clazz);

	/**
	 * 清除缓存中的所有数据
	 */
	void clear();
}
