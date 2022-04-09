package com.demo.tutorial.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.tutorial.entity.Country;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> {

	List<Country> findAll();
	
}