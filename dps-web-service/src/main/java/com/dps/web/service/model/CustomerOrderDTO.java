package com.dps.web.service.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Maps the customer orders.
 *
 * @see
 *
 * @Date 19-Nov-2016
 *
 * @author akshay
 */
public class CustomerOrderDTO
{
	private Long id;
	private String shipmark;
	private Date orderDate;
	private Date orderCompletedDate;
	private String status;
	private BigDecimal additionalCost;
	private String additionalCostDetails;
	private BigDecimal additionalDiscount;
	private String additionalDiscountDetails;
	private List<CustomerOrderDetailsDTO> lineItems = new ArrayList<>();
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getShipmark()
	{
		return shipmark;
	}
	public void setShipmark(String shipmark)
	{
		this.shipmark = shipmark;
	}
	public Date getOrderDate()
	{
		return orderDate;
	}
	public void setOrderDate(Date orderDate)
	{
		this.orderDate = orderDate;
	}
	public Date getOrderCompletedDate()
	{
		return orderCompletedDate;
	}
	public void setOrderCompletedDate(Date orderCompletedDate)
	{
		this.orderCompletedDate = orderCompletedDate;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public List<CustomerOrderDetailsDTO> getLineItems()
	{
		return lineItems;
	}
	public void setLineItems(List<CustomerOrderDetailsDTO> lineItems)
	{
		this.lineItems = lineItems;
	}
	public BigDecimal getAdditionalCost()
	{
		return additionalCost;
	}
	public void setAdditionalCost(BigDecimal additionalCost)
	{
		this.additionalCost = additionalCost;
	}
	public String getAdditionalCostDetails()
	{
		return additionalCostDetails;
	}
	public void setAdditionalCostDetails(String additionalCostDetails)
	{
		this.additionalCostDetails = additionalCostDetails;
	}
	public BigDecimal getAdditionalDiscount()
	{
		return additionalDiscount;
	}
	public void setAdditionalDiscount(BigDecimal additionalDiscount)
	{
		this.additionalDiscount = additionalDiscount;
	}
	public String getAdditionalDiscountDetails()
	{
		return additionalDiscountDetails;
	}
	public void setAdditionalDiscountDetails(String additionalDiscountDetails)
	{
		this.additionalDiscountDetails = additionalDiscountDetails;
	}
}
