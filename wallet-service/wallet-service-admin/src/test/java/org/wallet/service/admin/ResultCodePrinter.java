package org.wallet.service.admin;

import org.springframework.util.ReflectionUtils;
import org.wallet.common.enums.ResultCode;

import java.lang.reflect.Field;

/**
 * 响应码打印工具类
 *
 * 以方便更新接口文档
 * @author zengfucheng
 **/
public class ResultCodePrinter {

    public static void main(String [] args){
        printResultCodeMDTable();
    }

    /**
     * 打印 ResultCode Markdown 格式表格说明
     */
    private static void printResultCodeMDTable(){
        System.out.println("||~响应码||~说明||");
        ReflectionUtils.doWithFields(ResultCode.class, ResultCodePrinter::printSingleResultCode);
    }

    /**
     * 打印单个 ResultCode
     * @param field
     * @throws IllegalAccessException
     */
    private static void printSingleResultCode(Field field) throws IllegalAccessException {
        field.setAccessible(true);
        if(ResultCode.class.equals(field.getType())){
            ResultCode resultCode = (ResultCode) field.get(ResultCode.Success);
            System.out.println("||" + resultCode.getCode() + "||" + resultCode.getMessage() + "||");
        }
    }
}
