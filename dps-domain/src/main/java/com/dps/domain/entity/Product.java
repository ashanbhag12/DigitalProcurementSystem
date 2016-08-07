package com.dps.domain.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

/**
 * This class holds product information.
 *
 * @see
 *
 * @Date 10-Jul-2016
 *
 * @author Akshay
 */
@Entity
@Table(name = "DPS_PROD")
//@SequenceGenerator(name = "DPS_PROD_SEQ", sequenceName = "DPS_PROD_SEQ", initialValue = 1, allocationSize = 1)
@NamedQueries({
	@NamedQuery(name=Product.FIND_PRODUCT_BY_CODE, query="SELECT p.id from Product p where p.productCode like :code"),
	@NamedQuery(name=Product.GET_ALL_PRODUCT_CODE, query="SELECT p.productCode from Product p")
})
public class Product extends EntityBase
{
	private static final long serialVersionUID = 1L;
	public static final String FIND_PRODUCT_BY_CODE = "Product.FindProductByCode";
	public static final String GET_ALL_PRODUCT_CODE = "Product.GetAllProductCode";

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DPS_PROD_SEQ")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Basic
	@Column(name="PROD_CODE")
	private String productCode;

	@Basic
	private BigDecimal price;
	
	@Basic
	@Column(name="CRTN_QNTY")
	private Integer cartoonQuantity;
	
	@Basic
	private BigDecimal cbm;

	@Basic
	private BigDecimal weight;
	
	@Basic
	private String description;

	@Basic
	private Integer moq;

	@Basic
	@Column(name="IS_VALID")
	private Boolean isValid;
	
	@Basic
	@Column(name="DEFAULT_MARGIN")
	private BigDecimal defaultMargin;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@OrderColumn(name="PREF_NO")
	private List<SupplierProductInfo> suppProdInfo;
	
	public Long getId()
	{
		return id;
	}

	

	public String getProductCode()
	{
		return productCode;
	}



	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}



	public BigDecimal getPrice()
	{
		return price;
	}



	public void setPrice(BigDecimal price)
	{
		this.price = price;
	}



	public Integer getCartoonQuantity()
	{
		return cartoonQuantity;
	}



	public void setCartoonQuantity(Integer cartoonQuantity)
	{
		this.cartoonQuantity = cartoonQuantity;
	}



	public BigDecimal getCbm()
	{
		return cbm;
	}



	public void setCbm(BigDecimal cbm)
	{
		this.cbm = cbm;
	}



	public BigDecimal getWeight()
	{
		return weight;
	}



	public void setWeight(BigDecimal weight)
	{
		this.weight = weight;
	}



	public String getDescription()
	{
		return description;
	}



	public void setDescription(String description)
	{
		this.description = description;
	}



	public Integer getMoq()
	{
		return moq;
	}



	public void setMoq(Integer moq)
	{
		this.moq = moq;
	}



	public Boolean getIsValid()
	{
		return isValid;
	}



	public void setIsValid(Boolean isValid)
	{
		this.isValid = isValid;
	}



	public BigDecimal getDefaultMargin()
	{
		return defaultMargin;
	}



	public void setDefaultMargin(BigDecimal defaultMargin)
	{
		this.defaultMargin = defaultMargin;
	}



	public List<SupplierProductInfo> getSuppProdInfo()
	{
		return suppProdInfo;
	}



	public void setSuppProdInfo(List<SupplierProductInfo> suppProdInfo)
	{
		this.suppProdInfo = suppProdInfo;
	}



	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((productCode == null) ? 0 : productCode.hashCode());
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
		Product other = (Product) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (productCode == null)
		{
			if (other.productCode != null)
				return false;
		}
		else if (!productCode.equals(other.productCode))
			return false;
		return true;
	}
}
