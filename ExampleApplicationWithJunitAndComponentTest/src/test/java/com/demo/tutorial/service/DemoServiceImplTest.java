package com.demo.tutorial.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.demo.tutorial.ExampleApplicationTest;
import com.demo.tutorial.repository.CountryRepository;
import com.demo.tutorial.service.DemoServiceImpl;

@Testable
public class DemoServiceImplTest implements ExampleApplicationTest  {
	
	@InjectMocks
	DemoServiceImpl service;
	@Mock
	CountryRepository repository;
	
	@Test
	public void testExecute()
	{
		setExpectations();
		String response=service.execute();
		assertEquals("Service Executed !!", response);
	}

	private void setExpectations() {
		when(repository.count()).thenReturn(30L);		
	}
	

}
