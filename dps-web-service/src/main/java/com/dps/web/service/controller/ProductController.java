package com.dps.web.service.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.EnumMap;
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
			Map<Integer, UploadFields> uploadFieldsMap = new HashMap<>();
			while(rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				
				//The first row
				Iterator<Cell> cellIterator = row.cellIterator();
				int cellCount = -1;
				if(rowCount++ == 0)
				{
					while(++cellCount < UploadFields.values().length)
					{
						UploadFields uf = UploadFields.parse(getCellData(cellIterator.next()).toString());
						if(uf == null)
						{
							throw new RuntimeException("Upload fields error");
						}
						uploadFieldsMap.put(cellCount, uf);
					}
				}
				else
				{
					EnumMap<UploadFields, Object> values = getRowData(cellIterator, uploadFieldsMap);
					
					if(values.get(UploadFields.PRODUCT_CODE) == null)
					{
						break;
					}
					
					Product product = new Product();
					product.setProductCode(values.get(UploadFields.PRODUCT_CODE).toString());
					product.setCartoonQuantity(((Double)values.get(UploadFields.CARTOON_QUANTITY)).intValue());
					product.setCbm(new BigDecimal(values.get(UploadFields.CBM).toString()).setScale(3, RoundingMode.HALF_UP));
					product.setWeight(new BigDecimal(values.get(UploadFields.WEIGHT).toString()).setScale(3, RoundingMode.HALF_UP));
					product.setDescription(values.get(UploadFields.DESCRIPTION) != null ? values.get(UploadFields.DESCRIPTION).toString() : "");
					product.setMoq(((Double)values.get(UploadFields.MOQ)).intValue());
					BigDecimal margin = values.get(UploadFields.DEFAULT_MARGIN) != null ? new BigDecimal(values.get(UploadFields.DEFAULT_MARGIN).toString()) : Constants.BIG_DECIMAL_ONE;
					product.setDefaultMargin(margin);
					product.setIsValid("Valid".equalsIgnoreCase((String)values.get(UploadFields.IS_VALID)) || "Y".equalsIgnoreCase((String)values.get(UploadFields.IS_VALID)));
					product.setIsValid(Boolean.TRUE);
					
					List<SupplierProductInfo> suppProdInfoList = new ArrayList<>();
					
					if(values.get(UploadFields.SUPPLIER_1_INITIALS) != null)
					{
						SupplierProductInfo suppProdInfo = new SupplierProductInfo();
						String supplierInitials = values.get(UploadFields.SUPPLIER_1_INITIALS).toString();
						suppProdInfo.setSupplier(supplierMap.get(supplierInitials));
						suppProdInfo.setSupplierProductName(values.get(UploadFields.SUPPLIER_1_CODE).toString());
						suppProdInfo.setSupplierPrice(new BigDecimal(values.get(UploadFields.SUPPLIER_1_PRICE).toString()).setScale(3, RoundingMode.HALF_UP));
						suppProdInfo.setProduct(product);
						suppProdInfoList.add(suppProdInfo);
					}
					
					if(values.get(UploadFields.SUPPLIER_2_INITIALS) != null)
					{
						SupplierProductInfo suppProdInfo = new SupplierProductInfo();
						String supplierInitials = values.get(UploadFields.SUPPLIER_2_INITIALS).toString();
						suppProdInfo.setSupplier(supplierMap.get(supplierInitials));
						suppProdInfo.setSupplierProductName(values.get(UploadFields.SUPPLIER_2_CODE).toString());
						suppProdInfo.setSupplierPrice(new BigDecimal(values.get(UploadFields.SUPPLIER_2_PRICE).toString()).setScale(3, RoundingMode.HALF_UP));
						suppProdInfo.setProduct(product);
						suppProdInfoList.add(suppProdInfo);
					}
					
					if(values.get(UploadFields.SUPPLIER_3_INITIALS) != null)
					{
						SupplierProductInfo suppProdInfo = new SupplierProductInfo();
						String supplierInitials = values.get(UploadFields.SUPPLIER_3_INITIALS).toString();
						suppProdInfo.setSupplier(supplierMap.get(supplierInitials));
						suppProdInfo.setSupplierProductName(values.get(UploadFields.SUPPLIER_3_CODE).toString());
						suppProdInfo.setSupplierPrice(new BigDecimal(values.get(UploadFields.SUPPLIER_3_PRICE).toString()).setScale(3, RoundingMode.HALF_UP));
						suppProdInfo.setProduct(product);
						suppProdInfoList.add(suppProdInfo);
					}
					
					product.setSuppProdInfo(suppProdInfoList);
					productList.add(product);
				}
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
	
	private EnumMap<UploadFields, Object> getRowData(Iterator<Cell> cellIterator, Map<Integer, UploadFields> uploadFieldsMap)
	{
		EnumMap<UploadFields, Object> map = new EnumMap<>(UploadFields.class);
		
		int cellCount = 0;
		while(cellCount < UploadFields.values().length)
		{
			Cell cell = cellIterator.next();
			UploadFields uf = uploadFieldsMap.get(cellCount);
			map.put(uf, getCellData(cell));
		}
		
		return map;
	}
	
	private enum UploadFields
	{
		PRODUCT_CODE,
		CARTOON_QUANTITY,
		CBM,
		WEIGHT,
		DESCRIPTION,
		MOQ,
		DEFAULT_MARGIN,
		IS_VALID,
		SUPPLIER_1_INITIALS,
		SUPPLIER_1_CODE,
		SUPPLIER_1_PRICE,
		SUPPLIER_2_INITIALS,
		SUPPLIER_2_CODE,
		SUPPLIER_2_PRICE,
		SUPPLIER_3_INITIALS,
		SUPPLIER_3_CODE,
		SUPPLIER_3_PRICE;
		
		private static final Map<String, UploadFields> cache = new HashMap<>();
		
		static
		{
			for(UploadFields uf : UploadFields.values())
			{
				cache.put(uf.toString(), uf);
			}
		}
		
		public static UploadFields parse(String value)
		{
			return cache.get(value);
		}
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
			spi.setSupplierPrice(new BigDecimal(spii.getSupplierPrice()));
			
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
			spi.setSupplierPrice(new BigDecimal(spii.getSupplierPrice()));
			
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
		//prod.setPrice(prodDto.getPrice());
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
		//prodDto.setPrice(prod.getPrice());
		prodDto.setProductCode(prod.getProductCode());
		prodDto.setWeight(prod.getWeight());
		List<SupplierProductInfo> list = prod.getSuppProdInfo();
		for(SupplierProductInfo spi : list)
		{
			SuppProdInfo spii = new SuppProdInfo();
			spii.setSupplierInitials(spi.getSupplier().getInitials());
			spii.setProductCode(prod.getProductCode());
			spii.setSupplierProductCode(spi.getSupplierProductName());
			spii.setSupplierPrice(spi.getSupplierPrice().setScale(3, RoundingMode.HALF_UP).toString());
			prodDto.getSupplierProductInfoList().add(spii);
		}
		return prodDto;
	}
}
