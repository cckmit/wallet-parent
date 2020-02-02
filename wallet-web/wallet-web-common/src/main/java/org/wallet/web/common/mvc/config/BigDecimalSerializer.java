package org.wallet.web.common.mvc.config;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengfucheng
 **/
public class BigDecimalSerializer implements ObjectSerializer {
    public static final BigDecimalSerializer instance = new BigDecimalSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) { return; }

        serializer.out.writeString(String.valueOf(((BigDecimal) object).stripTrailingZeros().toPlainString()));
    }
}
