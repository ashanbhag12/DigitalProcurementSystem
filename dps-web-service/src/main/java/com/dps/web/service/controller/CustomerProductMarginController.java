package com.dps.web.service.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.Constants;
import com.dps.domain.entity.Customer;
import com.dps.domain.entity.Product;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerProductPreferenceService;
import com.dps.service.CustomerService;
import com.dps.service.ProductService;
import com.dps.web.service.model.AllCpmDTO;

/**
 * This is a controller class that computes the price of a product for a customer and also saves the discount for a customer.
 *
 * @see
 *
 * @Date 19-Nov-2016
 *
 * @author akshay
 */
@Path("/allcpm")
public class CustomerProductMarginController
{
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerProductPreferenceService custProdPrefService;
	
	@Autowired
	private ConfigurationsService configService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{prodCode}")
	public List<AllCpmDTO> getCpmForProduct(@PathParam("prodCode") String productCode)
	{
		Product prod = productService.findByCode(productCode).get(0);
		List<Customer> customers = customerService.findAll();
		Map<Long, BigDecimal> customerDiscountsMap = custProdPrefService.findAllCustomerPreferencesForProduct(prod.getId());
		
		List<AllCpmDTO> discountList = new ArrayList<>();
		
		for(Customer cust : customers)
		{
			AllCpmDTO dto = new AllCpmDTO();
			dto.setShipmark(cust.getShipmark());

			if(customerDiscountsMap.containsKey(cust.getId()))
			{
				dto.setDiscount(customerDiscountsMap.get(cust.getId()));
			}
			else
			{
				dto.setDiscount(Constants.BIG_DECIMAL_ZERO);
			}
			
			discountList.add(dto);
		}
		
		return discountList;
	}
}
