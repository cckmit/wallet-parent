package org.wallet.web.common.mvc.config;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author zengfucheng
 **/
public class TimestampSerializer implements ObjectSerializer {
    public static final TimestampSerializer instance = new TimestampSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) { return; }

        serializer.out.writeString(String.valueOf(((Date) object).getTime() / 1000));
    }
}
