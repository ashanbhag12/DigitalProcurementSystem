package com.dps.web.service.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.Constants;
import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.constants.CustomerOrderDetailStatus;
import com.dps.domain.constants.CustomerOrderStatus;
import com.dps.domain.constants.SupplierOrderDetailStatus;
import com.dps.domain.constants.SupplierOrderStatus;
import com.dps.domain.entity.Configurations;
import com.dps.domain.entity.CustomerOrder;
import com.dps.domain.entity.CustomerOrderDetails;
import com.dps.domain.entity.Product;
import com.dps.domain.entity.Supplier;
import com.dps.domain.entity.SupplierOrder;
import com.dps.domain.entity.SupplierOrderDetails;
import com.dps.domain.entity.SupplierProductInfo;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerOrderDetailsService;
import com.dps.service.CustomerOrderService;
import com.dps.service.SupplierOrderService;
import com.dps.service.SupplierService;
import com.dps.web.service.model.PlaceOrderDTO;

/**
 * Controller class that handles all the place supplier order requests.
 *
 * @see
 *
 * @Date 21-Aug-2016
 *
 * @author akshay
 */
@Path("/placeorder")
public class PlaceOrderController
{
	@Autowired
	private CustomerOrderDetailsService custOrderDetService;
	
	@Autowired
	private CustomerOrderService customerOrderService;
	
	@Autowired
	private ConfigurationsService configService;
	
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private SupplierOrderService supplierOrderService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<PlaceOrderDTO> displayUnorderedProducts()
	{
		List<CustomerOrderDetails> unorderedItems = custOrderDetService.getUnorderedProducts();
		Map<Long, List<CustomerOrderDetails>> ordersPerProducts = new HashMap<>();
		List<PlaceOrderDTO> resultList = new ArrayList<>();
		
		for(CustomerOrderDetails unorderedItem : unorderedItems)
		{
			Long productId = unorderedItem.getProduct().getId();
			List<CustomerOrderDetails> list = ordersPerProducts.get(productId);
			if(list == null)
			{
				list = new ArrayList<>();
				ordersPerProducts.put(productId, list);
			}
			list.add(unorderedItem);
		}
		
		for(Long porductId : ordersPerProducts.keySet())
		{
			List<CustomerOrderDetails> orders = ordersPerProducts.get(porductId);
			Product product = orders.get(0).getProduct();
			
			Map<String, List<CustomerOrderDetails>> supplierInitialsMap = new HashMap<>();
			for(CustomerOrderDetails det : orders)
			{
				String suppInitials = det.getSupplier().getInitials();
				List<CustomerOrderDetails> list = supplierInitialsMap.get(suppInitials);
				if(list == null)
				{
					list = new ArrayList<>();
					supplierInitialsMap.put(suppInitials, list);
				}
				list.add(det);
			}
			
			for(String suppInitials : supplierInitialsMap.keySet())
			{
				List<CustomerOrderDetails> details = supplierInitialsMap.get(suppInitials);
				StringBuilder remark = new StringBuilder();
				StringBuilder cust = new StringBuilder();
				StringBuilder ids = new StringBuilder();
				int quantity = 0;
				
				for(CustomerOrderDetails det : details)
				{
					if(StringUtils.isNotBlank(det.getRemarks()))
					{
						remark.append(det.getRemarks());
						remark.append(", ");
					}
					cust.append(det.getCustomerOrder().getCustomer().getShipmark());
					cust.append(":");
					cust.append(det.getQuantity());
					cust.append(", ");
					ids.append(det.getId().toString());
					ids.append(",");
					quantity += det.getQuantity();
				}
				
				PlaceOrderDTO order = new PlaceOrderDTO();
				if(cust.length() > 2)
				{
					order.setCustomerDetails(cust.toString().substring(0, cust.toString().length()-2));
				}
				if(remark.length() > 2)
				{
					order.setRemarks(remark.toString().substring(0, remark.toString().length()-2));
				}
				order.setMoq(product.getMoq());
				order.setProductCode(product.getProductCode());
				order.setProductId(product.getId());
				order.setQuantity(quantity);
				order.setSupplierInitials(suppInitials);
				order.setToOrder(quantity >= product.getMoq());
				order.setIdList(ids.toString());
				order.setProductDescription(product.getDescription());
				order.setGw(product.getWeight());
				order.setCbm(product.getCbm());
				order.setPackageing(product.getCartoonQuantity());
				
				BigDecimal price = Constants.BIG_DECIMAL_ONE;
				
				for(SupplierProductInfo spi : product.getSuppProdInfo())
				{
					if(StringUtils.equals(spi.getSupplier().getInitials(), suppInitials))
					{
						price = spi.getSupplierPrice();
						break;
					}
				}
				
				order.setPricePerItem(price);
				
				for(SupplierProductInfo suppProdInfo : product.getSuppProdInfo())
				{
					if(StringUtils.equalsIgnoreCase(suppInitials, suppProdInfo.getSupplier().getInitials()))
					{
						order.setSupplierProductCode(suppProdInfo.getSupplierProductName());
					}
				}
				
				resultList.add(order);
			}
					
		}
		return resultList;
	}
	
