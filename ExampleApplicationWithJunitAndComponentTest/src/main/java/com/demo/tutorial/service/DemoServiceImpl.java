package com.demo.tutorial.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.tutorial.entity.Country;
import com.demo.tutorial.repository.CountryRepository;

@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
	private CountryRepository repository;

	@Override
	public String execute() {
		List<Country> noOfCountries=repository.findAll();
		System.out.println("## of countries :: "+noOfCountries.size());
		return "Service Executed !!";
	}

}
