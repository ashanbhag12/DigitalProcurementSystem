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

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.Constants;
import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.Configurations;
import com.dps.domain.entity.Customer;
import com.dps.domain.entity.CustomerProductPreference;
import com.dps.domain.entity.Product;
import com.dps.domain.entity.SupplierProductInfo;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerOrderDetailsService;
import com.dps.service.CustomerOrderService;
import com.dps.service.CustomerProductPreferenceService;
import com.dps.service.CustomerService;
import com.dps.service.ProductService;
import com.dps.service.SupplierService;
import com.dps.web.service.model.BillDTO;
import com.dps.web.service.model.BuildOrderDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Path("/bill")
public class AdhocBillController
{
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerProductPreferenceService custProdPrefService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ConfigurationsService configService;
	
	@Autowired
	private CustomerOrderDetailsService custOrderDetService;
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private CustomerOrderService custOrderService;
	
	@POST
	@Path("/calculate")
	@Produces(MediaType.APPLICATION_JSON)
	public BillDTO calculatePrices(BillDTO wrapper) 
	{
		try
		{
			//Get customer information
			List<Customer> custList = customerService.findByShipmarkAndName(wrapper.getCustomerShipmark(), null);
			Customer cust = custList.get(0);
			BigDecimal custAddMargin = cust.getAdditionalMargin();
			
			//Get customer preferences
			Map<Long, BigDecimal> custProdPrefs = custProdPrefService.findPreferencesForCustomer(cust.getId());
			
			//Get global constants
			Configurations config = configService.findAll().get(0);
			
			List<BuildOrderDTO> orderItems = wrapper.getOrderItems();
			
			//Get all the products that are in the line item
			List<JpaEntityId> idList = new ArrayList<>();
			List<Long> longIdList = new ArrayList<>();
			for(BuildOrderDTO item : orderItems)
			{
				idList.add(new JpaEntityId(item.getProductId()));
				longIdList.add(item.getProductId());
			}
			List<Product> prodList = productService.findAll(idList);

			//Store all the products in a map for faster access
			Map<Long, Product> prodMap = new HashMap<>();
			for(Product p : prodList)
			{
				prodMap.put(p.getId(), p);
			}
			
			//Get all the unordered quantity of the products.
			Map<Long, Integer> unorderedProductQuantity = custOrderDetService.getUnorderedQuantityForProducts(longIdList);
			
			//Calculate the price of each item
			for(BuildOrderDTO item : orderItems)
			{
				Product product = prodMap.get(item.getProductId());
				BigDecimal custProdMargin = custProdPrefs.get(item.getProductId()) != null ? custProdPrefs.get(item.getProductId()) : Constants.BIG_DECIMAL_ONE;
				BigDecimal productMargin = prodMap.get(item.getProductId()).getDefaultMargin();
				
				BigDecimal price = Constants.BIG_DECIMAL_ONE;
				
				for(SupplierProductInfo spi : product.getSuppProdInfo())
				{
					if(StringUtils.equals(spi.getSupplier().getInitials(), item.getSelectedSupplierInitials()))
					{
						price = spi.getSupplierPrice();
						break;
					}
				}
				
				BigDecimal cost = Constants.BIG_DECIMAL_ONE;
				
				cost = cost.multiply(config.getExchangeRate());
				cost = cost.multiply(price);
				
				BigDecimal cost1 = Constants.BIG_DECIMAL_ONE;
				cost1 = cost1.multiply(product.getCbm());
				cost1 = cost1.multiply(config.getPricePerCbm());
				cost1 = cost1.divide(new BigDecimal(product.getCartoonQuantity()), RoundingMode.HALF_UP);
				
				BigDecimal cost2 = Constants.BIG_DECIMAL_ONE;
				cost2 = cost2.multiply(product.getWeight());
				cost2 = cost2.multiply(config.getPricePerWeight());
				cost2 = cost2.divide(new BigDecimal(product.getCartoonQuantity()), RoundingMode.HALF_UP);
				
				cost = cost.add(cost1);
				cost = cost.add(cost2);
				
				cost = cost.multiply(productMargin);
				cost = cost.multiply(custProdMargin);
				cost = cost.multiply(custAddMargin);
				
				cost.setScale(3, BigDecimal.ROUND_HALF_UP);
				
				item.setUnitCost(cost);
				
				//Check if the MOQ is fulfilled
				Integer unorderedQuantity = unorderedProductQuantity.get(product.getId());
				unorderedQuantity = unorderedQuantity == null ? 0 : unorderedQuantity; 
				unorderedQuantity += item.getQuantity();
				
				if(unorderedQuantity >= product.getMoq())
				{
					item.setIsMoqSatisfied(true);
				}
				
				//Add customer product margin
				item.setCustProdDiscount(custProdMargin);
				item.setCustProdDiscountPercent(cust.getDiscountPrcentage());
			}
			
			return wrapper;
		}
		catch(Exception e)
		{
			throw e;
		}
		
	}
	
