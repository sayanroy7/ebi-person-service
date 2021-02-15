package com.ebi.app;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockSecurity> {

    @Override
    public SecurityContext createSecurityContext(WithMockSecurity withMockSecurity) {
        // prepare tokens
        String[] scopes = withMockSecurity.scopes();
        SecurityContext securityContext = new SecurityContextImpl();
        var userDetails = User.withDefaultPasswordEncoder().username(withMockSecurity.username())
                .password(withMockSecurity.password())
                .authorities(scopes)
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
               null, userDetails.getAuthorities());
        securityContext.setAuthentication(authentication);
        return securityContext;
    }
}
