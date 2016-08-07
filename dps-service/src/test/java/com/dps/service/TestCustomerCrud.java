package com.dps.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dps.domain.entity.Address;
import com.dps.domain.entity.ContactDetails;
import com.dps.domain.entity.Customer;

/**
 * Inserts a test customer object in the database.
 *
 * @see
 *
 * @Date Jul 23, 2016
 *
 * @author akshay
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context-dps-service-test.xml"})
public class TestCustomerCrud
{
	@Autowired
	private CustomerService customerService;
	
	@Test
	public void method1()
	{
		Customer cust = new Customer();
		ContactDetails cd = new ContactDetails();
		cd.setEmailId("test");
		cd.setPhoneNumber("test");
		cust.setContactDetails(cd);
		Address addr = new Address();
		addr.setBuilding("A");
		addr.setCity("Mumbai");
		addr.setFlatNo("B");
		addr.setLocality("C");
		addr.setState("Maharashtra");
		addr.setStreet("D");
		addr.setZip("E");
		cust.setAddress(addr);
		cust.setAdditionalMargin(new BigDecimal(1.1));
		cust.setName("Akshay");
		cust.setShipmark("ABC");
		
		customerService.persist(cust);
	}
}
