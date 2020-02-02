package org.wallet.dap.cache.serial;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author zengfucheng
 **/
public class FastJsonSerializationUtils {

    private static final FastJsonConfig CONFIG = new FastJsonConfig();

    static{
        CONFIG.setSerializerFeatures(SerializerFeature.WriteClassName);
    }

    public static byte[] serialize(Object object) {
        if (object == null) {
            return new byte[0];
        }
        try {
            return JSON.toJSONBytes(
                    CONFIG.getCharset(),
                    object,
                    CONFIG.getSerializeConfig(),
                    CONFIG.getSerializeFilters(),
                    CONFIG.getDateFormat(),
                    JSON.DEFAULT_GENERATE_FEATURE,
                    CONFIG.getSerializerFeatures()
            );
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return (T) JSON.parseObject(
                    bytes,
                    CONFIG.getCharset(),
                    Object.class,
                    CONFIG.getParserConfig(),
                    CONFIG.getParseProcess(),
                    JSON.DEFAULT_PARSER_FEATURE,
                    CONFIG.getFeatures()
            );
        } catch (Exception ex) {
            throw new SerializationException("Could not deserialize: " + ex.getMessage(), ex);
        }
    }
}
