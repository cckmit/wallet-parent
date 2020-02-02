package org.wallet.web.common.mvc.version;

import org.wallet.common.enums.Device;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 匹配模型
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface MatchModel {
    /**
     * 匹配设备类型
     * @return 设备类型
     */
    Device device();

    /**
     * 匹配类型
     * @return 匹配类型
     */
    MatchType matchType() default MatchType.ALL;

    /**
     * 客户端版本号
     * @return 客户端版本号
     */
    int appVersion() default 0;
}
