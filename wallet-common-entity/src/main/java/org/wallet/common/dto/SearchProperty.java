package org.wallet.common.dto;

import org.wallet.common.constants.field.EntityField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性搜索方式
 * @author zengfucheng
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchProperty {
    SearchOperator value() default SearchOperator.eq;

    String name() default EntityField.NONE;

    Class valueType() default Void.class;
}
