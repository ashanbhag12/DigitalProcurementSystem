package com.dps.web.service.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.Configurations;
import com.dps.domain.entity.Product;
import com.dps.domain.entity.SupplierOrder;
import com.dps.domain.entity.SupplierOrderDetails;
import com.dps.domain.entity.SupplierProductInfo;
import com.dps.service.ConfigurationsService;
import com.dps.service.SupplierOrderService;
import com.dps.web.service.model.ViewSupplierOrderDTO;
import com.dps.web.service.model.ViewSupplierOrderDetailsDTO;

/**
 * Controller class that handles all the web service requests for updating supplier orders operations.
 *
 * @see
 *
 * @Date Oct 16, 2016
 *
 * @author akshay
 */
@Path("/supplierorder")
public class SupplierOrderController
{
	@Autowired
	private SupplierOrderService supplierOrderService;
	
	@Autowired
	private ConfigurationsService configService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{supplierId}/{startDate}/{endDate}")
	public List<ViewSupplierOrderDTO> getSupplierOrders(@PathParam("supplierId") String supplierInit, @PathParam("startDate") Long startDate, @PathParam("endDate") Long endDate)
	{
		List<SupplierOrder> supplierOrders = supplierOrderService.getSupplierOrders(supplierInit, new Date(startDate), new Date(endDate));
		List<ViewSupplierOrderDTO> suppOrderDtoList = new ArrayList<>();
		
		for(SupplierOrder suppOrder : supplierOrders)
		{
			suppOrderDtoList.add(buildDTO(suppOrder));
		}
		return suppOrderDtoList;
	}
	
	@GET
	@Path("{id}")
	public void createExcel(@PathParam("id") long id)
	{
		SupplierOrder suppOrder = supplierOrderService.find(new JpaEntityId(id));
		ViewSupplierOrderDTO dto = buildDTO(suppOrder);
		createExcel(dto);
	}
	
	private void createExcel(ViewSupplierOrderDTO dto)
	{
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy-kk-mm-ss");
		String dateStr = df.format(dto.getOrderDate());
		
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(dto.getSupplierInitials());
		
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
		
		for(ViewSupplierOrderDetailsDTO det : dto.getDetails())
		{
			row = sheet.createRow(rowNumber++);
			
			cell = row.createCell(0);
			cell.setCellValue(det.getSupplierProductCode());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(1);
			cell.setCellValue(det.getProductCode());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(2);
			cell.setCellValue(det.getProductDescription());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(3);
			cell.setCellValue(det.getQuantity());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(4);
			cell.setCellValue(det.getPackageing());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(5);
			cell.setCellValue(det.getPricePerItem().setScale(3, RoundingMode.HALF_UP).toString());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(6);
			cell.setCellValue(det.getCbm().setScale(3, RoundingMode.HALF_UP).toString());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(7);
			cell.setCellValue(det.getGw().setScale(3, RoundingMode.HALF_UP).toString());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(8);
			cell.setCellValue(det.getRemarks());
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(9);
			cell.setCellValue(det.getCustomerDetails());
			cell.setCellStyle(cellStyle);
		}
		
		for(int i = 0; i < 7; i++)
		{
			sheet.autoSizeColumn(i);
		}
		
		Configurations config = configService.findAll().get(0);
		
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
		
		filePath = filePath + File.separator + dto.getSupplierInitials() + "_" + dateStr  + ".xls";
		File file = new File(filePath);
		FileOutputStream out;
		try
		{
			out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
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
	
	private ViewSupplierOrderDTO buildDTO(SupplierOrder suppOrder)
	{

		ViewSupplierOrderDTO suppOrderDto = new ViewSupplierOrderDTO();
		suppOrderDto.setId(suppOrder.getId());
		suppOrderDto.setOrderDate(suppOrder.getOrderDate());
		suppOrderDto.setStatus(suppOrder.getStatus().toString());
		suppOrderDto.setSupplierInitials(suppOrder.getSupplier().getInitials());
		
		List<SupplierOrderDetails> suppOrderDetails = suppOrder.getSupplierOrderDetails();
		Map<Long,List<SupplierOrderDetails>> productOrderMap = new HashMap<>();
		Map<Long, Product> productMap = new HashMap<>();
		List<ViewSupplierOrderDetailsDTO> viewSupplierOrderDetailsDtoList = new ArrayList<>();
		
		for(SupplierOrderDetails suppOrderDetail : suppOrderDetails)
		{
			Product product = suppOrderDetail.getCustomerOrderDetails().getProduct();
			List<SupplierOrderDetails> list = productOrderMap.get(product.getId());
			if(list == null)
			{
				list = new ArrayList<>();
			}
			list.add(suppOrderDetail);
			productOrderMap.put(product.getId(), list);
			productMap.put(product.getId(), product);
		}
		
		for(Long id : productOrderMap.keySet())
		{
			List<SupplierOrderDetails> suppOrderDetList = productOrderMap.get(id);
			StringBuffer customerDetails = new StringBuffer();
			StringBuffer remark = new StringBuffer();
			ViewSupplierOrderDetailsDTO suppOrderDetDto = new ViewSupplierOrderDetailsDTO();
			suppOrderDetDto.setProductCode(productMap.get(id).getProductCode());
			suppOrderDetDto.setProductDescription(productMap.get(id).getDescription());
			suppOrderDetDto.setCbm(productMap.get(id).getCbm());
			suppOrderDetDto.setGw(productMap.get(id).getWeight());
			suppOrderDetDto.setPackageing(productMap.get(id).getCartoonQuantity());
			
			for(SupplierProductInfo suppProdInfo : productMap.get(id).getSuppProdInfo())
			{
				if(StringUtils.equals(suppOrder.getSupplier().getInitials(), suppProdInfo.getSupplier().getInitials()))
				{
					suppOrderDetDto.setPricePerItem(suppProdInfo.getSupplierPrice());
					suppOrderDetDto.setSupplierProductCode(suppProdInfo.getSupplierProductName());
				}
			}
			
			int quantity = 0;
			for(SupplierOrderDetails suppOrderDet : suppOrderDetList)
			{
				if(StringUtils.isNotBlank(suppOrderDet.getCustomerOrderDetails().getRemarks()))
				{
					remark = remark.append(suppOrderDet.getCustomerOrderDetails().getRemarks()).append(" ; ");
				}
				customerDetails.append(suppOrderDet.getCustomerOrderDetails().getCustomerOrder().getCustomer().getShipmark()).append(":");
				customerDetails.append(suppOrderDet.getCustomerOrderDetails().getQuantity()).append(" ; ");
				quantity += suppOrderDet.getCustomerOrderDetails().getQuantity();
			}
			
			if(customerDetails.length() > 2)
			{
				suppOrderDetDto.setCustomerDetails(customerDetails.toString().substring(0, customerDetails.toString().length()-2));
			}
			if(remark.length() > 2)
			{
				suppOrderDetDto.setRemarks(remark.toString().substring(0, remark.toString().length()-2));
			}
			suppOrderDetDto.setQuantity(quantity);
			viewSupplierOrderDetailsDtoList.add(suppOrderDetDto);
		}
		suppOrderDto.setDetails(viewSupplierOrderDetailsDtoList);
		
		return suppOrderDto;
	}
}
