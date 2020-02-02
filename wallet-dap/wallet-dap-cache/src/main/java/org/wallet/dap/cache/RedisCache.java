package org.wallet.dap.cache;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.wallet.dap.cache.serial.FastJsonSerializationUtils;

import java.util.*;

/**
 * @author zengfucheng
 * @date 2018年3月27日
 */
public class RedisCache implements Cache {
	private final static String CACHE_NAME_ERROR = "cacheName can not be null!";
	private final static String KEY_ERROR = "key can not be null!";
	private final static String KEYS_ERROR = "keys can not be null!";
	private final static String VALUE_ERROR = "value can not be null!";
	private final static String MAP_ERROR = "parameter [map] can not be null!";
	private final static String CLASS_ERROR = "clazz can not be null!";
	private final static String SEP = ":";
	private final static String ASTERISK = "*";
	private final static String SEP_ASTERISK = ":*";
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public Map<String, Object> get(String cacheName) {
		if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
        checkKeysCacheName(cacheName);
		return redisTemplate.execute((RedisCallback<Map<String, Object>>) connection -> {
			Set<byte[]> keys = connection.keys((cacheName+SEP_ASTERISK).getBytes());
			Map<String, Object> map = new HashMap<>(keys.size());
			int keySize = keys.size();
			if(keySize == 0) {return map;}
			byte[][] rawKey = keys.toArray(new byte[keySize][]);
			List<byte[]> values = connection.mGet(rawKey);
			for(int i=0;i<rawKey.length; i++) {
				map.put(new String(rawKey[i]).substring((cacheName+":").length()), FastJsonSerializationUtils.deserialize(values.get(i)));
			}
			return map;
		});
	}

	@Override
	public <T> Map<String, T> get(String cacheName, Class<T> clazz) {
		if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
        checkKeysCacheName(cacheName);
		if(clazz == null) {throw new IllegalArgumentException(CLASS_ERROR);}
		return redisTemplate.execute((RedisCallback<Map<String, T>>) connection -> {
			Set<byte[]> keys = connection.keys((cacheName+SEP_ASTERISK).getBytes());
			Map<String, T> map = new HashMap<>(keys.size());
			int keySize = keys.size();
			if(keySize == 0) {return map;}
			byte[][] rawKey = keys.toArray(new byte[keySize][]);
			List<byte[]> values = connection.mGet(rawKey);
			for(int i=0;i<rawKey.length; i++) {
				map.put(new String(rawKey[i]).substring((cacheName+":").length()), FastJsonSerializationUtils.deserialize(values.get(i)));
			}
			return map;
		});
	}

	@Override
	public Object get(String cacheName, String key) {
		 if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
		 if(StringUtils.isEmpty(key)) {throw new IllegalArgumentException(KEY_ERROR);}
		 return redisTemplate.execute((RedisCallback<Object>) connection -> {
			 byte[] value = connection.get(combKey(cacheName, key));
			 return value != null ? FastJsonSerializationUtils.deserialize(value) : null;
		 });
	}

	@Override
	public <T> T get(String cacheName, String key, Class<T> clazz) {
		if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
		if(StringUtils.isEmpty(key)) {throw new IllegalArgumentException(KEY_ERROR);}
		if(clazz == null) {throw new IllegalArgumentException(CLASS_ERROR);}
		T value = redisTemplate.execute((RedisCallback<T>) connection -> {
			byte[] value1 = connection.get(combKey(cacheName, key));
			return value1 != null ? FastJsonSerializationUtils.deserialize(value1) : null;
		});
        return clazz.isInstance(value) ? value : null;
	}

	@Override
	public Map<String, Object> get(String cacheName, String[] keys) {
		if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
		if(keys == null || keys.length == 0) {throw new IllegalArgumentException(KEYS_ERROR);}
		return redisTemplate.execute((RedisCallback<Map<String, Object>>) connection -> {
			Map<String, Object> map = new HashMap<>();
			int keySize = keys.length;
			byte[][] rawKey = new byte[keySize][];
			for(int i=0; i<keySize; i++) {
				rawKey[i] = combKey(cacheName, keys[i]);
			}
			List<byte[]> values = connection.mGet(rawKey);
			for(int i=0; i<keySize; i++) {
				map.put(new String(rawKey[i]).substring((cacheName+":").length()), FastJsonSerializationUtils.deserialize(values.get(i)));
			}
			return map;
		});
	}

