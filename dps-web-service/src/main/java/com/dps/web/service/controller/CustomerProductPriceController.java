package com.dps.web.service.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.Constants;
import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.Configurations;
import com.dps.domain.entity.Customer;
import com.dps.domain.entity.CustomerProductPreference;
import com.dps.domain.entity.Product;
import com.dps.domain.entity.SupplierProductInfo;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerProductPreferenceService;
import com.dps.service.CustomerService;
import com.dps.service.ProductService;
import com.dps.web.service.model.CustomerProductPricesDTO;
import com.dps.web.service.model.CustomerProductPricesWrapperDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This is a controller class that computes the price of a product for a customer and also saves the discount for a customer.
 *
 * @see
 *
 * @Date 15-Aug-2016
 *
 * @author akshay
 */
@Path("/custprodpref")
public class CustomerProductPriceController
{
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerProductPreferenceService custProdPrefService;
	
	@Autowired
	private ConfigurationsService configService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{shipmark}")
	public CustomerProductPricesWrapperDTO findProduct(@PathParam("shipmark") String shipmark)
	{
		List<CustomerProductPricesDTO> custPrices = new ArrayList<>();
		List<Product> prodList = new ArrayList<>();
		Map<Long, BigDecimal> custProdPrefs = new HashMap<>();
		Map<Long, BigDecimal> custProdPrefsPect = new HashMap<>();
		Customer cust = null;
		List<CustomerProductPreference> custProdPreferences = null;
		try
		{
			List<Customer> custList = customerService.findByShipmarkAndName(shipmark, null);
			cust = custList.get(0);
			prodList = productService.findAll();
			custProdPreferences = custProdPrefService.findAllPreferencesForCustomer(cust.getId());
			
			for(CustomerProductPreference pref : custProdPreferences)
			{
				custProdPrefs.put(pref.getProduct().getId(), pref.getDiscount());
				custProdPrefsPect.put(pref.getProduct().getId(), pref.getDiscountPrcentage());
			}
		}
		catch(Exception e)
		{
			
		}
		
		for(Product prod : prodList)
		{
			CustomerProductPricesDTO custProdPriceDto = new CustomerProductPricesDTO();
			custProdPriceDto.setProductCode(prod.getProductCode());
			custProdPriceDto.setCustomerProductMargin(custProdPrefs.get(prod.getId()) != null ? custProdPrefs.get(prod.getId()) : Constants.BIG_DECIMAL_ONE);
			custProdPriceDto.setCustomerProductMarginPercentage(custProdPrefsPect.get(prod.getId()) != null ? custProdPrefsPect.get(prod.getId()) : Constants.BIG_DECIMAL_ONE);
			custProdPriceDto.setProductMargin(prod.getDefaultMargin());
			custProdPriceDto.setProductMarginPercentage(prod.getDiscountPrcentage());
			custProdPriceDto.setProductPrice(prod.getSuppProdInfo().get(0).getSupplierPrice());
			custProdPriceDto.setProductDescription(prod.getDescription());
			custProdPriceDto.setCartoonQuantity(prod.getCartoonQuantity());
			custProdPriceDto.setGrossWeight(prod.getWeight());
			custProdPriceDto.setCbm(prod.getCbm());
			
			StringBuffer sb = new StringBuffer();
			for(SupplierProductInfo suppProdInfo : prod.getSuppProdInfo())
			{
				sb.append(suppProdInfo.getSupplier().getInitials());
				sb.append("; ");
			}
			
			custProdPriceDto.setSupplierInitials(sb.toString());
			
			BigDecimal cost = Constants.BIG_DECIMAL_ONE;
			cost = cost.multiply(custProdPriceDto.getCustomerProductMargin());
			cost = cost.multiply(cust.getAdditionalMargin());
			cost = cost.multiply(custProdPriceDto.getProductMargin());
			cost = cost.multiply(custProdPriceDto.getProductPrice());
			cost.setScale(3, BigDecimal.ROUND_HALF_UP);
			custProdPriceDto.setCost(cost);
			
			custPrices.add(custProdPriceDto);
		}
		
		CustomerProductPricesWrapperDTO custProdPriceWrapper = new CustomerProductPricesWrapperDTO();
		custProdPriceWrapper.setAdditionalCustomerMargin(cust.getAdditionalMargin());
		custProdPriceWrapper.setAdditionalCustomerMarginPercentage(cust.getDiscountPrcentage());
		custProdPriceWrapper.setShipmark(shipmark);
		custProdPriceWrapper.setCustomerProductPrices(custPrices);
		
		return custProdPriceWrapper;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/modify")
	public void savePreferences(CustomerProductPricesWrapperDTO wrapper)
	{
		try
		{
			//Get the customer
			List<Customer> custList = customerService.findByShipmarkAndName(wrapper.getShipmark(), null);
			Customer cust = custList.get(0);
			
			//Get all the preferences for that customer.
			List<CustomerProductPreference> customerPreferences = custProdPrefService.findAllPreferencesForCustomer(cust.getId());
			
			//Save fetched preferences in a map for ease of future access
			Map<String, CustomerProductPreference> custProdPrefObj = new HashMap<>();
			if(customerPreferences != null)
			{
				for(CustomerProductPreference pref : customerPreferences)
				{
					custProdPrefObj.put(pref.getProduct().getProductCode(), pref);
				}
			}
			
			//These will be merged to the database.
			List<CustomerProductPreference> customerUpdatedPreferences = new ArrayList<>();
			List<JpaEntityId> toDeletePreferences = new ArrayList<>();
			
			for(CustomerProductPricesDTO custProdPrice : wrapper.getCustomerProductPrices())
			{
				if(!Constants.BIG_DECIMAL_ONE.equals(custProdPrice.getCustomerProductMargin()))
				{
					BigDecimal existingDiscount = Constants.BIG_DECIMAL_ONE; 
					CustomerProductPreference existingPreference = custProdPrefObj.get(custProdPrice.getProductCode());
					if(existingPreference != null)
					{
						existingDiscount = existingPreference.getDiscount();
					}
					
					if(!ObjectUtils.equals(existingDiscount, custProdPrice.getCustomerProductMargin()))
					{
						CustomerProductPreference pref = custProdPrefObj.get(custProdPrice.getProductCode());
						
						if(pref == null)
						{
							pref = new CustomerProductPreference();
							pref.setCustomer(cust);
							Product product = productService.findByCode(custProdPrice.getProductCode()).get(0);
							pref.setProduct(product);
						}
						pref.setDiscount(custProdPrice.getCustomerProductMargin());
						
						customerUpdatedPreferences.add(pref);
					}
				}
				else
				{
					CustomerProductPreference existingPreference = custProdPrefObj.get(custProdPrice.getProductCode());
					if(existingPreference != null)
					{
						toDeletePreferences.add(new JpaEntityId(existingPreference.getId()));
					}
				}
			}
			
			custProdPrefService.mergeAll(customerUpdatedPreferences);
			custProdPrefService.removeAll(toDeletePreferences);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@POST
	@Path("/copy/{cust1}/{cust2}")
	public void copyDiscountingData(@PathParam("cust1") String cust1, @PathParam("cust2") String cust2)
	{
		Customer srcCust = customerService.findByShipmarkAndName(cust1, null).get(0);
		Customer destCust = customerService.findByShipmarkAndName(cust2, null).get(0);
		
		List<CustomerProductPreference> srcCustPrefs = custProdPrefService.findAllPreferencesForCustomer(srcCust.getId());
		List<CustomerProductPreference> destCustPrefs = new ArrayList<>();
		
		for(CustomerProductPreference srcPref : srcCustPrefs)
		{
			CustomerProductPreference destPref = new CustomerProductPreference();
			destPref.setCustomer(destCust);
			destPref.setDiscount(srcPref.getDiscount());
			destPref.setProduct(srcPref.getProduct());
			destCustPrefs.add(destPref);
		}
		
		custProdPrefService.persistAll(destCustPrefs);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/export")
	public void generatePdfReport(CustomerProductPricesWrapperDTO wrapper)
	{
		try
		{
			//Get the configurations
			Configurations config = configService.findAll().get(0);
			
			//Get the customer
			List<Customer> custList = customerService.findByShipmarkAndName(wrapper.getShipmark(), null);
			Customer cust = custList.get(0);
			
			Date date = new Date();
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			String dateStr = df.format(date);
			
			String reportPath = config.getBasePath() + "customer" + File.separator;
			String imagePath = config.getBasePath() + "images"  + File.separator;
			
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(reportPath + cust.getShipmark() + "_" +dateStr+".pdf"));
			document.open();
			
			PdfPTable table = new PdfPTable(5); 
			table.setWidthPercentage(100);
			table.setSpacingBefore(5f);
			table.setSpacingAfter(5f);
			
			float[] columnWidths = {1f,1f,1f,1f,1f};
			table.setWidths(columnWidths);
			
			for(CustomerProductPricesDTO custProdPrice : wrapper.getCustomerProductPrices())
			{
				if(custProdPrice.isToExport())
				{
					PdfPCell cell = createNewCell();
					cell.addElement(new Paragraph("    "+custProdPrice.getProductPrice().setScale(2, RoundingMode.HALF_UP).toString()));
					
					Image image = null;
					try
					{
						image = Image.getInstance(imagePath + custProdPrice.getProductCode() + ".jpg");
					}
					catch(Exception e)
					{
						image = Image.getInstance(imagePath + custProdPrice.getProductCode() + ".jpeg");
					}
					
					//image.scaleAbsoluteHeight(75f);
					//image.scaleAbsoluteWidth(75f);
					image.scaleToFit(75f, 75f);
					image.setBorderWidth(2);
					image.setBorder(Rectangle.BOX);
					cell.addElement(image);
					
					table.addCell(cell);
				}
			}
			
			table.completeRow();
			
			document.add(table);
			
			document.close();
			writer.flush();
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private PdfPCell createNewCell()
	{
		PdfPCell cell = new PdfPCell();
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);	
		cell.setPadding(10f);
		
		return cell;
	}
}
