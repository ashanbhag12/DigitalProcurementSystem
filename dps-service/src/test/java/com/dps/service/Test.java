package com.dps.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test
{
	public static void main(String[] args)
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy-kk-mm-ss");
		String dateStr = df.format(date);
		System.out.println(dateStr);
	}
}
