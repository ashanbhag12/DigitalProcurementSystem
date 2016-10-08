package com.dps.domain.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * This class stores the default configurations that the entire application will need.
 *
 * @see
 *
 * @Date Jul 17, 2016
 *
 * @author akshay
 */
@Entity
@Table(name = "DPS_CONFIG")
public class Configurations extends EntityBase
{
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="DPS_CONFIG_ID", table="DPS_ID_GEN", pkColumnName="GEN_NAME",
					valueColumnName="GEN_VAL", pkColumnValue="DPS_CONFIG_ID")
	@GeneratedValue(strategy=GenerationType.TABLE, generator="DPS_CONFIG_ID")
	private Long id;
	
	@Column(name="CBM_RATE")
	private BigDecimal pricePerCbm;
	
	@Column(name="FX_RATE")
	private BigDecimal exchangeRate;
	
	@Column(name="WEIGHT_RATE")
	private BigDecimal pricePerWeight;
	
	@Column(name="BASE_PATH")
	private String basePath;

	public BigDecimal getPricePerCbm()
	{
		return pricePerCbm;
	}

	public void setPricePerCbm(BigDecimal pricePerCbm)
	{
		this.pricePerCbm = pricePerCbm;
	}

	public BigDecimal getExchangeRate()
	{
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate)
	{
		this.exchangeRate = exchangeRate;
	}

	public BigDecimal getPricePerWeight()
	{
		return pricePerWeight;
	}

	public void setPricePerWeight(BigDecimal pricePerWeight)
	{
		this.pricePerWeight = pricePerWeight;
	}

	public Long getId()
	{
		return id;
	}

	public String getBasePath()
	{
		return basePath;
	}

	public void setBasePath(String basePath)
	{
		this.basePath = basePath;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((exchangeRate == null) ? 0 : exchangeRate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((pricePerCbm == null) ? 0 : pricePerCbm.hashCode());
		result = prime * result
				+ ((pricePerWeight == null) ? 0 : pricePerWeight.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configurations other = (Configurations) obj;
		if (exchangeRate == null)
		{
			if (other.exchangeRate != null)
				return false;
		}
		else if (!exchangeRate.equals(other.exchangeRate))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (pricePerCbm == null)
		{
			if (other.pricePerCbm != null)
				return false;
		}
		else if (!pricePerCbm.equals(other.pricePerCbm))
			return false;
		if (pricePerWeight == null)
		{
			if (other.pricePerWeight != null)
				return false;
		}
		else if (!pricePerWeight.equals(other.pricePerWeight))
			return false;
		return true;
	}
}
