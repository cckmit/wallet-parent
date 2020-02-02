package org.wallet.web.admin.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author zengfucheng
 **/
public class ShiroJwtToken implements AuthenticationToken {
    private String token;

    ShiroJwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public String toString() {
        return token;
    }
}
