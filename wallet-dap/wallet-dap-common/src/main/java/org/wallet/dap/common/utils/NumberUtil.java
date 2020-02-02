package org.wallet.dap.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class NumberUtil {
	private static final String NUMBER_REGEX = "[0-9]*";

	/**
	 * Double保留2位小数位
	 */
	public static Double toFixed(Double double1){
		double f1=0.00;
		try {
			if(double1!= null){
				BigDecimal bg = new BigDecimal(double1);  
				f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		} catch (Exception e) {
            log.error("转换小数位出错");
		}
		return f1;			
	}

	/**
	 * 字符串转 int
	 */
	public static Integer stringToInt(String string){
		Integer f1 = 0;
		try {
			f1 = Integer.parseInt(string);
		} catch (Exception e) {
            log.error("字符串转 int 出错");
		} 
		return f1;
	}
	
	/**
	 * 字符串转 浮点型
	 */
	public static Float stringToFloat(String string){
		Float f1 = 0f;
		try {
			f1 = Float.parseFloat(string);
		} catch (Exception e) {
			log.error("字符串转 Float 出错");
		}
		return f1;	
	}
	
	/**
	 * 字符串转double
	 */
	public static Double stringToDouble(String string){
		Double f1 = 0d;
		try {
			f1 = Double.parseDouble(string);
		} catch (Exception e) {
			log.error("字符串转 double 出错");
		}
		return f1;	
	}

	/**
	 * 百分比 转 double
	 * 12%》》0.12
	 */
	public static Double bfbToDouble(String string){
		Double f1 = 0d;
		try {
			string = string.substring(0,string.length() - 1);
			f1 = Double.parseDouble(string) / 100 ;
		} catch (Exception ignore) {
			log.error("百分比 转 double 出错");
		}
		return f1;
	}

	/**
	 * 正则验证字符串 是否是number
	 */
	public static boolean isNumber(String str){
	   Pattern pattern = Pattern.compile(NUMBER_REGEX); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}

}