	@POST
	@Path("/txtinv")
	@Produces(MediaType.APPLICATION_JSON)
	public void generateInvoice(BillDTO wrapper) throws Exception 
	{
		Configurations config = configService.findAll().get(0);
		Customer cust = customerService.findByShipmarkAndName(wrapper.getCustomerShipmark(), null).get(0);
		
		Map<Long, BigDecimal> custProdPrefs = new HashMap<>();
		
		List<CustomerProductPreference> custProdPreferences = custProdPrefService.findAllPreferencesForCustomer(cust.getId());
		
		for(CustomerProductPreference pref : custProdPreferences)
		{
			custProdPrefs.put(pref.getProduct().getId(), pref.getDiscount());
		}
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String dateStr = df.format(date);
		
		String basePath = config.getBasePath();
		if(!basePath.endsWith(File.separator))
		{
			basePath = basePath + File.separator;
		}
		
		String reportPath = basePath + "customer";
		
		File dir = new File(reportPath);
		if(!dir.exists())
		{
			dir.mkdir();
		}
		
		
		BigDecimal gt = Constants.BIG_DECIMAL_ZERO;
		
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(reportPath + File.separator + "inv_" + wrapper.getCustomerShipmark() + "_" +dateStr+"_adhoc.pdf"));
		document.open();
		
		document.add(new Paragraph(dateStr + "\n\n"));
		
		for(BuildOrderDTO item : wrapper.getOrderItems())
		{
				Product product = productService.findByCode(item.getProductCode()).get(0);
				
				BigDecimal cost = new BigDecimal(item.getUnitCost().doubleValue());
				String unitPrice = cost.setScale(2, RoundingMode.HALF_UP).toString();
				
				cost = cost.multiply(new BigDecimal(product.getCartoonQuantity()));
				//cost = cost.multiply(new BigDecimal(item.getQuantity()));
				String price = cost.setScale(2, RoundingMode.HALF_UP).toString();
				
				document.add(new Paragraph(item.getQuantity() + " . " + product.getCartoonQuantity() + " . " +  product.getProductCode() + " . " + price + " . " + unitPrice + " . " + product.getDescription()));
				
				gt = gt.add(cost);
		}
		
