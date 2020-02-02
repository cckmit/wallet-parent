package org.wallet.web.common.mvc;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.wallet.web.common.mvc.version.ApiVersion;
import org.wallet.web.common.mvc.version.ApiVersionCondition;
import org.wallet.web.common.mvc.version.AppVersion;
import org.wallet.web.common.mvc.version.AppVersionCondition;

import java.lang.reflect.Method;

/**
 * @author zengfucheng
 **/
public class WalletRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        AppVersion appVersion = AnnotationUtils.findAnnotation(handlerType, AppVersion.class);
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        if(null != apiVersion){ return createApiVersionCondition(apiVersion); }
        if(null != appVersion){ return createAppVersionCondition(appVersion); }
        return null;
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        AppVersion appVersion = AnnotationUtils.findAnnotation(method, AppVersion.class);
        if(null != apiVersion){ return createApiVersionCondition(apiVersion); }
        if(null != appVersion){ return createAppVersionCondition(appVersion); }
        return null;
    }

    private RequestCondition<AppVersionCondition> createAppVersionCondition(AppVersion appVersion) {
        return null == appVersion ? null : new AppVersionCondition(appVersion);
    }

    private RequestCondition<ApiVersionCondition> createApiVersionCondition(ApiVersion apiVersion) {
        return null == apiVersion ? null : new ApiVersionCondition(apiVersion.value());
    }
}
