package com.demo.tutorial.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.demo.tutorial.ExampleApplicationTest;
import com.demo.tutorial.entity.Country;
import com.demo.tutorial.repository.CountryRepository;

@DataJpaTest
public class CountryRepositoryTest implements ExampleApplicationTest  {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private CountryRepository repository;

	@Test
	void testPersistData() throws Exception {
		Country country=new Country();
		country.setName("Nepal");
		country.setPopulation(1000000);
		Country persistedCountry = entityManager.persist(country);
		List<Country> listOfCountries=repository.findAll();

		System.out.println("Country details :: "+country.toString());
		
		assertEquals(country, repository.findById(country.getId()).get());
		assertNotEquals(country, new Country(country.getId(),"Bhutan",1000000));
		assertTrue(persistedCountry!=null);
		assertTrue(country.getId()!=null);
		assertEquals("Nepal", country.getName());
		assertEquals(1000000, country.getPopulation());
		assertEquals(35, listOfCountries.size());
	}

	@Test
	void testFindAll() throws Exception {
		List<Country> listOfCountries=repository.findAll();
		assertEquals(34, listOfCountries.size());
	}

}
