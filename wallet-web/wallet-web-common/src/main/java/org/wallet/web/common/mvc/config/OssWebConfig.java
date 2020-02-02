package org.wallet.web.common.mvc.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.wallet.web.common.mvc.XssFilter;

/**
 * @author zengfucheng
 * @date 2018年5月25日
 */
@Configuration
public class OssWebConfig {

	@Bean
	public FilterRegistrationBean<CharacterEncodingFilter> encodingRegistration(CharacterEncodingFilter characterEncodingFilter) {
	    FilterRegistrationBean<CharacterEncodingFilter> registration = new FilterRegistrationBean<>(characterEncodingFilter);
	    registration.addUrlPatterns("/*");
	    registration.addInitParameter("exclusions","/oss/*");
	    registration.setName("encodingRegistration");
	    registration.setOrder(1);
	    return registration;
	}
	
	@Bean
	public FilterRegistrationBean<XssFilter> xssFilterRegistration(XssFilter xssFilter) {
	    FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>(xssFilter);
	    registration.addUrlPatterns("/*");
	    registration.addInitParameter("exclusions","/oss/*");
	    registration.setName("xssFilterRegistration");
	    registration.setOrder(2);
	    return registration;
	}
}
