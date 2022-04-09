package com.demo.tutorial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.demo.tutorial.service.DemoService;

@Controller
public class DemoController {

	@Autowired
	private DemoService service;

	@Value("${third.party.base.url}")
	private String url;

	@GetMapping("/demo")
	public ResponseEntity<String> hello()
	{

		String serviceResponse=service.execute();
		System.out.println("In DemoController :: hello :: serviceResponse :: "+serviceResponse);
		return new ResponseEntity<String>("Hello World !!", HttpStatus.OK);
	}

	@GetMapping("/api")
	public ResponseEntity<String> api()
	{
		RestTemplate restTemplate=new RestTemplate();
		String response = restTemplate.getForObject(url, String.class);

		System.out.println("In DemoController :: api :: apiResponse :: "+response);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

}
