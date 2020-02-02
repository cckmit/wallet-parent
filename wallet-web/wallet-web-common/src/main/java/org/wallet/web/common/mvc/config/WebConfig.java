package org.wallet.web.common.mvc.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.PathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;
import org.wallet.web.common.mvc.WalletRequestMappingHandlerMapping;
import org.wallet.web.common.mvc.token.TokenInterceptor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author zengfucheng
 **/
@Configuration
@EnableConfigurationProperties(CryptoProperties.class)
public class WebConfig extends WebMvcConfigurationSupport {
    @Value("${api.token.enable:true}")
    private Boolean apiTokenEnable;

    @Override
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping handlerMapping = new WalletRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        handlerMapping.setInterceptors(getInterceptors());
        handlerMapping.setContentNegotiationManager(mvcContentNegotiationManager());
        handlerMapping.setCorsConfigurations(getCorsConfigurations());

        PathMatchConfigurer configurer = getPathMatchConfigurer();
        if (configurer.isUseSuffixPatternMatch() != null) {
            handlerMapping.setUseSuffixPatternMatch(configurer.isUseSuffixPatternMatch());
        }
        if (configurer.isUseRegisteredSuffixPatternMatch() != null) {
            handlerMapping.setUseRegisteredSuffixPatternMatch(configurer.isUseRegisteredSuffixPatternMatch());
        }
        if (configurer.isUseTrailingSlashMatch() != null) {
            handlerMapping.setUseTrailingSlashMatch(configurer.isUseTrailingSlashMatch());
        }
        UrlPathHelper pathHelper = configurer.getUrlPathHelper();
        if (pathHelper != null) {
            handlerMapping.setUrlPathHelper(pathHelper);
        }
        PathMatcher pathMatcher = configurer.getPathMatcher();
        if (pathMatcher != null) {
            handlerMapping.setPathMatcher(pathMatcher);
        }
        return handlerMapping;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1、需要先定义一个 convert 转换消息对象；
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        // 2、添加 fastJson 的配置信息，比如: 是否要格式化返回的Json数据；
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        fastJsonConfig.setSerializerFeatures(
                // 输出key时是否使用双引号,默认为true
                SerializerFeature.QuoteFieldNames,
                // Enum输出name()或者original,默认为false
                SerializerFeature.WriteEnumUsingToString,
                // 消除对同一对象循环引用的问题，默认为false
                SerializerFeature.DisableCircularReferenceDetect);

        // 避免浮点数出现科学计数法
        fastJsonConfig.getSerializeConfig().put(Double.class, ToStringSerializer.instance);
        fastJsonConfig.getSerializeConfig().put(Double.TYPE, ToStringSerializer.instance);
        fastJsonConfig.getSerializeConfig().put(Integer.class, ToStringSerializer.instance);
        fastJsonConfig.getSerializeConfig().put(Integer.TYPE, ToStringSerializer.instance);
        fastJsonConfig.getSerializeConfig().put(Long.class, ToStringSerializer.instance);
        fastJsonConfig.getSerializeConfig().put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.getSerializeConfig().put(Boolean.class, ToStringSerializer.instance);
        fastJsonConfig.getSerializeConfig().put(Boolean.TYPE, ToStringSerializer.instance);
        fastJsonConfig.getSerializeConfig().put(Date.class, TimestampSerializer.instance);
        fastJsonConfig.getSerializeConfig().put(BigDecimal.class, BigDecimalSerializer.instance);

        // 处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        // 3、在 Convert 中添加配置信息;
        fastConverter.setFastJsonConfig(fastJsonConfig);

        // 解决接口响应乱码问题
        converters.add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        // 4、将 convert 添加到 converts 中;
        converters.add(1, fastConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if(apiTokenEnable){
            registry.addInterceptor(tokenInterceptor())
                    .addPathPatterns("/**");
        }
    }

    @Bean
    public TokenInterceptor tokenInterceptor(){
        return new TokenInterceptor();
    }


    @Bean
    public Validator validator(){
        // 设置校验模式为：快速失败返回模式
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .failFast(true)
                .buildValidatorFactory();

        return validatorFactory.getValidator();
    }
}
