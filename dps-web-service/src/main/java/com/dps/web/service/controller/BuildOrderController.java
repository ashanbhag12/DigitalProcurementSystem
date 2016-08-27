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
import com.dps.domain.constants.CustomerOrderStatus;
import com.dps.domain.entity.Configurations;
import com.dps.domain.entity.Customer;
import com.dps.domain.entity.CustomerOrder;
import com.dps.domain.entity.CustomerOrderDetails;
import com.dps.domain.entity.Product;
import com.dps.domain.entity.Supplier;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerOrderDetailsService;
import com.dps.service.CustomerOrderService;
import com.dps.service.CustomerProductPreferenceService;
import com.dps.service.CustomerService;
import com.dps.service.ProductService;
import com.dps.service.SupplierService;
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
	
	@Autowired
	private CustomerOrderDetailsService custOrderDetService;
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private CustomerOrderService custOrderService;
	
	
	@POST
	@Path("/calculate")
	@Produces(MediaType.APPLICATION_JSON)
	public BuildOrderWrapperDTO calculatePrices(BuildOrderWrapperDTO wrapper) 
	{
		try
		{
			//Get customer information
			List<Customer> custList = customerService.findByShipmarkAndName(wrapper.getCustomerShipmark(), null);
			Customer cust = custList.get(0);
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
			List<Long> longIdList = new ArrayList<>();
			for(BuildOrderDTO item : orderItems)
			{
				idList.add(new JpaEntityId(item.getProductId()));
				longIdList.add(item.getProductId());
			}
			List<Product> prodList = productService.findAll(idList);

			//Store all the products in a map for faster access
			Map<Long, Product> prodMap = new HashMap<>();
			for(Product p : prodList)
			{
				prodMap.put(p.getId(), p);
			}
			
			//Get all the unordered quantity of the products.
			Map<Long, Integer> unorderedProductQuantity = custOrderDetService.getUnorderedQuantityForProducts(longIdList);
			
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
				
				//Check if the MOQ is fulfilled
				Integer unorderedQuantity = unorderedProductQuantity.get(product.getId());
				unorderedQuantity = unorderedQuantity == null ? 0 : unorderedQuantity; 
				unorderedQuantity += item.getQuantity();
				
				if(unorderedQuantity >= product.getMoq())
				{
					item.setIsMoqSatisfied(true);
				}
				
				//Add customer product margin
				item.setCustProdDiscount(custProdMargin);
			}
			
			return wrapper;
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}
	
	@POST
	@Path("/saveorder")
	public void saveCustomerOrder(BuildOrderWrapperDTO wrapper)
	{
		try
		{
			//Get customer information
			Customer cust = customerService.findByShipmarkAndName(wrapper.getCustomerShipmark(), null).get(0);
			
			//Get global constants
			Configurations config = configService.findAll().get(0);
			
			//Get all suppliers
			List<Supplier> suppliersList = supplierService.findAll();
			
			//Store the suppliers in a map for faster access.
			Map<String, Supplier> suppliers = new HashMap<>();
			for(Supplier s : suppliersList)
			{
				suppliers.put(s.getInitials(), s);
			}
			
			List<BuildOrderDTO> orderItems = wrapper.getOrderItems();
			
			//Get all the products that are in the line item
			List<JpaEntityId> idList = new ArrayList<>();
			List<Long> longIdList = new ArrayList<>();
			for(BuildOrderDTO item : orderItems)
			{
				idList.add(new JpaEntityId(item.getProductId()));
				longIdList.add(item.getProductId());
			}
			List<Product> prodList = productService.findAll(idList);

			//Store all the products in a map for faster access
			Map<Long, Product> prodMap = new HashMap<>();
			for(Product p : prodList)
			{
				prodMap.put(p.getId(), p);
			}
			
			//Create parent customer order object and set the parameters.
			CustomerOrder custOrder = new CustomerOrder();
			custOrder.setCustomer(cust);
			custOrder.setCbmRate(config.getPricePerCbm());
			custOrder.setWeightRate(config.getPricePerWeight());
			custOrder.setExchangeRate(config.getExchangeRate());
			custOrder.setOrderDate(wrapper.getOrderDate());
			custOrder.setStatus(CustomerOrderStatus.NO_ITEMS_ORDERED);
			
			//Create new line items for this order
			List<CustomerOrderDetails> lineItems = new ArrayList<>();
			for(BuildOrderDTO item : orderItems)
			{
				CustomerOrderDetails det = new CustomerOrderDetails();
				det.setCustomerOrder(custOrder);
				det.setProduct(prodMap.get(item.getProductId()));
				det.setQuantity(item.getQuantity());
				det.setSupplier(suppliers.get(item.getSelectedSupplierInitials()));
				det.setCbmRate(config.getPricePerCbm());
				det.setWeightRate(config.getPricePerWeight());
				det.setExchangeRate(config.getExchangeRate());
				det.setRemarks(item.getRemarks());
				lineItems.add(det);
			}
			
			custOrder.setLineItems(lineItems);
			
			custOrderService.persist(custOrder);
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}
}