package com.adpro.backend;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
    private BackendApplication backendApplication;


    @Test
    void contextLoads() {
        assertNotNull(backendApplication);
    }

    @Test
    void testSpringApplicationRun() {
         assertDoesNotThrow(() -> backendApplication.main(new String[] {}));

    }

}
