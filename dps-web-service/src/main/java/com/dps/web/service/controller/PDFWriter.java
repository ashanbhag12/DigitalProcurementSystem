package com.dps.web.service.controller;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * TODO Class Description goes here
 *
 * @see
 *
 * @Date 04-Sep-2016
 *
 * @author akshay
 */
public class PDFWriter
{
	public static void main(String[] args)
	{
		Document document = new Document();
		try
		{
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("AddImageExample.pdf"));
			document.open();
			document.add(new Paragraph("Image Example"));
			
			PdfPTable table = new PdfPTable(3); // 3 columns.
			table.setWidthPercentage(100); //Width 100%
			table.setSpacingBefore(10f); //Space before table
			table.setSpacingAfter(10f); //Space after table
			
			float[] columnWidths = {table.getWidthPercentage()/3, table.getWidthPercentage()/3, table.getWidthPercentage()/3};
			table.setWidths(columnWidths);
			
			PdfPCell cell = new PdfPCell();
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);	
			cell.setPadding(10f);
			Paragraph para = new Paragraph("Image 1");
			cell.addElement(para);
			Image image = Image.getInstance("/users/akshay/Downloads/java1.png");
			image.scaleAbsolute(100f, 75f);
			image.setBorderWidth(2);
			image.setBorder(Rectangle.BOX);
			cell.addElement(image);
			table.addCell(cell);
			
			cell = new PdfPCell();
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(10f);
			para = new Paragraph("Image 2");
			cell.addElement(para);
			image = Image.getInstance("/users/akshay/Downloads/java2.png");
			image.scaleAbsolute(100f, 75f);
			image.setBorderWidth(2);
			image.setBorder(Rectangle.BOX);
			cell.addElement(image);
			table.addCell(cell);
			
			cell = new PdfPCell();
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			cell.setPadding(10f);
			para = new Paragraph("Image 3");
			cell.addElement(para);
			image = Image.getInstance("/users/akshay/Downloads/java3.png");
			image.scaleAbsolute(100f, 75f);
			image.setBorderWidth(2);
			image.setBorder(Rectangle.BOX);
			cell.addElement(image);
			table.addCell(cell);
			
			document.add(table);
	
			document.close();
			writer.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
