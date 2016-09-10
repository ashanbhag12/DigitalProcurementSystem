package com.dps.web.service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.Constants;
import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.Product;
import com.dps.domain.entity.Supplier;
import com.dps.domain.entity.SupplierProductInfo;
import com.dps.service.ProductService;
import com.dps.service.SupplierService;
import com.dps.web.service.model.ProductDTO;
import com.dps.web.service.model.SuppProdInfo;
import com.sun.jersey.multipart.FormDataParam;

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
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Path("/upload")
	public void importFromExcel(@FormDataParam("fileUpload") InputStream uploadedInputStream)
	{
		List<Supplier> suppliersList = supplierService.findAll();
		Map<String, Supplier> supplierMap = new HashMap<>();
		
		for(Supplier supplier : suppliersList)
		{
			supplierMap.put(supplier.getInitials(), supplier);
		}
		
		try
		{
			XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			List<Product> productList = new ArrayList<>();
			
			int rowCount = 0;
			Iterator<Row> rowIterator = sheet.iterator();
			while(rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				
				//Ignore the first row
				if(rowCount++ == 0)
				{
					continue;
				}
				
				Iterator<Cell> cellIterator = row.cellIterator();
				Object[] values = new Object[15];
				int cellCount = 0;
				
				while(cellCount < 15)
				{
					Cell cell = cellIterator.next();
					values[cellCount++] = getCellData(cell);
				}
				
				if(values[0] == null)
				{
					break;
				}
				
				Product product = new Product();
				product.setProductCode(values[0].toString());
				product.setPrice(new BigDecimal(values[1].toString()));
				product.setCartoonQuantity(((Double)values[2]).intValue());
				product.setCbm(new BigDecimal(values[3].toString()));
				product.setWeight(new BigDecimal(values[4].toString()));
				product.setDescription(values[5] != null ? values[5].toString() : "");
				product.setMoq(((Double)values[6]).intValue());
				BigDecimal margin = values[7] != null ? new BigDecimal(values[7].toString()) : Constants.BIG_DECIMAL_ONE;
				product.setDefaultMargin(margin);
				product.setIsValid("Valid".equalsIgnoreCase((String)values[8]) || "Y".equalsIgnoreCase((String)values[8]));
				
				List<SupplierProductInfo> suppProdInfoList = new ArrayList<>();
				
				if(values[9] != null)
				{
					SupplierProductInfo suppProdInfo = new SupplierProductInfo();
					String supplierInitials = values[9].toString();
					suppProdInfo.setSupplier(supplierMap.get(supplierInitials));
					suppProdInfo.setSupplierProductName(values[10].toString());
					suppProdInfo.setProduct(product);
					suppProdInfoList.add(suppProdInfo);
				}
				
				if(values[11] != null)
				{
					SupplierProductInfo suppProdInfo = new SupplierProductInfo();
					String supplierInitials = values[11].toString();
					suppProdInfo.setSupplier(supplierMap.get(supplierInitials));
					suppProdInfo.setSupplierProductName(values[12].toString());
					suppProdInfo.setProduct(product);
					suppProdInfoList.add(suppProdInfo);
				}
				
				if(values[13] != null)
				{
					SupplierProductInfo suppProdInfo = new SupplierProductInfo();
					String supplierInitials = values[13].toString();
					suppProdInfo.setSupplier(supplierMap.get(supplierInitials));
					suppProdInfo.setSupplierProductName(values[14].toString());
					suppProdInfo.setProduct(product);
					suppProdInfoList.add(suppProdInfo);
				}
				
				product.setSuppProdInfo(suppProdInfoList);
				productList.add(product);
			}
			
			productService.persistAll(productList);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}
	
	private Object getCellData(Cell cell)
	{
		switch(cell.getCellType()) 
		{
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_NUMERIC:
				return cell.getNumericCellValue();
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
		}
		return null;
	}
	
	private Product productFromProductDtoAdd(Product prod, ProductDTO prodDto)
	{
		productFromProductDtoCommon(prod, prodDto);
		
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
