package com.dps.web.service.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.constants.CustomerOrderDetailStatus;
import com.dps.domain.constants.CustomerOrderStatus;
import com.dps.domain.constants.SupplierOrderDetailStatus;
import com.dps.domain.constants.SupplierOrderStatus;
import com.dps.domain.entity.CustomerOrder;
import com.dps.domain.entity.CustomerOrderDetails;
import com.dps.domain.entity.SupplierOrder;
import com.dps.domain.entity.SupplierOrderDetails;
import com.dps.service.CustomerOrderService;
import com.dps.service.SupplierOrderService;
import com.dps.web.service.model.SupplierOrderDTO;
import com.dps.web.service.model.SupplierOrderDetailsDTO;

/**
 * Controller class that handles all the web service requests for updating supplier orders operations.
 *
 * @see
 *
 * @Date Oct 16, 2016
 *
 * @author akshay
 */
@Path("/updatesupplierorder")
public class UpdateSupplierOrderController
{
	@Autowired
	private SupplierOrderService supplierOrderService;
	
	@Autowired
	private CustomerOrderService customerOrderService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<SupplierOrderDTO> getIncompleteOrders()
	{
		List<SupplierOrder> supplierOrders = supplierOrderService.getUncompletedOrders();
		Collections.sort(supplierOrders, new Comparator<SupplierOrder>(){

			@Override
			public int compare(SupplierOrder o1, SupplierOrder o2)
			{
				return o1.getOrderDate().compareTo(o2.getOrderDate());
			}
			
		});
		
		List<SupplierOrderDTO> supplierOrderList = new ArrayList<>();
		for(SupplierOrder supplierOrder : supplierOrders)
		{
			//Add the supplier level details.
			SupplierOrderDTO suppOrderDto = new SupplierOrderDTO();
			suppOrderDto.setId(supplierOrder.getId());
			suppOrderDto.setOrderDate(supplierOrder.getOrderDate());
			suppOrderDto.setStatus(supplierOrder.getStatus().toString());
			suppOrderDto.setSupplierInitials(supplierOrder.getSupplier().getInitials());
			
			List<SupplierOrderDetails> suppOrderDetails = supplierOrder.getSupplierOrderDetails();
			
			//Iterate through the orders and create the supplier details object.
			List<SupplierOrderDetailsDTO> suppOrderDetailsDtoList = new ArrayList<>(); 
			for(SupplierOrderDetails suppOrderDetail : suppOrderDetails)
			{
				SupplierOrderDetailsDTO suppOrderDet = new SupplierOrderDetailsDTO();
				suppOrderDet.setCustomerInitials(suppOrderDetail.getCustomerOrderDetails().getCustomerOrder().getCustomer().getShipmark());
				suppOrderDet.setOrderedQuantity(suppOrderDetail.getCustomerOrderDetails().getQuantity());
				suppOrderDet.setPrevoiuslyReceivedQuantity(suppOrderDetail.getCustomerOrderDetails().getReceivedQuantity() == null ? 0 : suppOrderDetail.getCustomerOrderDetails().getReceivedQuantity());
				suppOrderDet.setPendingQuantity(suppOrderDet.getOrderedQuantity() - suppOrderDet.getPrevoiuslyReceivedQuantity());
				if(suppOrderDet.getPendingQuantity() == 0)
				{
					continue;
				}
				suppOrderDet.setProductCode(suppOrderDetail.getCustomerOrderDetails().getProduct().getProductCode());
				suppOrderDet.setRemarks(suppOrderDetail.getCustomerOrderDetails().getRemarks());
				suppOrderDet.setId(suppOrderDetail.getId());
				
				suppOrderDetailsDtoList.add(suppOrderDet);
			}
			suppOrderDto.setOrderDetails(suppOrderDetailsDtoList);
			supplierOrderList.add(suppOrderDto);
		}
		
		return supplierOrderList;
	}
	
