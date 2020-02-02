package org.wallet.dap.common.log;

import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import java.util.Arrays;
import java.util.List;

/**
 * FastJSON 字符串序列化格式配置实体
 * @author zengfucheng
 **/
public class LogFormatEntity {
    /**
     * 需要配置的类
     */
    private Class<?> clazz;
    /**
     * 需要序列化的属性
     */
    private List<String> includes;
    /**
     * 不需要序列化的属性
     */
    private List<String> excludes;

    public LogFormatEntity() {
    }

    public LogFormatEntity(Class<?> clazz) {
        this.clazz = clazz;
    }

    public LogFormatEntity(Class<?> clazz, String... includes) {
        this.clazz = clazz;
        this.includes = Arrays.asList(includes);
    }

    public LogFormatEntity(Class<?> clazz, List<String> excludes) {
        this.clazz = clazz;
        this.excludes = excludes;
    }

    public SimplePropertyPreFilter covertToFilter(){
        if(null == clazz){
            return null;
        }

        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(clazz);
        if(null != includes){
            filter.getIncludes().addAll(includes);
        }
        if(null != excludes){
            filter.getExcludes().addAll(excludes);
        }
        return filter;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }
}
