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
import com.dps.domain.entity.ContactDetails;
import com.dps.domain.entity.Supplier;
import com.dps.service.SupplierService;
import com.dps.web.service.model.SupplierDTO;

/**
 * Controller class that handles all the web service requests for suppliers crud operations.
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
@Path("/supplier")
public class SupplierController
{
	@Autowired
	private SupplierService supplierService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public SupplierDTO findSupplier(@PathParam("id") long id)
	{
		Supplier supp = supplierService.find(new JpaEntityId(id));
		return supplierToSupplierDto(supp);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find")
	public List<SupplierDTO> findSuppliers(@QueryParam(value = "initials") String initials, @QueryParam(value = "name") String name)
	{
		List<Supplier> suppList = supplierService.findByInitialsAndName(initials, name);
		List<SupplierDTO> suppDtoList = new ArrayList<>();
		
		for(Supplier supp : suppList)
		{
			suppDtoList.add(supplierToSupplierDto(supp));
		}
		
		return suppDtoList;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/initials")
	public List<String> getAllSupplierInitials()
	{
		return supplierService.getAllSupplierInitials();
	}
	
	@POST
	@Path("/add")
	public Response createSupplier(SupplierDTO supplierDto)
	{
		Supplier supplier = new Supplier();
		supplier = supplierFromSupplierDto(supplierDto, supplier);
		try
		{
			supplierService.persist(supplier);
		}
		catch(Exception e)
		{
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.status(Response.Status.CREATED).build();
	}
	
	@POST
	@Path("/modify")
	public Response modifySupplier(SupplierDTO supplierDto)
	{
		Supplier supplier = supplierService.find(new JpaEntityId(supplierDto.getId()));
		supplier = supplierFromSupplierDto(supplierDto, supplier);
		try
		{
			supplierService.merge(supplier);
		}
		catch(Exception e)
		{
			return Response.status(Response.Status.NOT_MODIFIED).build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	@POST
	@Path("/delete/{id}")
	public Response deleteSupplier(@PathParam("id") long id)
	{
		try
		{
			supplierService.remove(new JpaEntityId(id));
		}
		catch(Exception e)
		{
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	/**
	 * Copies the attributes of the specified {@link SupplierDTO} to the {@link Supplier} entity.
	 * @param supplierDto - the DTO object. The data will be copied from this object.
	 * @param supplier - the entity object. Data will be copied to this object.
	 * @return {@link Supplier} updated entity.
	 */
	private Supplier supplierFromSupplierDto(SupplierDTO supplierDto, Supplier supplier)
	{
		supplier.setName(supplierDto.getName());
		supplier.setInitials(supplierDto.getInitials());
		supplier.setActive(true);
		
		ContactDetails contactDetails = supplier.getContactDetails();
		if(contactDetails == null)
		{
			contactDetails = new ContactDetails();
		}
		contactDetails.setEmailId(supplierDto.getEmailId());
		contactDetails.setPhoneNumber(supplierDto.getPhoneNumber());
		
		supplier.setContactDetails(contactDetails);
		return supplier;
	}
	
	private SupplierDTO supplierToSupplierDto(Supplier supp)
	{
		SupplierDTO suppDto = new SupplierDTO();
		
		suppDto.setId(supp.getId());
		suppDto.setEmailId(supp.getContactDetails().getEmailId());
		suppDto.setInitials(supp.getInitials());
		suppDto.setName(supp.getName());
		suppDto.setPhoneNumber(supp.getContactDetails().getPhoneNumber());
		
		return suppDto;
	}
}
