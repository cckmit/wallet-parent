package org.wallet.dap.common.utils;

import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * @author zengfucheng
 **/
public class FileUtil {

    public static InputStream getFileInputStream(String absolutePath) {
        try {
            return new FileInputStream(absolutePath);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static String getFileSuffix(String fileName){
        if(StringUtils.isEmpty(fileName)){
            return fileName;
        }else if(fileName.contains(".")){
            return fileName.split("[.]")[1];
        }else{
            return null;
        }
    }

    public static String createFilePathByDate(){
        LocalDate date = LocalDate.now();
        return date.getYear() + "/" + String.format("%02d", date.getMonthValue()) + "/" + String.format("%02d", date.getDayOfMonth()) + "/";
    }

    public static String createFileNameByTime(String suffix){
        if(StringUtils.isEmpty(suffix)){ suffix = ""; }
        if(suffix.length() > 0 && !suffix.startsWith(".")){
            suffix = "." + suffix;
        }
        return System.currentTimeMillis() + suffix;
    }
}
