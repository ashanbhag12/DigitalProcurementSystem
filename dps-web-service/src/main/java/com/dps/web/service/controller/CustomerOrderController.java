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

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.Constants;
import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.constants.CustomerOrderDetailStatus;
import com.dps.domain.constants.CustomerOrderStatus;
import com.dps.domain.entity.Configurations;
import com.dps.domain.entity.Customer;
import com.dps.domain.entity.CustomerOrder;
import com.dps.domain.entity.CustomerOrderDetails;
import com.dps.domain.entity.CustomerProductPreference;
import com.dps.domain.entity.Product;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerOrderService;
import com.dps.service.CustomerProductPreferenceService;
import com.dps.service.CustomerService;
import com.dps.service.ProductService;
import com.dps.web.service.model.CustomerOrderDTO;
import com.dps.web.service.model.CustomerOrderDetailsDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Controller class that handles all the web service requests for updating customer orders operations.
 *
 * @see
 *
 * @Date 19-Nov-2016
 *
 * @author akshay
 */
@Path("/customerorder")
public class CustomerOrderController
{
	@Autowired
	private CustomerOrderService custOrderService;
	
	@Autowired
	private ConfigurationsService configService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerProductPreferenceService custProdPrefService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{shipmark}/{startDate}/{endDate}")
	public List<CustomerOrderDTO> getCustomerOrders(@PathParam("shipmark") String shipmark, @PathParam("startDate") Long startDate, @PathParam("endDate") Long endDate)
	{
		List<CustomerOrder> custOrders = custOrderService.getCustomerOrders(shipmark, new Date(startDate), new Date(endDate));
		List<CustomerOrderDTO> custOrdersList = new ArrayList<>();
		
		for(CustomerOrder custOrder : custOrders)
		{
			custOrdersList.add(convertToDto(custOrder));
		}
		
		return custOrdersList;
	}
	
	@POST
	@Path("/cancel")
	public void cancelOrders(CustomerOrderDTO custOrderDto)
	{
		CustomerOrder custOrder = custOrderService.find(new JpaEntityId(custOrderDto.getId()));
		List<CustomerOrderDetails> custOrderDetails = custOrder.getLineItems();
		Map<Long, CustomerOrderDetails> custOrdersMap = new HashMap<>();
		
		for(CustomerOrderDetails custOrderDet : custOrderDetails)
		{
			custOrdersMap.put(custOrderDet.getId(), custOrderDet);
		}
		
		List<CustomerOrderDetailsDTO> lineItemsDto = custOrderDto.getLineItems();
		int completedOrder = 0;
		for(CustomerOrderDetailsDTO lineItem : lineItemsDto)
		{
			CustomerOrderDetails custOrderDet = custOrdersMap.get(lineItem.getId());
			
			if(lineItem.isSelected())
				custOrderDet.setStatus(CustomerOrderDetailStatus.CANCELLED);
			
			if(custOrderDet.getStatus() == CustomerOrderDetailStatus.CANCELLED || custOrderDet.getStatus() == CustomerOrderDetailStatus.ORDER_COMPLETED)
			{
				completedOrder++;
			}
		}
		
		if(completedOrder == lineItemsDto.size())
		{
			custOrder.setStatus(CustomerOrderStatus.COMPLETED);
		}
		
		custOrder = updateAdditionalDetails(custOrderDto, custOrder);
		
		custOrderService.merge(custOrder);
	}
	
	@POST
	@Path("/update")
	public void updateOrders(CustomerOrderDTO custOrderDto)
	{
		CustomerOrder custOrder = custOrderService.find(new JpaEntityId(custOrderDto.getId()));
		List<CustomerOrderDetails> custOrderDetails = custOrder.getLineItems();
		Map<Long, CustomerOrderDetails> custOrdersMap = new HashMap<>();
		
		for(CustomerOrderDetails custOrderDet : custOrderDetails)
		{
			custOrdersMap.put(custOrderDet.getId(), custOrderDet);
		}
		
		List<CustomerOrderDetailsDTO> lineItemsDto = custOrderDto.getLineItems();
		for(CustomerOrderDetailsDTO lineItem : lineItemsDto)
		{
			CustomerOrderDetails custOrderDet = custOrdersMap.get(lineItem.getId());
			custOrderDet.setProductPrice(lineItem.getProductPrice());
		}
		
		custOrder = updateAdditionalDetails(custOrderDto, custOrder);
		
		custOrderService.merge(custOrder);
	}
	
