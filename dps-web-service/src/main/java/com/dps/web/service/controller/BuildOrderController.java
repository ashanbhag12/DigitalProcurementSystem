package com.dps.web.service.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.Constants;
import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.Configurations;
import com.dps.domain.entity.Customer;
import com.dps.domain.entity.Product;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerProductPreferenceService;
import com.dps.service.CustomerService;
import com.dps.service.ProductService;
import com.dps.web.service.model.BuildOrderDTO;
import com.dps.web.service.model.BuildOrderWrapperDTO;

/**
 * Handles all the web service requests for  building customer orders.
 *
 * @see
 *
 * @Date 20-Aug-2016
 *
 * @author akshay
 */
@Path("/buildorder")
public class BuildOrderController
{
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerProductPreferenceService custProdPrefService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ConfigurationsService configService;
	
	
	@POST
	@Path("/calculate")
	@Produces(MediaType.APPLICATION_JSON)
	public BuildOrderWrapperDTO calculatePrices(BuildOrderWrapperDTO wrapper) 
	{
		try
		{
			//Get customer information
			Customer cust = customerService.findByShipmarkAndName(wrapper.getCustomerShipmark(), null).get(0);
			BigDecimal custAddMargin = cust.getAdditionalMargin();
			
			//Get customer preferences
			Map<Long, BigDecimal> custProdPrefs = custProdPrefService.findPreferencesForCustomer(cust.getId());
			
			//Get global constants
			Configurations config = configService.findAll().get(0);
			BigDecimal fxrt = config.getExchangeRate();
			BigDecimal cbmrt = config.getPricePerCbm();
			BigDecimal gwrt = config.getPricePerWeight();
			
			List<BuildOrderDTO> orderItems = wrapper.getOrderItems();
			
			//Get all the products that are in the line item
			List<JpaEntityId> idList = new ArrayList<>();
			for(BuildOrderDTO item : orderItems)
			{
				idList.add(new JpaEntityId(item.getProductId()));
			}
			List<Product> prodList = productService.findAll(idList);

			//Store all the products in a map for faster access
			Map<Long, Product> prodMap = new HashMap<>();
			for(Product p : prodList)
			{
				prodMap.put(p.getId(), p);
			}
			
			//Get all the unordered quantity of the products.
			//TODO: Add this functionality here.
			
			//Calculate the price of each item
			for(BuildOrderDTO item : orderItems)
			{
				Product product = prodMap.get(item.getProductId());
				BigDecimal custProdMargin = custProdPrefs.get(item.getProductId()) != null ? custProdPrefs.get(item.getProductId()) : Constants.BIG_DECIMAL_ONE;
				BigDecimal productMargin = prodMap.get(item.getProductId()).getDefaultMargin();
				
				BigDecimal price = product.getPrice();
				price = price.multiply(productMargin);
				price = price.multiply(custProdMargin);
				price = price.multiply(custAddMargin);
				price = price.multiply(fxrt);
				
				BigDecimal cbmPrice = cbmrt;
				cbmPrice = cbmPrice.multiply(product.getCbm());
				
				BigDecimal gwPrice = gwrt;
				gwPrice = gwPrice.multiply(product.getWeight());
				
				BigDecimal unitPrice = price.add(cbmPrice);
				unitPrice = unitPrice.add(gwPrice);
				
				item.setUnitCost(unitPrice);
				
			}
			
			return wrapper;
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}
}