	@POST
	@Path("/update")
	public void updateOrders(List<SupplierOrderDTO> suppOrderDtoList)
	{
		List<JpaEntityId> idList = new ArrayList<>();
		Date today = new Date();
		for(SupplierOrderDTO suppOrderDto : suppOrderDtoList)
		{
			idList.add(new JpaEntityId(suppOrderDto.getId()));
		}
		
		List<SupplierOrder> suppOrderList = supplierOrderService.findAll(idList);
		Map<Long, SupplierOrder> suppOrderMap = new HashMap<>();
		Set<CustomerOrder> custOrderSet = new HashSet<>();
		
		for(SupplierOrder suppOrder : suppOrderList)
		{
			suppOrderMap.put(suppOrder.getId(), suppOrder);
		}
		
		//Update the received quantity
		for(SupplierOrderDTO suppOrderDto : suppOrderDtoList)
		{
			SupplierOrder suppOrder = suppOrderMap.get(suppOrderDto.getId());
			
			Map<Long, SupplierOrderDetails> suppOrderDetMap = new HashMap<>();
			for(SupplierOrderDetails suppOrderDet : suppOrder.getSupplierOrderDetails())
			{
				suppOrderDetMap.put(suppOrderDet.getId(), suppOrderDet);
			}
			
			for(SupplierOrderDetailsDTO suppOrderDetDto : suppOrderDto.getOrderDetails())
			{
				if(suppOrderDetDto.getReceivedQuantity() != null && suppOrderDetDto.getReceivedQuantity() > 0)
				{
					SupplierOrderDetails suppOrderDet = suppOrderDetMap.get(suppOrderDetDto.getId());
					int receivedQuantity = suppOrderDet.getCustomerOrderDetails().getReceivedQuantity() == null ? 0 : suppOrderDet.getCustomerOrderDetails().getReceivedQuantity();
					receivedQuantity += suppOrderDetDto.getReceivedQuantity();
					suppOrderDet.getCustomerOrderDetails().setReceivedQuantity(receivedQuantity);
					suppOrderDet.getCustomerOrderDetails().setLastReceivedQuantity(suppOrderDetDto.getReceivedQuantity());
					suppOrderDet.getCustomerOrderDetails().setOrderReceivedDate(today);
					
					if(suppOrderDet.getCustomerOrderDetails().getReceivedQuantity() == suppOrderDet.getCustomerOrderDetails().getQuantity())
					{
						suppOrderDet.getCustomerOrderDetails().setStatus(CustomerOrderDetailStatus.ORDER_COMPLETED);
						suppOrderDet.setStatus(SupplierOrderDetailStatus.ITEM_COMPLETELY_RECEIVED);
					}
					else
					{
						suppOrderDet.getCustomerOrderDetails().setStatus(CustomerOrderDetailStatus.ORDER_PARTIALLY_COMPLETED);
						suppOrderDet.setStatus(SupplierOrderDetailStatus.ITEM_PARTIALLY_RECEIVED);
					}
					custOrderSet.add(suppOrderDet.getCustomerOrderDetails().getCustomerOrder());
				}
			}
		}
		
		for(CustomerOrder custOrder : custOrderSet)
		{
			int completedCount = 0;
			for(CustomerOrderDetails custOrderDet : custOrder.getLineItems())
			{
				if(custOrderDet.getStatus() == CustomerOrderDetailStatus.ORDER_COMPLETED)
				{
					completedCount ++;
				}
				if(custOrderDet.getOrderReceivedDate() != today)
				{
					custOrderDet.setLastReceivedQuantity(0);
				}
			}
			
			if(completedCount == custOrder.getLineItems().size())
			{
				custOrder.setStatus(CustomerOrderStatus.COMPLETED);
				custOrder.setOrderCompletedDate(new Date());
			}
		}
		
		for(SupplierOrder suppOrder1 : suppOrderList)
		{
			int completedCount = 0;
			for(SupplierOrderDetails suppOrderDet : suppOrder1.getSupplierOrderDetails())
			{
				if(suppOrderDet.getStatus() == SupplierOrderDetailStatus.ITEM_COMPLETELY_RECEIVED)
				{
					completedCount ++;
				}
			}
			if(completedCount == suppOrder1.getSupplierOrderDetails().size())
			{
				suppOrder1.setStatus(SupplierOrderStatus.ORDER_COMPLETED);
				suppOrder1.setOrderCompletedDate(new Date());
			}
			else
			{
				suppOrder1.setStatus(SupplierOrderStatus.ORDER_PARTIALLY_COMPLETED);
			}
		}
		
		customerOrderService.mergeAll(custOrderSet);
		supplierOrderService.mergeAll(suppOrderList);
	}
}
