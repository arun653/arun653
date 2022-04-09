package com.demo.tutorial;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Profile("test")
//@ExtendWith(SpringExtension.class)
@SpringJUnitConfig
public interface ExampleApplicationTest {

	

}
