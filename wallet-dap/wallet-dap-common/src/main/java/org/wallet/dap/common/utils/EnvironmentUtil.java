package org.wallet.dap.common.utils;

import org.springframework.core.env.Environment;

public class EnvironmentUtil {
    private static Environment environment = null;

    public static void init(Environment env){
        environment = env;
    }

    public static String getProperty(String key) {
        if(environment == null){
            throw new IllegalArgumentException("环境变量未初始化");
        }
        return environment.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(environment == null){
            return defaultValue;
        }
        return environment.getProperty(key, defaultValue);
    }
}
