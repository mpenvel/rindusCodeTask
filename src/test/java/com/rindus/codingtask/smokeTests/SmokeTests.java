package com.rindus.codingtask.smokeTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rindus.codingtask.controllers.UserController;

@SpringBootTest
public class SmokeTests {
	
	@Autowired
	private UserController userController;
	
    @Test
    public void contexLoads() throws Exception {
        assertThat(userController).isNotNull();
    }
}
