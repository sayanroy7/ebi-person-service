/**
 * Copyright 2020 Xylem
 * All rights reserved
 */
package com.ebi.app;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class EnableMockSecurityTestConfig {
}
