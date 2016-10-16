package com.dps.web.service.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
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
import com.dps.domain.entity.SupplierProductInfo;
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
				
				BigDecimal price = Constants.BIG_DECIMAL_ONE;
				
				for(SupplierProductInfo spi : product.getSuppProdInfo())
				{
					if(StringUtils.equals(spi.getSupplier().getInitials(), item.getSelectedSupplierInitials()))
					{
						price = spi.getSupplierPrice();
						break;
					}
				}
				
				BigDecimal cost = Constants.BIG_DECIMAL_ONE;
				
				cost = cost.multiply(config.getExchangeRate());
				cost = cost.multiply(price);
				
				BigDecimal cost1 = Constants.BIG_DECIMAL_ONE;
				cost1 = cost1.multiply(product.getCbm());
				cost1 = cost1.multiply(config.getPricePerCbm());
				cost1 = cost1.divide(new BigDecimal(product.getCartoonQuantity()), RoundingMode.HALF_UP);
				
				BigDecimal cost2 = Constants.BIG_DECIMAL_ONE;
				cost2 = cost2.multiply(product.getWeight());
				cost2 = cost2.multiply(config.getPricePerWeight());
				cost2 = cost2.divide(new BigDecimal(product.getCartoonQuantity()), RoundingMode.HALF_UP);
				
				cost = cost.add(cost1);
				cost = cost.add(cost2);
				
				cost = cost.multiply(productMargin);
				cost = cost.multiply(custProdMargin);
				cost = cost.multiply(custAddMargin);
				
				cost = cost.multiply(new BigDecimal(product.getCartoonQuantity()));
				
				cost.setScale(3, BigDecimal.ROUND_HALF_UP);
				
				/*price = price.multiply(productMargin);
				price = price.multiply(custProdMargin);
				price = price.multiply(custAddMargin);
				price = price.multiply(fxrt).setScale(3, RoundingMode.HALF_UP);
				
				BigDecimal cbmPrice = cbmrt;
				cbmPrice = cbmPrice.multiply(product.getCbm());
				cbmPrice = cbmPrice.divide(cartoonQuantity, 3, RoundingMode.HALF_UP);
				
				BigDecimal gwPrice = gwrt;
				gwPrice = gwPrice.multiply(product.getWeight());
				gwPrice.divide(cartoonQuantity, 3, RoundingMode.HALF_UP);
				
				BigDecimal unitPrice = price.add(cbmPrice);
				unitPrice = unitPrice.add(gwPrice).setScale(3, RoundingMode.HALF_UP);*/
				
				item.setUnitCost(cost);
				
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
				item.setCustProdDiscountPercent(cust.getDiscountPrcentage());
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
				
				Product prod = prodMap.get(item.getProductId());
				det.setProduct(prod);
				det.setQuantity(item.getQuantity());
				det.setSupplier(suppliers.get(item.getSelectedSupplierInitials()));
				det.setCbmRate(config.getPricePerCbm());
				det.setWeightRate(config.getPricePerWeight());
				det.setExchangeRate(config.getExchangeRate());
				det.setRemarks(item.getRemarks());
				
				for(SupplierProductInfo suppProdInfo : prod.getSuppProdInfo())
				{
					if(suppProdInfo.getSupplier().getInitials().equals(item.getSelectedSupplierInitials()))
					{
						det.setProductPrice(suppProdInfo.getSupplierPrice());
						break;
					}
				}
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