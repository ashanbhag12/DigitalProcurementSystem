package com.dps.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dps.domain.entity.ContactDetails;
import com.dps.domain.entity.Supplier;

/**
 * Inserts a test supplier
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context-dps-service-test.xml"})
public class TestInsertSupplier
{
	@Autowired
	private SupplierService supplierService;
	
	@Test
	public void method1()
	{
		Supplier supp = new Supplier();
		ContactDetails cd = new ContactDetails();
		cd.setEmailId("test");
		cd.setPhoneNumber("15615");
		supp.setContactDetails(cd);
		supp.setName("Akshay");
		supp.setInitials("ABC");
		
		supplierService.persist(supp);
	}
}
