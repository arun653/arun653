package com.demo.tutorial.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.tutorial.ExampleApplicationTest;
import com.demo.tutorial.controller.DemoController;
import com.demo.tutorial.service.DemoService;

@Testable
public class DemoControllerTest implements ExampleApplicationTest  {

	@InjectMocks
	DemoController controller;
	@Spy
	private DemoService service;

	@Test
	public void testHello() throws Exception{
		setExpectations();
		ResponseEntity<String> response = controller.hello();
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	private void setExpectations()
	{
		when(service.execute()).thenReturn("Mocked response");
	}

}