	@Override
	public <T> Map<String, T> get(String cacheName, String[] keys, Class<T> clazz) {
		if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
		if(keys == null || keys.length == 0) {throw new IllegalArgumentException(KEYS_ERROR);}
		if(clazz == null) {throw new IllegalArgumentException(CLASS_ERROR);}
		return redisTemplate.execute((RedisCallback<Map<String, T>>) connection -> {
			Map<String, T> map = new HashMap<>();
			int keySize = keys.length;
			byte[][] rawKey = new byte[keySize][];
			for(int i=0; i<keySize; i++) {
				rawKey[i] = combKey(cacheName, keys[i]);
			}
			List<byte[]> values = connection.mGet(rawKey);
			for(int i=0; i<keySize; i++) {
				map.put(new String(rawKey[i]).substring((cacheName+":").length()), FastJsonSerializationUtils.deserialize(values.get(i)));
			}
			return map;
		});
	}

	@Override
	public void put(String cacheName, String key, Object value) {
		put(cacheName, key, value, -1);
	}

	@Override
	public void put(String cacheName, String key, Object value, long expire) {
		if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
		if(StringUtils.isEmpty(key)) {throw new IllegalArgumentException(KEY_ERROR);}
		if(value == null) {throw new IllegalArgumentException(VALUE_ERROR);}
		byte[] combKey = combKey(cacheName, key);
		redisTemplate.execute((RedisCallback<Boolean>) connection -> {
			if(expire > 0) {connection.setEx(combKey, expire, FastJsonSerializationUtils.serialize(value));}
			else {connection.set(combKey, FastJsonSerializationUtils.serialize(value));}
			return true;
		});
	}

	@Override
	public boolean putIfAbsent(String cacheName, String key, Object value) {
		return putIfAbsent(cacheName, key, value, -1);
	}

