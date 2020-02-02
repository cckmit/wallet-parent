package org.wallet.dap.cache.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 序列化工具
 * @author zengfucheng
 * @date 2018年3月27日
 */
public class KryoSerializationUtils {

    /**
     * 每个线程的 Kryo 实例
     */
    private static final ThreadLocal<Kryo> KRYO_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        //默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置
        kryo.setReferences(false);
        //不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
        //默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置
        kryo.setRegistrationRequired(false);
        //Fix the NPE bug when deserialize Collections.
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        return kryo;
    });
    
    public static Kryo getInstance() {
        return KRYO_LOCAL.get();
    }
    
    public static byte[] serialize(Object object) {
    	if(object == null) return null;
    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Output output = new Output(byteArrayOutputStream);
		getInstance().writeClassAndObject(output, object);
		output.flush();
		return byteArrayOutputStream.toByteArray();
    }

    @SuppressWarnings("unchecked")
	public static <T> T deserialize(byte[] byteArray) {
		if(byteArray == null) return null;
		Input input = new Input(new ByteArrayInputStream(byteArray));
		return (T) getInstance().readClassAndObject(input);
    }
}
