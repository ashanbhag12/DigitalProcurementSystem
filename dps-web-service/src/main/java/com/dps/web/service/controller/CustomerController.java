package com.dps.web.service.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.Address;
import com.dps.domain.entity.ContactDetails;
import com.dps.domain.entity.Customer;
import com.dps.service.CustomerService;
import com.dps.web.service.model.CustomerDTO;

/**
 * Controller class that handles all the web service requests for customer crud operations.
 *
 * @see
 *
 * @Date Jul 23, 2016
 *
 * @author akshay
 */
@Path("/customer")
public class CustomerController
{
	@Autowired
	private CustomerService customerService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public CustomerDTO findCustomer(@PathParam("id") long id)
	{
		Customer cust = customerService.find(new JpaEntityId(id));
		return customerToCustomerDTO(cust);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find")
	public List<CustomerDTO> findCustomers(@QueryParam(value = "shipmark") String shipmark, @QueryParam(value = "name") String name)
	{
		List<Customer> custList = customerService.findByShipmarkAndName(shipmark,name);
		List<CustomerDTO> custDtoList = new ArrayList<>();
		for(Customer cust : custList)
		{
			custDtoList.add(customerToCustomerDTO(cust));
		}
		
		return custDtoList;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/shipmark")
	public List<String> getAllCustomerShipmarks()
	{
		return customerService.getAllCustomerShipmarks();
	}
	
	@POST
	@Path("/add")
	public Response createCustomer(CustomerDTO custDto)
	{
		Customer cust = new Customer();
		cust = customerFromCustomerDTO(cust, custDto);
		try
		{
			customerService.persist(cust);
		}
		catch(Exception e)
		{
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.status(Response.Status.CREATED).build();
	}
	
	@POST
	@Path("/modify")
	public Response modifyCustomer(CustomerDTO custDto)
	{
		Customer cust = customerService.find(new JpaEntityId(custDto.getId()));
		cust = customerFromCustomerDTO(cust, custDto);
		try
		{
			customerService.merge(cust);
		}
		catch(Exception e)
		{
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/delete/{id}")
	public Response deleteCustomer(@PathParam("id") long id)
	{
		try
		{
			customerService.remove(new JpaEntityId(id));
		}
		catch(Exception e)
		{
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Copies attributes from the specified {@link CustomerDTO} object to the {@link Customer} entity objet.
	 * @param cust - the entity object. Data will be copied to this object.
	 * @param custDto - the DTO object. The data will be copied from this object.
	 * @return {@link Customer} updated entity.
	 */
	private Customer customerFromCustomerDTO(Customer cust, CustomerDTO custDto)
	{
		cust.setAdditionalMargin(custDto.getAdditionalMargin());
		cust.setDiscountPrcentage(custDto.getAdditionalMarginPercentage());
		cust.setName(custDto.getName());
		cust.setShipmark(custDto.getShipmark());
		cust.setOriginalShipmark(custDto.getOriginalShipmark());
		cust.setActive(true);
		
		Address address = cust.getAddress();
		if(address == null)
		{
			address = new Address();
		}
		address.setBuilding(custDto.getBuilding());
		address.setCity(custDto.getCity());
		address.setFlatNo(custDto.getFlatNo());
		address.setLocality(custDto.getLocality());
		address.setState(custDto.getState());
		address.setStreet(custDto.getStreet());
		address.setZip(custDto.getZip());
		cust.setAddress(address);
		
		ContactDetails contactDetails = cust.getContactDetails();
		if(contactDetails == null)
		{
			contactDetails = new ContactDetails();
		}
		contactDetails.setEmailId(custDto.getEmailId());
		contactDetails.setPhoneNumber(custDto.getPhoneNumber());
		cust.setContactDetails(contactDetails);
		
		return cust;
	}
	
	private CustomerDTO customerToCustomerDTO(Customer cust)
	{
		CustomerDTO custDto = new CustomerDTO();
		custDto.setId(cust.getId());
		custDto.setAdditionalMargin(cust.getAdditionalMargin());
		custDto.setAdditionalMarginPercentage(cust.getDiscountPrcentage());
		custDto.setBuilding(cust.getAddress().getBuilding());
		custDto.setCity(cust.getAddress().getCity());
		custDto.setEmailId(cust.getContactDetails().getEmailId());
		custDto.setFlatNo(cust.getAddress().getFlatNo());
		custDto.setLocality(cust.getAddress().getLocality());
		custDto.setName(cust.getName());
		custDto.setPhoneNumber(cust.getContactDetails().getPhoneNumber());
		custDto.setShipmark(cust.getShipmark());
		custDto.setOriginalShipmark(cust.getOriginalShipmark());
		custDto.setState(cust.getAddress().getState());
		custDto.setStreet(cust.getAddress().getStreet());
		custDto.setZip(cust.getAddress().getZip());
		
		return custDto;
	}
}
