package com.dps.web.service.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.constants.CustomerOrderDetailStatus;
import com.dps.domain.entity.Configurations;
import com.dps.domain.entity.CustomerOrderDetails;
import com.dps.domain.entity.Product;
import com.dps.domain.entity.Supplier;
import com.dps.domain.entity.SupplierOrder;
import com.dps.domain.entity.SupplierOrderDetails;
import com.dps.service.ConfigurationsService;
import com.dps.service.CustomerOrderDetailsService;
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
			
			List<PlaceOrderDTO> orders = ordersPerSupplier.get(supplierInitials);
			List<SupplierOrderDetails> supplierOrderDetailList = new ArrayList<>();
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
						supplierOrderDetailList.add(suppOrderDet);
					}
				}
				
			}
			
			supplierOrder.setSupplierOrderDetails(supplierOrderDetailList);
			supplierOrderList.add(supplierOrder);
		}
		
		supplierOrderService.persistAll(supplierOrderList);
		custOrderDetService.mergeAll(custOrderDetList);
		
		createExcel(ordersPerSupplier, config);
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
			
			//Create header Row
			Row row = sheet.createRow(0);
			Cell cell = row.createCell(0);
			cell.setCellValue("Product Code");
			cell = row.createCell(1);
			cell.setCellValue("Quantity");
			cell = row.createCell(2);
			cell.setCellValue("Remark");
			cell = row.createCell(3);
			cell.setCellValue("Customer Information");
			
			int rowNumber = 1;
			//Set rest of the rows from Data:
			for(PlaceOrderDTO order : ordersPerSupplier.get(suppInitials))
			{
				row = sheet.createRow(rowNumber++);
				cell = row.createCell(0);
				cell.setCellValue(order.getProductCode());
				cell = row.createCell(1);
				cell.setCellValue(order.getQuantity());
				cell = row.createCell(2);
				cell.setCellValue(order.getRemarks());
				cell = row.createCell(3);
				cell.setCellValue(order.getCustomerDetails());
			}
			
			String filePath = config.getBasePath() + "supplier" + File.separator;
			filePath = filePath + suppInitials + "_" + dateStr  + ".xls";
			File file = new File(filePath);
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		}
		
		return null;
	}
}
