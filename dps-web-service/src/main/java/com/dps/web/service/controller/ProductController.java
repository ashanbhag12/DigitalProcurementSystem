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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.Product;
import com.dps.domain.entity.Supplier;
import com.dps.domain.entity.SupplierProductInfo;
import com.dps.service.ProductService;
import com.dps.service.SupplierService;
import com.dps.web.service.model.ProductDTO;
import com.dps.web.service.model.SuppProdInfo;

/**
 * Controller class that handles all the web service requests for product crud operations.
 *
 * @see
 *
 * @Date Jul 30, 2016
 *
 * @author akshay
 */
@Path("/product")
public class ProductController
{
	@Autowired
	private ProductService productService;
	
	@Autowired
	private SupplierService supplierService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public ProductDTO findProduct(@PathParam("id") long id)
	{
		Product product = productService.find(new JpaEntityId(id));
		return productToProductDto(product);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/find")
	public List<ProductDTO> findProducts(@QueryParam(value = "code") String code)
	{
		List<Product> prodList = productService.findByCode(code);
		List<ProductDTO> prodDtoList = new ArrayList<>();
		for(Product prod : prodList)
		{
			prodDtoList.add(productToProductDto(prod));
		}
		
		return prodDtoList;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/codes")
	public List<String> getAllProductCodes()
	{
		return productService.getAllProductCodes();
	}
	
	@POST
	@Path("/add")
	public Response createProduct(ProductDTO prodDto)
	{
		Product prod = new Product();
		try
		{
			prod = productFromProductDtoAdd(prod, prodDto);
		}
		catch(Exception e)
		{
			throw e;
			//return Response.status(Response.Status.BAD_REQUEST).build();
		}
		try
		{
			productService.persist(prod);
		}
		catch(Exception e)
		{
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.status(Response.Status.CREATED).build();
	}
	
	@POST
	@Path("/modify")
	public Response modifyProduct(ProductDTO prodDto)
	{
		Product prod = productService.find(new JpaEntityId(prodDto.getId()));
		prod = productFromProductDtoUpdate(prod, prodDto);
		try
		{
			productService.merge(prod);
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
			productService.remove(new JpaEntityId(id));
		}
		catch(Exception e)
		{
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	private Product productFromProductDtoAdd(Product prod, ProductDTO prodDto)
	{
		productFromProductDtoCommon(prod, prodDto);
		
		for(SuppProdInfo spii : prodDto.getSupplierProductInfoList())
		{
			SupplierProductInfo spi = new SupplierProductInfo();
			spi.setProduct(prod);
			spi.setSupplierProductName(spii.getSupplierProductCode());
			
			List<Supplier> supplierList = supplierService.findByInitialsAndName(spii.getSupplierInitials(), null);
			if(supplierList == null || supplierList.size() == 0)
			{
				throw new RuntimeException("Invalid Supplier");
			}
			spi.setSupplier(supplierList.get(0));
			
			prod.getSuppProdInfo().add(spi);
		}
		
		return prod;
	}
	
	private Product productFromProductDtoUpdate(Product prod, ProductDTO prodDto)
	{
		productFromProductDtoCommon(prod, prodDto);
		
		prod.getSuppProdInfo().clear();
		
		for(SuppProdInfo spii : prodDto.getSupplierProductInfoList())
		{
			if(StringUtils.isBlank(spii.getSupplierInitials()))
			{
				continue;
			}
			SupplierProductInfo spi = new SupplierProductInfo();
			spi.setProduct(prod);
			spi.setSupplierProductName(spii.getSupplierProductCode());
			
			List<Supplier> supplierList = supplierService.findByInitialsAndName(spii.getSupplierInitials(), null);
			if(supplierList == null || supplierList.size() == 0)
			{
				throw new RuntimeException("Invalid Supplier");
			}
			spi.setSupplier(supplierList.get(0));
			
			prod.getSuppProdInfo().add(spi);
		}
		
		return prod;
	}
	
	private Product productFromProductDtoCommon(Product prod, ProductDTO prodDto)
	{
		prod.setCartoonQuantity(prodDto.getCartoonQuantity());
		prod.setCbm(prodDto.getCbm());
		prod.setDefaultMargin(prodDto.getDefaultMargin());
		prod.setDescription(prodDto.getDescription());
		prod.setIsValid(prodDto.getIsValid());
		prod.setMoq(prodDto.getMoq());
		prod.setPrice(prodDto.getPrice());
		prod.setProductCode(prodDto.getProductCode());
		prod.setWeight(prodDto.getWeight());
		
		return prod;
	}
	
	private ProductDTO productToProductDto(Product prod)
	{
		ProductDTO prodDto = new ProductDTO();
		
		prodDto.setCartoonQuantity(prod.getCartoonQuantity());
		prodDto.setCbm(prod.getCbm());
		prodDto.setDefaultMargin(prod.getDefaultMargin());
		prodDto.setDescription(prod.getDescription());
		prodDto.setId(prod.getId());
		prodDto.setIsValid(prod.getIsValid());
		prodDto.setMoq(prod.getMoq());
		prodDto.setPrice(prod.getPrice());
		prodDto.setProductCode(prod.getProductCode());
		prodDto.setWeight(prod.getWeight());
		List<SupplierProductInfo> list = prod.getSuppProdInfo();
		for(SupplierProductInfo spi : list)
		{
			SuppProdInfo spii = new SuppProdInfo();
			spii.setSupplierInitials(spi.getSupplier().getInitials());
			spii.setProductCode(prod.getProductCode());
			spii.setSupplierProductCode(spi.getSupplierProductName());
			prodDto.getSupplierProductInfoList().add(spii);
		}
		return prodDto;
	}
}
