package org.wallet.dap.common.boot;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Objects;

/**
 * @author zengfucheng
 * @date 2018年3月13日
 */
public class Bootstrap extends AbstractBootstrap {
	
private ClassPathXmlApplicationContext context;
	
	private static Bootstrap instance;
	
	private Bootstrap() {}
	
	public static Bootstrap getInstance() {
		if(instance == null) {
			synchronized (Bootstrap.class) {
				if(instance == null) {
					instance = new Bootstrap();
				}
			}
		}
		return instance;
	}
	
	public static void run(Class<?> clazz, String[] args) {
		Bootstrap boot = Bootstrap.getInstance();
		boot.setClazz(clazz);
		boot.setArgs(args);
		if(args.length == 0) {
			Bootstrap.getInstance().start();
		} else {
			switch (args.length) {
				case 1: {
					if (args[0].equals("start")) {
						Bootstrap.getInstance().start();
					} else {
						Bootstrap.getInstance().sendClosingSignal();
					}
					break;
				}
				case 2: {
					Bootstrap.getInstance().setServiceName(args[0]);
					if (args[1].equals("start")) {
						Bootstrap.getInstance().start();
					} else {
						Bootstrap.getInstance().sendClosingSignal();
					}
					break;
				}
				default: break;
			}
		}
	}
	
	@Override
	protected void startContainers(Class<?> clazz, String[] args) {
		context = new ClassPathXmlApplicationContext(new String[] {"classpath*:spring/*.xml"});
        context.start();
	}
	
	@Override
	protected void closeContainers() {
		ProtocolConfig.destroyAll(); //关闭dubbo server
		if(!Objects.isNull(context)) {	
			context.stop();
			context.close();
		}
		ReferenceConfigCache.getCache().destroyAll(); //关闭dubbo client
	}
}


