package org.wallet.dap.common.utils;

import java.util.List;

/**
 * 转换工具类
 */
public class ConverterUtils {

	/**
	 * 数组 查询in操作符 jpql 语句
	 */
	public static String arrayToInJpql(List<String> ids){
		if(ids==null || ids.size()== 0) {
			return "()";
		}
		StringBuilder str = new StringBuilder("(");
		for (String s : ids) {
			str.append("'").append(s).append("',");
		}
		return str.toString().substring(0,str.length()-1)+")";
	}
}