	@POST
	@Path("/save")
	public void saveSupplierOrder(List<PlaceOrderDTO> orderList) throws IOException
	{
		List<JpaEntityId> idList = new ArrayList<>();
		Map<String, List<PlaceOrderDTO>> ordersPerSupplier = new HashMap<>();
		
		for(PlaceOrderDTO order : orderList)
		{
			if(order.isToOrder())
			{
				String supplierInitials = order.getSupplierInitials();
				List<PlaceOrderDTO> list = ordersPerSupplier.get(supplierInitials);
				if(list == null)
				{
					list = new ArrayList<>();
					ordersPerSupplier.put(supplierInitials, list);
				}
				list.add(order);
				
				String[] idString = order.getIdList().split(",");
				for(String id : idString)
				{
					if(StringUtils.isNotBlank(id))
					{
						idList.add(new JpaEntityId(Long.parseLong(id)));
					}
				}
			}
		}
		
		List<CustomerOrderDetails> custOrderDetList = custOrderDetService.findAll(idList);
		Configurations config = configService.findAll().get(0);
		List<Supplier> suppliersList = supplierService.findAll();
		
		Map<String, Supplier> suppliersMap = new HashMap<>();
		for(Supplier s : suppliersList)
		{
			suppliersMap.put(s.getInitials(), s);
		}
		
		
		Map<Long, CustomerOrderDetails> custOrderDetMap = new HashMap<>();
		
		for(CustomerOrderDetails custOrderDetails : custOrderDetList)
		{
			custOrderDetMap.put(custOrderDetails.getId(), custOrderDetails);
		}
		
		List<SupplierOrder> supplierOrderList = new ArrayList<>();
		for(String supplierInitials : ordersPerSupplier.keySet())
		{
			SupplierOrder supplierOrder = new SupplierOrder();
			supplierOrder.setCbmRate(config.getPricePerCbm());
			supplierOrder.setExchangeRate(config.getExchangeRate());
			supplierOrder.setWeightRate(config.getPricePerWeight());
			supplierOrder.setOrderDate(new Date());
			supplierOrder.setSupplier(suppliersMap.get(supplierInitials));
			supplierOrder.setStatus(SupplierOrderStatus.ORDER_PLACED);
			
			List<PlaceOrderDTO> orders = ordersPerSupplier.get(supplierInitials);
			List<SupplierOrderDetails> supplierOrderDetailList = new ArrayList<>();
			Set<CustomerOrder> customerOrders = new HashSet<>();
			for(PlaceOrderDTO order : orders)
			{
				String[] idString = order.getIdList().split(",");
				for(String id : idString)
				{
					if(StringUtils.isNotBlank(id))
					{
						SupplierOrderDetails suppOrderDet = new SupplierOrderDetails();
						CustomerOrderDetails custOrderDet = custOrderDetMap.get(Long.parseLong(id));
						custOrderDet.setStatus(CustomerOrderDetailStatus.ORDER_SENT);
						suppOrderDet.setCustomerOrderDetails(custOrderDet);
						suppOrderDet.setSupplierOrder(supplierOrder);
						suppOrderDet.setStatus(SupplierOrderDetailStatus.ITEM_NOT_RECEIVED);
						supplierOrderDetailList.add(suppOrderDet);
						customerOrders.add(custOrderDet.getCustomerOrder());
					}
				}
				
			}
			
			supplierOrder.setSupplierOrderDetails(supplierOrderDetailList);
			supplierOrderList.add(supplierOrder);
			
			for(CustomerOrder custOrder : customerOrders)
			{
				int unorderedCount = 0;
				for(CustomerOrderDetails det : custOrder.getLineItems())
				{
					if(det.getStatus() == CustomerOrderDetailStatus.NOT_ORDERED)
					{
						unorderedCount ++;
					}
				}
				 
				if(unorderedCount == 0)
				{
					custOrder.setStatus(CustomerOrderStatus.ALL_ITEMS_ORDERED);
				}
				else
				{
					custOrder.setStatus(CustomerOrderStatus.SOME_ITEMS_ORDERED);
				}
			}
			
			customerOrderService.mergeAll(customerOrders);
		}
		
		createExcel(ordersPerSupplier, config);
		
		supplierOrderService.persistAll(supplierOrderList);
		custOrderDetService.mergeAll(custOrderDetList);
	}

