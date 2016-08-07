package com.dps.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dps.domain.constants.Constants;
import com.dps.domain.entity.Configurations;

/**
 * Tests the CURD database operations for {@link Configurations} objects.
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context-dps-service-test.xml"})
public class CurdOperationsTest
{
	@Autowired
	private ConfigurationsService configurationsService;
	
	@Test
	public void insert()
	{
		Configurations config = new Configurations();
		config.setExchangeRate(Constants.BIG_DECIMAL_ONE);
		config.setPricePerCbm(Constants.BIG_DECIMAL_ONE);
		config.setPricePerWeight(Constants.BIG_DECIMAL_ONE);
		
		try
		{
			configurationsService.persist(config);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
