package org.wallet.dap.common.bind.filter;

import lombok.Data;
import org.wallet.common.dto.SearchOperator;

import java.io.Serializable;

/**
 * 参数过滤对象
 */
@Data
public class SearchFilter implements Serializable {

    private String property;
    private SearchOperator operator;
    private Object value;
    private Class valueType;

    /**
     * 默认构造
     */
    public SearchFilter() {
    }

    /**
     * 添加自定义过滤
     */
    public SearchFilter(String property, SearchOperator operator, Object value) {
        this.property = property;
        this.operator = operator;
        this.value = value;
    }

    public SearchFilter(String property, SearchOperator operator, Object value, Class valueType) {
        this.property = property;
        this.operator = operator;
        this.value = value;
        this.valueType = valueType;
    }

    public Object getValue() {
        if (null == valueType || Void.TYPE.equals(valueType)) {
            return value;
        } else if (null != value) {
	        if(valueType.equals(Boolean.class)){
                return Boolean.valueOf(value.toString());
            }
        }
        return value;
    }
}
