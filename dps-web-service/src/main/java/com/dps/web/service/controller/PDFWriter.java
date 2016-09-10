package com.dps.web.service.controller;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
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
			
			PdfPTable table = new PdfPTable(2); // 3 columns.
			table.setWidthPercentage(100); //Width 100%
			table.setSpacingBefore(10f); //Space before table
			table.setSpacingAfter(10f); //Space after table
			
			float[] columnWidths = {1f, 1f};
			table.setWidths(columnWidths);
			
			PdfPCell cell1 = new PdfPCell(new Paragraph("Image 1"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell1);
			
			cell1 = new PdfPCell(new Paragraph("Image 2"));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell1);
			
			Image image1 = Image.getInstance("/users/akshay/Downloads/java1.png");
			image1.scaleAbsolute(100, 100);
			cell1 = new PdfPCell(image1);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell1);
			
			Image image2 = Image.getInstance("/users/akshay/Downloads/java2.png");
			image2.scaleAbsolute(100, 100);
			cell1 = new PdfPCell(image2);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cell1);
			
			document.add(table);
	
			document.close();
			writer.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
