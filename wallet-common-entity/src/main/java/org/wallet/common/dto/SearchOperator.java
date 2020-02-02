package org.wallet.common.dto;

import org.springframework.util.StringUtils;

/**
 * 搜索操作符
 */
public enum SearchOperator {
	/**
	 * 搜索操作枚举
	 */
    eq("等于", "="), ne("不等于", "!="),
    gt("大于", ">"), gte("大于等于", ">="), lt("小于", "<"), lte("小于等于", "<="),
    leftLike("左模糊匹配", "like"), rightLike("右模糊匹配", "like"),
    allLike("全模糊匹配", "like"), notLike("不匹配", "not like"),
    isNull("空", "is null"), isNotNull("非空", "is not null"),
    in("包含", "in"), notIn("不包含", "not in");

    private final String info;
    private final String operator;

    SearchOperator(final String info, String operator) {
        this.info = info;
        this.operator = operator;
    }

    public String getInfo() {
        return info;
    }

    public String getOperator() {
        return operator;
    }

    /**
     * 操作符是否允许为空
     * @param operator
     * @return
     */
    public static boolean isAllowBlankValue(final SearchOperator operator) {
        return operator == SearchOperator.isNotNull || operator == SearchOperator.isNull;
    }

    /**
     * 获取枚举
     * @param operator
     * @return
     * @throws RuntimeException
     */
    public static SearchOperator valueBySymbol(String operator) throws RuntimeException {
    	operator = formatSymbol(operator);
        for (SearchOperator op : values()) {
            if (op.name().equals(operator)) {
                return op;
            }
        }
        throw new RuntimeException("不支持的查询操作符号: " + operator);
    }

    /**
     * 格式化操作符
     * @param symbol
     * @return
     */
    private static String formatSymbol(String symbol) {
        if (StringUtils.isEmpty(symbol)) {
            return symbol;
        }
        return symbol.trim().toLowerCase().replace("  ", " ");
    }
    
}
