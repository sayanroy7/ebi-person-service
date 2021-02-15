package com.ebi.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = {EnableMockSecurityTestConfig.class})
class EbiPersonServiceApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;


    @Test
    public void shouldLoadContext() {
        assertThat(applicationContext).isNotNull();
    }

}
