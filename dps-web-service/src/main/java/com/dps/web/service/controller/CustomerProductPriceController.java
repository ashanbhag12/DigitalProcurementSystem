package com.dps.web.service.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.Constants;
import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.Customer;
import com.dps.domain.entity.CustomerProductPreference;
import com.dps.domain.entity.Product;
import com.dps.service.CustomerProductPreferenceService;
import com.dps.service.CustomerService;
import com.dps.service.ProductService;
import com.dps.web.service.model.CustomerProductPricesDTO;
import com.dps.web.service.model.CustomerProductPricesWrapperDTO;

/**
 * This is a controller class that computes the price of a product for a customer and also saves the discount for a customer.
 *
 * @see
 *
 * @Date 15-Aug-2016
 *
 * @author akshay
 */
@Path("/custprodpref")
public class CustomerProductPriceController
{
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerProductPreferenceService custProdPrefService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{shipmark}")
	public CustomerProductPricesWrapperDTO findProduct(@PathParam("shipmark") String shipmark)
	{
		List<CustomerProductPricesDTO> custPrices = new ArrayList<>();
		List<Product> prodList = new ArrayList<>();
		Map<Long, BigDecimal> custProdPrefs = new HashMap<>();
		Customer cust = null;
		try
		{
			List<Customer> custList = customerService.findByShipmarkAndName(shipmark, null);
			cust = custList.get(0);
			prodList = productService.findAll();
			custProdPrefs = custProdPrefService.findPreferencesForCustomer(cust.getId());
		}
		catch(Exception e)
		{
			
		}
		
		for(Product prod : prodList)
		{
			CustomerProductPricesDTO custProdPriceDto = new CustomerProductPricesDTO();
			custProdPriceDto.setProductCode(prod.getProductCode());
			custProdPriceDto.setCustomerProductMargin(custProdPrefs.get(prod.getId()) != null ? custProdPrefs.get(prod.getId()) : Constants.BIG_DECIMAL_ONE);
			custProdPriceDto.setProductMargin(prod.getDefaultMargin());
			custProdPriceDto.setProductPrice(prod.getPrice());
			custProdPriceDto.setCustomerMargin(cust.getAdditionalMargin());
			
			BigDecimal cost = Constants.BIG_DECIMAL_ONE;
			cost = cost.multiply(custProdPriceDto.getCustomerProductMargin());
			cost = cost.multiply(cust.getAdditionalMargin());
			cost = cost.multiply(custProdPriceDto.getProductMargin());
			cost = cost.multiply(prod.getPrice());
			cost.setScale(2, BigDecimal.ROUND_HALF_UP);
			custProdPriceDto.setCost(cost);
			
			custPrices.add(custProdPriceDto);
		}
		
		CustomerProductPricesWrapperDTO custProdPriceWrapper = new CustomerProductPricesWrapperDTO();
		custProdPriceWrapper.setAdditionalCustomerMargin(cust.getAdditionalMargin());
		custProdPriceWrapper.setShipmark(shipmark);
		custProdPriceWrapper.setCustomerProductPrices(custPrices);
		
		return custProdPriceWrapper;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/modify")
	public void savePreferences(CustomerProductPricesWrapperDTO wrapper)
	{
		try
		{
			//Get the customer
			List<Customer> custList = customerService.findByShipmarkAndName(wrapper.getShipmark(), null);
			Customer cust = custList.get(0);
			
			//Get all the preferences for that customer.
			List<CustomerProductPreference> customerPreferences = custProdPrefService.findAllPreferencesForCustomer(cust.getId());
			
			//Save fetched preferences in a map for ease of future access
			Map<String, CustomerProductPreference> custProdPrefObj = new HashMap<>();
			if(customerPreferences != null)
			{
				for(CustomerProductPreference pref : customerPreferences)
				{
					custProdPrefObj.put(pref.getProduct().getProductCode(), pref);
				}
			}
			
			//These will be merged to the database.
			List<CustomerProductPreference> customerUpdatedPreferences = new ArrayList<>();
			List<JpaEntityId> toDeletePreferences = new ArrayList<>();
			
			for(CustomerProductPricesDTO custProdPrice : wrapper.getCustomerProductPrices())
			{
				if(!Constants.BIG_DECIMAL_ONE.equals(custProdPrice.getCustomerProductMargin()))
				{
					BigDecimal existingDiscount = Constants.BIG_DECIMAL_ONE; 
					CustomerProductPreference existingPreference = custProdPrefObj.get(custProdPrice.getProductCode());
					if(existingPreference != null)
					{
						existingDiscount = existingPreference.getDiscount();
					}
					
					if(!ObjectUtils.equals(existingDiscount, custProdPrice.getCustomerProductMargin()))
					{
						CustomerProductPreference pref = custProdPrefObj.get(custProdPrice.getProductCode());
						
						if(pref == null)
						{
							pref = new CustomerProductPreference();
							pref.setCustomer(cust);
							Product product = productService.findByCode(custProdPrice.getProductCode()).get(0);
							pref.setProduct(product);
						}
						pref.setDiscount(custProdPrice.getCustomerProductMargin());
						
						customerUpdatedPreferences.add(pref);
					}
				}
				else
				{
					CustomerProductPreference existingPreference = custProdPrefObj.get(custProdPrice.getProductCode());
					if(existingPreference != null)
					{
						toDeletePreferences.add(new JpaEntityId(existingPreference.getId()));
					}
				}
			}
			
			custProdPrefService.mergeAll(customerUpdatedPreferences);
			custProdPrefService.removeAll(toDeletePreferences);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
