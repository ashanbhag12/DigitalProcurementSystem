package com.dps.web.service.model;

import java.util.Date;
import java.util.List;

/**
 * This class maps to the Customer order page on the UI.
 *
 * @see
 *
 * @date 07-Aug-2016
 *
 * @author akshay
 */
public class CustomerOrderDTO
{
	private String customerShipmark;
	private Date orderDate;
	
	private List<CustOrderLineItemsDTO> lineItems;
	
}