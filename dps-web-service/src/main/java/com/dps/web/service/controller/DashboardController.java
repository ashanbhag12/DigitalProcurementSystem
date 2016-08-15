package com.dps.web.service.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.domain.entity.Configurations;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerService;
import com.dps.service.ProductService;
import com.dps.service.SupplierService;
import com.dps.web.service.model.DashboardDTO;

/**
 * Controller class that handles all the web service requests for dashboard operations.
 *
 * @see
 *
 * @Date Aug 15, 2016
 *
 * @author akshay
 */
@Path("/dashboard")
public class DashboardController 
{
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private ConfigurationsService configService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public DashboardDTO getDashboardDetails()
	{
		int productCount = productService.getProductCount();
		int customerCount = customerService.getCustomerCount();
		int supplierCount = supplierService.getSupplierCount();
		Configurations config = configService.getConfigurations();
		
		DashboardDTO dashboardDto = new DashboardDTO();
		dashboardDto.setCustomerCount(customerCount);
		dashboardDto.setExchangeRate(config.getExchangeRate().doubleValue());
		dashboardDto.setProductsCount(productCount);
		dashboardDto.setSupplierCount(supplierCount);
		
		return dashboardDto;
	}
	
}
