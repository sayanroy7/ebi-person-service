package com.ebi.app;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockSecurity {
    String[] scopes() default {};

    String username() default "user";

    String password() default "user";

    String issuer() default "ebi-auth-service";
}
