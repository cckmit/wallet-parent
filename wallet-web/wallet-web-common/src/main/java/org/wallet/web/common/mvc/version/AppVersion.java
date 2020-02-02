package org.wallet.web.common.mvc.version;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * App 版本控制
 * @author zengfucheng
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface AppVersion {

    /**
     * 接口版本匹配规则
     * @return 匹配模型
     */
    MatchModel[] rules() default {};
}
