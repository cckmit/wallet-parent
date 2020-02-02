package org.wallet.web.admin.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.wallet.common.dto.SimpleResult;
import org.wallet.dap.common.bind.Results;
import org.wallet.web.common.mvc.token.TokenException;
import org.wallet.web.common.utils.ContextUtil;
import org.wallet.web.common.utils.TokenUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zengfucheng
 **/
@Slf4j
public class ShiroJwtFilter extends AuthenticatingFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginRequest(request, response)) {
            return true;
        }
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch(IllegalStateException e){
            log.error("Not found any token");
        }catch (Exception e) {
            log.error("Error occurs when login", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse resp) {
        HttpServletResponse response = WebUtils.toHttp(resp);
        Throwable t = e.getCause();
        SimpleResult result;
        if(t instanceof TokenException){
            result = Results.byCode(((TokenException) t).getResultCode());
        } else {
            result = Results.fail(e.getMessage());
        }
        ContextUtil.sendResult(result, response);
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse resp) throws Exception {
        return false;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest req, ServletResponse resp) throws Exception {
        HttpServletRequest request = (HttpServletRequest) req;
        String token = TokenUtil.getToken(request);
        return new ShiroJwtToken(token);
    }
}
