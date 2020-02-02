package org.wallet.web.admin.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @author zengfucheng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

	String value() default "";
}
