package com.dps.web.service.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.dps.commons.domain.Constants;

public class ControllerUtils
{
	public static BigDecimal computeAbsoluteDiscount(BigDecimal percentDiscount)
	{
		if(percentDiscount == null || percentDiscount.equals(Constants.BIG_DECIMAL_ZERO))
		{
			return Constants.BIG_DECIMAL_ONE;
		}
		
		BigDecimal absoluteDiscount = percentDiscount.divide(Constants.BIG_DECIMAL_HUNDRED, RoundingMode.HALF_UP);
		absoluteDiscount = Constants.BIG_DECIMAL_ONE.subtract(absoluteDiscount);
		
		if(percentDiscount.signum() == 1)
		{
			absoluteDiscount = Constants.BIG_DECIMAL_ONE.divide(absoluteDiscount, RoundingMode.HALF_UP);
		}
		
		return absoluteDiscount;
	}
}