	@Override
	public boolean putIfAbsent(String cacheName, String key, Object value, long expire) {
		if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
		if(StringUtils.isEmpty(key)) {throw new IllegalArgumentException(KEY_ERROR);}
		byte[] combKey = combKey(cacheName, key);
		return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
			boolean b = connection.setNX(combKey, FastJsonSerializationUtils.serialize(value));
			if(expire > 0 && b) {connection.expire(combKey, expire);}
			return b;
		});
	}

	@Override
	public void evict(String cacheName, String key) {
		if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
		if(StringUtils.isEmpty(key)) {throw new IllegalArgumentException(KEY_ERROR);}
		redisTemplate.execute((RedisCallback<Long>) connection -> connection.del(combKey(cacheName, key)));
	}

	@Override
	public void evict(String cacheName, List<String> keys) {
		if(keys.isEmpty()) {return;}
		redisTemplate.execute((RedisCallback<Long>) connection -> {
			byte[][] keyArray = new byte[keys.size()][];
			for(int i=0; i<keys.size(); i++) {
				keyArray[i] = combKey(cacheName, keys.get(i));
			}
			return connection.del(keyArray);
		});
	}

	@Override
	public void evict(String cacheName) {
        if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
        checkKeysCacheName(cacheName);
		redisTemplate.execute((RedisCallback<Long>) connection -> {
            Set<byte[]> keys = connection.keys((cacheName + SEP_ASTERISK).getBytes());
			if(keys.size() == 0) {return 0L;}
			byte[][] keyArray = new byte[keys.size()][];
			int i = 0;
            for (Iterator<byte[]> it = keys.iterator(); it.hasNext(); i++) {
                keyArray[i] = it.next();
            }
			return connection.del(keyArray);
		});
	}

    @Override
    public long sAdd(String cacheName, Object... values) {
        if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
        if(null == values || values.length == 0) {return 0;}
        checkKeysCacheName(cacheName);
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            byte[][] bytes = new byte[values.length][];
            for (int i = 0; i < values.length; i++) {
                bytes[i] = FastJsonSerializationUtils.serialize(values[i]);
            }
            return connection.sAdd(cacheName.getBytes(), bytes);
        });
    }

    @Override
    public long sRem(String cacheName, Object... values) {
        if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
        if(null == values || values.length == 0) {return 0;}
        checkKeysCacheName(cacheName);
        return redisTemplate.execute((RedisCallback<Long>) connection -> {
            byte[][] bytes = new byte[values.length][];
            for (int i = 0; i < values.length; i++) {
                bytes[i] = FastJsonSerializationUtils.serialize(values[i]);
            }
            return connection.sRem(cacheName.getBytes(), bytes);
        });
    }

    @Override
    public boolean sIsMember(String cacheName, Object value) {
        if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
        if(StringUtils.isEmpty(value)) {throw new IllegalArgumentException(VALUE_ERROR);}
        checkKeysCacheName(cacheName);
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.sIsMember(cacheName.getBytes(), FastJsonSerializationUtils.serialize(value)));
    }

    @Override
    public long sCard(String cacheName) {
        if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
        checkKeysCacheName(cacheName);
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.sCard(cacheName.getBytes()));
    }

    @Override
    public <T> Set<T> sMembers(String cacheName, Class<T> clazz) {
        if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
        checkKeysCacheName(cacheName);
        return redisTemplate.execute((RedisCallback<Set<T>>) connection -> {
            Set<byte[]> byteSet = connection.sMembers(cacheName.getBytes());
            Set<T> valueSet = new HashSet<>(byteSet.size());
            byteSet.iterator().forEachRemaining(bytes -> valueSet.add(FastJsonSerializationUtils.deserialize(bytes)));
            return valueSet;
        });
    }

    @Override
	public void clear() {
		redisTemplate.execute((RedisCallback<String>) connection -> {
			connection.flushDb();
			return "ok";
		});
	}

	@Override
	public void put(String cacheName, Map<String, Object> map) {
		put(cacheName, map, -1);
	}

	@Override
	public void put(String cacheName, Map<String, Object> map, long expire) {
		if(map.isEmpty()) {return;}
		redisTemplate.execute((RedisCallback<Boolean>) connection -> {
			connection.mSet(transfer(cacheName, map));
			expire(connection, cacheName, map, expire);
			return true;
		});
	}

	@Override
	public boolean putIfAbsent(String cacheName, Map<String, Object> map) {
		return putIfAbsent(cacheName, map , -1);
	}

	@Override
	public boolean putIfAbsent(String cacheName, Map<String, Object> map, long expire) {
		if(map.isEmpty()) {return false;}
		return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
			boolean isSuccess=connection.mSetNX(transfer(cacheName, map));
			if(isSuccess && expire > 0) {expire(connection, cacheName, map, expire);}
			return isSuccess;
		});
	}

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private byte[] combKey(String cacheName, String key) {
        return (cacheName + ":" + key).getBytes();
    }

    private void checkKeysCacheName(String cacheName) {
        if (!cacheName.contains(SEP)) {
            throw new IllegalArgumentException(String.format("cacheName[%s] must contains ':'", cacheName));
        }
        if (cacheName.contains(ASTERISK)) {
            throw new IllegalArgumentException(String.format("cacheName[%s] can not contains *", cacheName));
        }
    }

	private void expire(RedisConnection connection, String cacheName, Map<String, Object> map, long expire) {
		if(expire > 0){map.forEach((k,v) -> connection.expire(combKey(cacheName, k), expire));}
	}

	private Map<byte[], byte[]> transfer(String cacheName, Map<String, Object> map) {
		if(StringUtils.isEmpty(cacheName)) {throw new IllegalArgumentException(CACHE_NAME_ERROR);}
		if(map == null || map.size() == 0) {throw new IllegalArgumentException(MAP_ERROR);}
		Map<byte[], byte[]> ma = new HashMap<>(map.size());
		map.forEach((k, v) -> ma.put(combKey(cacheName, k), FastJsonSerializationUtils.serialize(v)));
		return ma;
	}

}