	@POST
	@Path("/txtinv")
	public void generateTextInvoice(CustomerOrderDTO custOrderDto) throws Exception
	{
		Configurations config = configService.findAll().get(0);
		Customer cust = customerService.findByShipmarkAndName(custOrderDto.getShipmark(), null).get(0);
		
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
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(reportPath + File.separator + "inv_" + custOrderDto.getShipmark() + "_" +dateStr+".pdf"));
		document.open();
		
		document.add(new Paragraph(dateStr + "\n\n"));
		
		for(CustomerOrderDetailsDTO custOrdrDet : custOrderDto.getLineItems())
		{
			if(custOrdrDet.isSelected())
			{
				Product product = productService.findByCode(custOrdrDet.getProductCode()).get(0);
				
				BigDecimal cost = findCost(config, custOrdrDet.getProductPrice(), product, cust.getAdditionalMargin(),custProdPrefs.get(product.getId()));
				String costPerItem = cost.setScale(2, RoundingMode.HALF_UP).toString();
				if(custOrdrDet.getReceivedQuantity() == null)
				{
					custOrdrDet.setReceivedQuantity(0);
				}
				
				cost = cost.multiply(new BigDecimal(product.getCartoonQuantity()));
				cost = cost.multiply(new BigDecimal(custOrdrDet.getReceivedQuantity()));
				String price = cost.setScale(2, RoundingMode.HALF_UP).toString();
				
				document.add(new Paragraph(custOrdrDet.getReceivedQuantity() + " . " + product.getCartoonQuantity() + " . " +  product.getProductCode() + " . " + costPerItem + " . " + price + " . " + product.getDescription()));
				
				gt = gt.add(cost);
			}
		}
		