		if(!(wrapper.getAdditionalCost() == null && wrapper.getAdditionalCostDetails() == null))
		{
			if(wrapper.getAdditionalCost() == null)
			{
				wrapper.setAdditionalCost(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(wrapper.getAdditionalCostDetails() == null)
			{
				wrapper.setAdditionalCostDetails("");
			}
			
			document.add(new Paragraph( "\n" + wrapper.getAdditionalCostDetails() + " : " + wrapper.getAdditionalCost().setScale(2, RoundingMode.HALF_UP).toString()));
			
			gt = gt.add(wrapper.getAdditionalCost());
		}
		
		if(!(wrapper.getAdditionalDiscount() == null && wrapper.getAdditionalDiscountDetails() == null))
		{
			if(wrapper.getAdditionalDiscount() == null)
			{
				wrapper.setAdditionalDiscount(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(wrapper.getAdditionalDiscountDetails() == null)
			{
				wrapper.setAdditionalDiscountDetails("");
			}
			
			document.add(new Paragraph( "\n" + wrapper.getAdditionalDiscountDetails() + " : " + wrapper.getAdditionalDiscount().setScale(2, RoundingMode.HALF_UP).toString()));
			
			gt = gt.subtract(wrapper.getAdditionalDiscount());
		}
		
		document.add(new Paragraph("\n\n" + gt.setScale(2, RoundingMode.HALF_UP).toString()));
		
		document.close();
		writer.flush();
		writer.close();
	}
	
	@POST
	@Path("/imginv")
	public void generatePicInvoice(BillDTO wrapper) throws Exception
	{
		Configurations config = configService.findAll().get(0);
		Customer cust = customerService.findByShipmarkAndName(wrapper.getCustomerShipmark(), null).get(0);
		
		Map<Long, BigDecimal> custProdPrefs = new HashMap<>();
		
		List<CustomerProductPreference> custProdPreferences = custProdPrefService.findAllPreferencesForCustomer(cust.getId());
		
		for(CustomerProductPreference pref : custProdPreferences)
		{
			custProdPrefs.put(pref.getProduct().getId(), pref.getDiscount());
		}
		
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String dateStr = df.format(date);
		
		String basePath = config.getBasePath();
		if(!basePath.endsWith(File.separator))
		{
			basePath = basePath + File.separator;
		}
		
		String reportPath = basePath + "customer";
		
		File dir = new File(reportPath);
		if(!dir.exists())
		{
			dir.mkdir();
		}
		
		String imagePath = basePath + "images"  + File.separator;
		
		BigDecimal gt = Constants.BIG_DECIMAL_ZERO;
		
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(reportPath + File.separator + "img_inv_" + wrapper.getCustomerShipmark() + "_" +dateStr+"_adhoc.pdf"));
		document.open();
		
		PdfPTable table = new PdfPTable(5); 
		table.setWidthPercentage(100);
		table.setSpacingBefore(1f);
		table.setSpacingAfter(1f);
		
		float[] columnWidths = {1f,1f,1f,1f,1f};
		table.setWidths(columnWidths);
		
		for(BuildOrderDTO item : wrapper.getOrderItems())
		{
				Product product = productService.findByCode(item.getProductCode()).get(0);
				
				PdfPCell cell = createNewCell();
				
				//BigDecimal cost = findCost(config, item.getUnitCost(), product, cust.getAdditionalMargin(),custProdPrefs.get(product.getId()));
				BigDecimal cost = new BigDecimal(item.getUnitCost().doubleValue());
				String unitPrice = cost.setScale(2, RoundingMode.HALF_UP).toString();
				//cost = cost.multiply(new BigDecimal(item.getQuantity()));
				cost = cost.multiply(new BigDecimal(product.getCartoonQuantity()));
				String price = cost.setScale(2, RoundingMode.HALF_UP).toString();
				cell.addElement(new Paragraph(product.getProductCode() + "  " + product.getCartoonQuantity() + "\n" +unitPrice + " " + item.getQuantity() + " " + price));
				
				gt = gt.add(cost);
				
				Image image = null;
				try
				{
					image = Image.getInstance(imagePath + product.getProductCode() + ".jpg");
					image.scaleToFit(75f, 75f);
					image.setBorderWidth(2);
					image.setBorder(Rectangle.BOX);
					cell.addElement(image);
				}
				catch(Exception e)
				{
					try
					{
						image = Image.getInstance(imagePath + product.getProductCode() + ".jpeg");
						image.scaleToFit(75f, 75f);
						image.setBorderWidth(2);
						image.setBorder(Rectangle.BOX);
						cell.addElement(image);
					}
					catch(Exception ex)
					{
						cell.addElement(new Paragraph("\nImage not available."));
					}
				}
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
		}
		
		if(!(wrapper.getAdditionalCostDetails() == null && wrapper.getAdditionalCost() == null))
		{
			PdfPCell cell = createNewCell();
			
			if(wrapper.getAdditionalCost() == null)
			{
				wrapper.setAdditionalCost(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(wrapper.getAdditionalCostDetails() == null)
			{
				wrapper.setAdditionalCostDetails("") ;
			}
			cell.addElement(new Paragraph(wrapper.getAdditionalCostDetails() + " : " + wrapper.getAdditionalCost().setScale(2, RoundingMode.HALF_UP).toString()));
		
			table.addCell(cell);
		
			gt = gt.add(wrapper.getAdditionalCost());
		}
		
		if(!(wrapper.getAdditionalDiscountDetails() == null && wrapper.getAdditionalDiscount() == null))
		{
			PdfPCell cell = createNewCell();
			
			if(wrapper.getAdditionalDiscount() == null)
			{
				wrapper.setAdditionalDiscount(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(wrapper.getAdditionalDiscountDetails() == null)
			{
				wrapper.setAdditionalDiscountDetails("") ;
			}
			cell.addElement(new Paragraph(wrapper.getAdditionalDiscountDetails() + " : " + wrapper.getAdditionalDiscount().setScale(2, RoundingMode.HALF_UP).toString()));
			
			table.addCell(cell);
			
			gt = gt.subtract(wrapper.getAdditionalDiscount());
		}
		
		PdfPCell cell = createNewCell();
		cell.addElement(new Paragraph(gt.setScale(2, RoundingMode.HALF_UP).toString()));
		
		table.addCell(cell);
		
		table.completeRow();
		
		document.add(table);
		
		document.close();
		writer.flush();
		writer.close();
	}
	
	@SuppressWarnings("unused")
	private BigDecimal findCost(Configurations config, BigDecimal productPrice, Product product, BigDecimal custMargin, BigDecimal custProdMargin)
	{
		BigDecimal cost = Constants.BIG_DECIMAL_ONE;
		
		cost = cost.multiply(config.getExchangeRate());
		cost = cost.multiply(productPrice);
		
		BigDecimal cost1 = Constants.BIG_DECIMAL_ONE;
		cost1 = cost1.multiply(product.getCbm());
		cost1 = cost1.multiply(config.getPricePerCbm());
		cost1 = cost1.divide(new BigDecimal(product.getCartoonQuantity()), RoundingMode.HALF_UP);
		
		BigDecimal cost2 = Constants.BIG_DECIMAL_ONE;
		cost2 = cost2.multiply(product.getWeight());
		cost2 = cost2.multiply(config.getPricePerWeight());
		cost2 = cost2.divide(new BigDecimal(product.getCartoonQuantity()), RoundingMode.HALF_UP);
		
		cost = cost.add(cost1);
		cost = cost.add(cost2);
		
		if(custProdMargin == null)
		{
			custProdMargin = Constants.BIG_DECIMAL_ONE;
		}
		
		cost = cost.multiply(product.getDefaultMargin());
		cost = cost.multiply(custProdMargin);
		cost = cost.multiply(custMargin);
		
		return cost;
	}
	
	private PdfPCell createNewCell()
	{
		PdfPCell cell = new PdfPCell();
		cell.setPadding(2f);
		
		return cell;
	}
}