	private HSSFWorkbook createExcel(Map<String, List<PlaceOrderDTO>> ordersPerSupplier, Configurations config) throws IOException
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String dateStr = df.format(date);
		
		for(String suppInitials : ordersPerSupplier.keySet())
		{
			//Create a new sheet for each supplier
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet(suppInitials);
			
			CellStyle headerCellStyle = createHeaderCellStyle(workbook);
			CellStyle cellStyle = createCellStyle(workbook);
			
			//Create header Row
			Row row = sheet.createRow(0);
			
			Cell cell = row.createCell(0);
			cell.setCellValue("SC");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(1);
			cell.setCellValue("Product Code");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(2);
			cell.setCellValue("Product description");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(3);
			cell.setCellValue("Carton");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(4);
			cell.setCellValue("Packaging");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(5);
			cell.setCellValue("Price");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(6);
			cell.setCellValue("CBM");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(7);
			cell.setCellValue("GW");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(8);
			cell.setCellValue("Remark");
			cell.setCellStyle(headerCellStyle);
			
			cell = row.createCell(9);
			cell.setCellValue("Shipmark");
			cell.setCellStyle(headerCellStyle);
			
			int rowNumber = 1;
			//Set rest of the rows from Data:
			for(PlaceOrderDTO order : ordersPerSupplier.get(suppInitials))
			{
				row = sheet.createRow(rowNumber++);
				
				cell = row.createCell(0);
				cell.setCellValue(order.getSupplierProductCode());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(1);
				cell.setCellValue(order.getProductCode());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(2);
				cell.setCellValue(order.getProductDescription());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(3);
				cell.setCellValue(order.getQuantity());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(4);
				cell.setCellValue(order.getPackageing());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(5);
				cell.setCellValue(order.getPricePerItem().setScale(3, RoundingMode.HALF_UP).toString());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(6);
				cell.setCellValue(order.getCbm().setScale(3, RoundingMode.HALF_UP).toString());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(7);
				cell.setCellValue(order.getGw().setScale(3, RoundingMode.HALF_UP).toString());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(8);
				cell.setCellValue(order.getRemarks());
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(9);
				cell.setCellValue(order.getCustomerDetails());
				cell.setCellStyle(cellStyle);
			}
			
			for(int i = 0; i < 7; i++)
			{
				sheet.autoSizeColumn(i);
			}
			
			String basePath = config.getBasePath();
			if(!basePath.endsWith(File.separator))
			{
				basePath = basePath + File.separator;
			}
			
			String filePath = basePath + "supplier";
			
			File dir = new File(filePath);
			if(!dir.exists())
			{
				dir.mkdir();
			}
			
			filePath = filePath + File.separator + suppInitials + "_" + dateStr  + ".xls";
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		}
		
		return null;
	}
	
	private CellStyle createHeaderCellStyle(HSSFWorkbook workbook)
	{
		CellStyle cellStyle = workbook.createCellStyle();
		
		//Set margins
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		
		//Set alignments:
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		
		return cellStyle;
	}
	
	private CellStyle createCellStyle(HSSFWorkbook workbook)
	{
		return createHeaderCellStyle(workbook);
	}
}