		if(!(custOrderDto.getAdditionalCost() == null && custOrderDto.getAdditionalCostDetails() == null))
		{
			if(custOrderDto.getAdditionalCost() == null)
			{
				custOrderDto.setAdditionalCost(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(custOrderDto.getAdditionalCostDetails() == null)
			{
				custOrderDto.setAdditionalCostDetails("");
			}
			
			document.add(new Paragraph( "\n" + custOrderDto.getAdditionalCostDetails() + " : " + custOrderDto.getAdditionalCost().setScale(2, RoundingMode.HALF_UP).toString()));
			
			gt = gt.add(custOrderDto.getAdditionalCost());
		}
		
		if(!(custOrderDto.getAdditionalDiscount() == null && custOrderDto.getAdditionalDiscountDetails() == null))
		{
			if(custOrderDto.getAdditionalDiscount() == null)
			{
				custOrderDto.setAdditionalDiscount(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(custOrderDto.getAdditionalDiscountDetails() == null)
			{
				custOrderDto.setAdditionalDiscountDetails("");
			}
			
			document.add(new Paragraph( "\n" + custOrderDto.getAdditionalDiscountDetails() + " : " + custOrderDto.getAdditionalDiscount().setScale(2, RoundingMode.HALF_UP).toString()));
			
			gt = gt.subtract(custOrderDto.getAdditionalDiscount());
		}
		
		document.add(new Paragraph("\n\n" + gt.setScale(2, RoundingMode.HALF_UP).toString()));
		
		document.close();
		writer.flush();
		writer.close();
	}
	
	
	@POST
	@Path("/lasttxtinv")
	public void generateLastBatchTextInvoice(CustomerOrderDTO custOrderDto) throws Exception
	{
		Configurations config = configService.findAll().get(0);
		Customer cust = customerService.findByShipmarkAndName(custOrderDto.getShipmark(), null).get(0);
		
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
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(reportPath + File.separator + "inv_" + custOrderDto.getShipmark() + "_" +dateStr+".pdf"));
		document.open();
		
		document.add(new Paragraph(dateStr + "\n\n"));
		
		for(CustomerOrderDetailsDTO custOrdrDet : custOrderDto.getLineItems())
		{
			if(custOrdrDet.getLastReceivedQuantity()!= null && custOrdrDet.getLastReceivedQuantity() != 0)
			{
				Product product = productService.findByCode(custOrdrDet.getProductCode()).get(0);
				
				BigDecimal cost = findCost(config, custOrdrDet.getProductPrice(), product, cust.getAdditionalMargin(),custProdPrefs.get(product.getId()));
				String costPerItem = cost.setScale(2, RoundingMode.HALF_UP).toString();
				cost = cost.multiply(new BigDecimal(product.getCartoonQuantity()));
				cost = cost.multiply(new BigDecimal(custOrdrDet.getLastReceivedQuantity()));
				String price = cost.setScale(2, RoundingMode.HALF_UP).toString();
				
				document.add(new Paragraph(custOrdrDet.getLastReceivedQuantity() + " . " + product.getCartoonQuantity() + " . " +  product.getProductCode() + " . " + costPerItem + " . " + price + " . " + product.getDescription()));
				
				gt = gt.add(cost);
			}
		}
		
		if(!(custOrderDto.getAdditionalCost() == null && custOrderDto.getAdditionalCostDetails() == null))
		{
			if(custOrderDto.getAdditionalCost() == null)
			{
				custOrderDto.setAdditionalCost(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(custOrderDto.getAdditionalCostDetails() == null)
			{
				custOrderDto.setAdditionalCostDetails("");
			}
			
			document.add(new Paragraph( "\n" + custOrderDto.getAdditionalCostDetails() + " : " + custOrderDto.getAdditionalCost().setScale(2, RoundingMode.HALF_UP).toString()));
			
			gt = gt.add(custOrderDto.getAdditionalCost());
		}
		
		if(!(custOrderDto.getAdditionalDiscount() == null && custOrderDto.getAdditionalDiscountDetails() == null))
		{
			if(custOrderDto.getAdditionalDiscount() == null)
			{
				custOrderDto.setAdditionalDiscount(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(custOrderDto.getAdditionalDiscountDetails() == null)
			{
				custOrderDto.setAdditionalDiscountDetails("");
			}
			
			document.add(new Paragraph( "\n" + custOrderDto.getAdditionalDiscountDetails() + " : " + custOrderDto.getAdditionalDiscount().setScale(2, RoundingMode.HALF_UP).toString()));
			
			gt = gt.subtract(custOrderDto.getAdditionalDiscount());
		}
		
		
		document.add(new Paragraph("\n\n" + gt.setScale(2, RoundingMode.HALF_UP).toString()));
		
		document.close();
		writer.flush();
		writer.close();
	}
	
	
	@POST
	@Path("/imginv")
	public void generatePicInvoice(CustomerOrderDTO custOrderDto) throws Exception
	{
		Configurations config = configService.findAll().get(0);
		Customer cust = customerService.findByShipmarkAndName(custOrderDto.getShipmark(), null).get(0);
		
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
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(reportPath + File.separator + "img_inv_" + custOrderDto.getShipmark() + "_" +dateStr+".pdf"));
		document.open();
		
		PdfPTable table = new PdfPTable(5); 
		table.setWidthPercentage(100);
		table.setSpacingBefore(1f);
		table.setSpacingAfter(1f);
		
		float[] columnWidths = {1f,1f,1f,1f,1f};
		table.setWidths(columnWidths);
		
		for(CustomerOrderDetailsDTO custOrdrDet : custOrderDto.getLineItems())
		{
			if(custOrdrDet.isSelected())
			{
				Product product = productService.findByCode(custOrdrDet.getProductCode()).get(0);
				
				PdfPCell cell = createNewCell();
				
				BigDecimal cost = findCost(config, custOrdrDet.getProductPrice(), product, cust.getAdditionalMargin(),custProdPrefs.get(product.getId()));
				String costPerItem = cost.setScale(2, RoundingMode.HALF_UP).toString();
				
				if(custOrdrDet.getReceivedQuantity() == null)
				{
					custOrdrDet.setReceivedQuantity(0);
				}
				
				cost = cost.multiply(new BigDecimal(product.getCartoonQuantity()));
				cost = cost.multiply(new BigDecimal(custOrdrDet.getReceivedQuantity()));
				String price = cost.setScale(2, RoundingMode.HALF_UP).toString();
				cell.addElement(new Paragraph(product.getProductCode() + "  " + product.getCartoonQuantity() + "\n" +costPerItem + " " + custOrdrDet.getReceivedQuantity() + " " + price));
				
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
		}
		
		if(!(custOrderDto.getAdditionalCostDetails() == null && custOrderDto.getAdditionalCost() == null))
		{
			PdfPCell cell = createNewCell();
			
			if(custOrderDto.getAdditionalCost() == null)
			{
				custOrderDto.setAdditionalCost(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(custOrderDto.getAdditionalCostDetails() == null)
			{
				custOrderDto.setAdditionalCostDetails("") ;
			}
			cell.addElement(new Paragraph(custOrderDto.getAdditionalCostDetails() + " : " + custOrderDto.getAdditionalCost().setScale(2, RoundingMode.HALF_UP).toString()));
		
			table.addCell(cell);
		
			gt = gt.add(custOrderDto.getAdditionalCost());
		}
		
		if(!(custOrderDto.getAdditionalDiscountDetails() == null && custOrderDto.getAdditionalDiscount() == null))
		{
			PdfPCell cell = createNewCell();
			
			if(custOrderDto.getAdditionalDiscount() == null)
			{
				custOrderDto.setAdditionalDiscount(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(custOrderDto.getAdditionalDiscountDetails() == null)
			{
				custOrderDto.setAdditionalDiscountDetails("") ;
			}
			cell.addElement(new Paragraph(custOrderDto.getAdditionalDiscountDetails() + " : " + custOrderDto.getAdditionalDiscount().setScale(2, RoundingMode.HALF_UP).toString()));
			
			table.addCell(cell);
			
			gt = gt.subtract(custOrderDto.getAdditionalDiscount());
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
	
	@POST
	@Path("/lastimginv")
	public void generateLastBatchPicInvoice(CustomerOrderDTO custOrderDto) throws Exception
	{
		Configurations config = configService.findAll().get(0);
		Customer cust = customerService.findByShipmarkAndName(custOrderDto.getShipmark(), null).get(0);
		
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
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(reportPath + File.separator + "img_inv_" + custOrderDto.getShipmark() + "_" +dateStr+".pdf"));
		document.open();
		
		PdfPTable table = new PdfPTable(5); 
		table.setWidthPercentage(100);
		table.setSpacingBefore(1f);
		table.setSpacingAfter(1f);
		
		float[] columnWidths = {1f,1f,1f,1f,1f};
		table.setWidths(columnWidths);
		
		for(CustomerOrderDetailsDTO custOrdrDet : custOrderDto.getLineItems())
		{
			if(custOrdrDet.getLastReceivedQuantity() != null && custOrdrDet.getLastReceivedQuantity() != 0)
			{
				Product product = productService.findByCode(custOrdrDet.getProductCode()).get(0);
				
				PdfPCell cell = createNewCell();
				
				BigDecimal cost = findCost(config, custOrdrDet.getProductPrice(), product, cust.getAdditionalMargin(),custProdPrefs.get(product.getId()));
				String costPerItem = cost.setScale(2, RoundingMode.HALF_UP).toString();
				cost = cost.multiply(new BigDecimal(product.getCartoonQuantity()));
				cost = cost.multiply(new BigDecimal(custOrdrDet.getLastReceivedQuantity()));
				String price = cost.setScale(2, RoundingMode.HALF_UP).toString();
				cell.addElement(new Paragraph(product.getProductCode() + "  " + product.getCartoonQuantity() + "\n" +costPerItem + " " + custOrdrDet.getLastReceivedQuantity() + " " + price));
				
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
		}
		
		
		
		if(!(custOrderDto.getAdditionalCostDetails() == null && custOrderDto.getAdditionalCost() == null))
		{
			PdfPCell cell = createNewCell();
			
			if(custOrderDto.getAdditionalCost() == null)
			{
				custOrderDto.setAdditionalCost(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(custOrderDto.getAdditionalCostDetails() == null)
			{
				custOrderDto.setAdditionalCostDetails("") ;
			}
			cell.addElement(new Paragraph(custOrderDto.getAdditionalCostDetails() + " : " + custOrderDto.getAdditionalCost().setScale(2, RoundingMode.HALF_UP).toString()));
		
			table.addCell(cell);
		
			gt = gt.add(custOrderDto.getAdditionalCost());
		}
		
		if(!(custOrderDto.getAdditionalDiscountDetails() == null && custOrderDto.getAdditionalDiscount() == null))
		{
			PdfPCell cell = createNewCell();
			
			if(custOrderDto.getAdditionalDiscount() == null)
			{
				custOrderDto.setAdditionalDiscount(Constants.BIG_DECIMAL_ZERO);
			}
			
			if(custOrderDto.getAdditionalDiscountDetails() == null)
			{
				custOrderDto.setAdditionalDiscountDetails("") ;
			}
			cell.addElement(new Paragraph(custOrderDto.getAdditionalDiscountDetails() + " : " + custOrderDto.getAdditionalDiscount().setScale(2, RoundingMode.HALF_UP).toString()));
			
			table.addCell(cell);
			
			gt = gt.subtract(custOrderDto.getAdditionalDiscount());
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
	
	private CustomerOrder updateAdditionalDetails(CustomerOrderDTO dto, CustomerOrder order)
	{
		order.setAdditionalCost(dto.getAdditionalCost());
		order.setAdditionalCostDetails(dto.getAdditionalCostDetails());
		order.setAdditionalDiscount(dto.getAdditionalDiscount());
		order.setAdditionalDiscountDetails(dto.getAdditionalDiscountDetails());
		return order;
	}

	private CustomerOrderDTO convertToDto(CustomerOrder custOrder)
	{
		CustomerOrderDTO order = new CustomerOrderDTO();
		order.setId(custOrder.getId());
		order.setOrderCompletedDate(custOrder.getOrderCompletedDate());
		order.setOrderDate(custOrder.getOrderDate());
		order.setShipmark(custOrder.getCustomer().getShipmark());
		order.setStatus(custOrder.getStatus().toString());
		order.setAdditionalCost(custOrder.getAdditionalCost());
		order.setAdditionalCostDetails(custOrder.getAdditionalCostDetails());
		order.setAdditionalDiscount(custOrder.getAdditionalDiscount());
		order.setAdditionalDiscountDetails(custOrder.getAdditionalDiscountDetails());
		
		List<CustomerOrderDetails> lineItems = custOrder.getLineItems();
		List<CustomerOrderDetailsDTO> lineItemsDtoList = new ArrayList<>();
		
		for(CustomerOrderDetails lineItem : lineItems)
		{
			CustomerOrderDetailsDTO lineItemDto = new CustomerOrderDetailsDTO();
			lineItemDto.setOrderPlacedDate(lineItem.getOrderPlacedDate());
			lineItemDto.setOrderReceivedDate(lineItem.getOrderReceivedDate());
			lineItemDto.setProductCode(lineItem.getProduct().getProductCode());
			lineItemDto.setProductPrice(lineItem.getProductPrice());
			lineItemDto.setQuantity(lineItem.getQuantity());
			lineItemDto.setReceivedQuantity(lineItem.getReceivedQuantity());
			lineItemDto.setRemarks(lineItem.getRemarks());
			lineItemDto.setStatus(lineItem.getStatus().toString());
			lineItemDto.setSupplierInitials(lineItem.getSupplier().getInitials());
			lineItemDto.setId(lineItem.getId());
			lineItemDto.setLastReceivedQuantity(lineItem.getLastReceivedQuantity());
			
			lineItemsDtoList.add(lineItemDto);
		}
		
		order.setLineItems(lineItemsDtoList);
		
		return order;
	}
}
