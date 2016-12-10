package com.dps.web.service.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.constants.CustomerOrderDetailStatus;
import com.dps.domain.constants.CustomerOrderStatus;
import com.dps.domain.entity.CustomerOrder;
import com.dps.domain.entity.CustomerOrderDetails;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerOrderService;
import com.dps.web.service.model.CustomerOrderDTO;
import com.dps.web.service.model.CustomerOrderDetailsDTO;

/**
 * Controller class that handles all the web service requests for updating customer orders operations.
 *
 * @see
 *
 * @Date 19-Nov-2016
 *
 * @author akshay
 */
@Path("/customerorder")
public class CustomerOrderController
{
	@Autowired
	private CustomerOrderService custOrderService;
	
	@Autowired
	private ConfigurationsService configService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{shipmark}/{startDate}/{endDate}")
	public List<CustomerOrderDTO> getCustomerOrders(@PathParam("shipmark") String shipmark, @PathParam("startDate") Long startDate, @PathParam("endDate") Long endDate)
	{
		List<CustomerOrder> custOrders = custOrderService.getCustomerOrders(shipmark, new Date(startDate), new Date(endDate));
		List<CustomerOrderDTO> custOrdersList = new ArrayList<>();
		
		for(CustomerOrder custOrder : custOrders)
		{
			custOrdersList.add(convertToDto(custOrder));
		}
		
		return custOrdersList;
	}
	
	@POST
	@Path("/cancel")
	public void cancelOrders(CustomerOrderDTO custOrderDto)
	{
		CustomerOrder custOrder = custOrderService.find(new JpaEntityId(custOrderDto.getId()));
		List<CustomerOrderDetails> custOrderDetails = custOrder.getLineItems();
		Map<Long, CustomerOrderDetails> custOrdersMap = new HashMap<>();
		
		for(CustomerOrderDetails custOrderDet : custOrderDetails)
		{
			custOrdersMap.put(custOrderDet.getId(), custOrderDet);
		}
		
		List<CustomerOrderDetailsDTO> lineItemsDto = custOrderDto.getLineItems();
		int completedOrder = 0;
		for(CustomerOrderDetailsDTO lineItem : lineItemsDto)
		{
			CustomerOrderDetails custOrderDet = custOrdersMap.get(lineItem.getId());
			custOrderDet.setStatus(CustomerOrderDetailStatus.CANCELLED);
			
			if(custOrderDet.getStatus() == CustomerOrderDetailStatus.CANCELLED || custOrderDet.getStatus() == CustomerOrderDetailStatus.CANCELLED)
			{
				completedOrder++;
			}
		}
		
		if(completedOrder == lineItemsDto.size())
		{
			custOrder.setStatus(CustomerOrderStatus.COMPLETED);
		}
		
		custOrder = updateAdditionalDetails(custOrderDto, custOrder);
		
		custOrderService.merge(custOrder);
	}
	
	@POST
	@Path("/update")
	public void updateOrders(CustomerOrderDTO custOrderDto)
	{
		CustomerOrder custOrder = custOrderService.find(new JpaEntityId(custOrderDto.getId()));
		List<CustomerOrderDetails> custOrderDetails = custOrder.getLineItems();
		Map<Long, CustomerOrderDetails> custOrdersMap = new HashMap<>();
		
		for(CustomerOrderDetails custOrderDet : custOrderDetails)
		{
			custOrdersMap.put(custOrderDet.getId(), custOrderDet);
		}
		
		List<CustomerOrderDetailsDTO> lineItemsDto = custOrderDto.getLineItems();
		for(CustomerOrderDetailsDTO lineItem : lineItemsDto)
		{
			CustomerOrderDetails custOrderDet = custOrdersMap.get(lineItem.getId());
			custOrderDet.setProductPrice(lineItem.getProductPrice());
		}
		
		custOrder = updateAdditionalDetails(custOrderDto, custOrder);
		
		custOrderService.merge(custOrder);
	}
	
	@POST
	@Path("/invoice")
	public void generateInvoice(CustomerOrderDTO custOrderDto)
	{
		
	}
	
	private CustomerOrder updateAdditionalDetails(CustomerOrderDTO dto, CustomerOrder order)
	{
		order.setAdditionalCost(dto.getAdditionalCost());
		order.setAdditionalCostDetails(dto.getAdditionalCostDetails());
		order.setAdditionalDiscount(dto.getAdditionalDiscount());
		order.setAdditionalDiscountDetails(dto.getAdditionalDiscountDetails());
		return order;
	}

	private CustomerOrderDTO convertToDto(CustomerOrder custOrder)
	{
		CustomerOrderDTO order = new CustomerOrderDTO();
		order.setId(custOrder.getId());
		order.setOrderCompletedDate(custOrder.getOrderCompletedDate());
		order.setOrderDate(custOrder.getOrderDate());
		order.setShipmark(custOrder.getCustomer().getShipmark());
		order.setStatus(custOrder.getStatus().toString());
		order.setAdditionalCost(custOrder.getAdditionalCost());
		order.setAdditionalCostDetails(custOrder.getAdditionalCostDetails());
		order.setAdditionalDiscount(custOrder.getAdditionalDiscount());
		order.setAdditionalDiscountDetails(custOrder.getAdditionalDiscountDetails());
		
		List<CustomerOrderDetails> lineItems = custOrder.getLineItems();
		List<CustomerOrderDetailsDTO> lineItemsDtoList = new ArrayList<>();
		
		for(CustomerOrderDetails lineItem : lineItems)
		{
			CustomerOrderDetailsDTO lineItemDto = new CustomerOrderDetailsDTO();
			lineItemDto.setOrderPlacedDate(lineItem.getOrderPlacedDate());
			lineItemDto.setOrderReceivedDate(lineItem.getOrderReceivedDate());
			lineItemDto.setProductCode(lineItem.getProduct().getProductCode());
			lineItemDto.setProductPrice(lineItem.getProductPrice());
			lineItemDto.setQuantity(lineItem.getQuantity());
			lineItemDto.setReceivedQuantity(lineItem.getReceivedQuantity());
			lineItemDto.setRemarks(lineItem.getRemarks());
			lineItemDto.setStatus(lineItem.getStatus().toString());
			lineItemDto.setSupplierInitials(lineItem.getSupplier().getInitials());
			lineItemDto.setId(lineItem.getId());
			
			lineItemsDtoList.add(lineItemDto);
		}
		
		order.setLineItems(lineItemsDtoList);
		
		return order;
	}
}
