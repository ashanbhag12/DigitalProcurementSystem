package com.dps.commons.domain;

import java.math.BigDecimal;

/**
 * This class contains values for all constants that will be used in the project.
 *
 * @see
 *
 * @Date Jul 16, 2016
 *
 * @author akshay
 */
public class Constants
{
	private Constants()
	{
		throw new RuntimeException("This is a utility class. Cannot instantiate");
	}
	
	public static final BigDecimal BIG_DECIMAL_ONE = new BigDecimal(1);
}
