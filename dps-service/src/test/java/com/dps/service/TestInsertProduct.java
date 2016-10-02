package com.dps.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dps.commons.domain.JpaEntityId;
import com.dps.domain.entity.Product;
import com.dps.domain.entity.Supplier;
import com.dps.domain.entity.SupplierProductInfo;

/**
 * TODO Enter type description here
 *
 * @see
 *
 * @Date Jul 30, 2016
 *
 * @author akshay
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-context-dps-service-test.xml"})
public class TestInsertProduct
{
	@Autowired
	private SupplierService supplierService;
	
	@Autowired
	private ProductService productService;
	
	private static final BigDecimal ONE = new BigDecimal(1);
	
	@Test
	public void Test1()
	{
		Supplier supp1 = supplierService.find(new JpaEntityId(1L));
		Supplier supp2 = supplierService.find(new JpaEntityId(2L));
		
		Product prod = new Product();
		prod.setCartoonQuantity(1);
		prod.setCbm(ONE);
		prod.setDefaultMargin(ONE);
		prod.setDescription("Test");
		prod.setIsValid(true);
		prod.setMoq(1);
		//prod.setPrice(ONE);
		prod.setProductCode("Test4");
		prod.setWeight(ONE);
		
		SupplierProductInfo s1= new SupplierProductInfo();
		s1.setProduct(prod);
		s1.setSupplier(supp1);
		s1.setSupplierProductName(supp1.getInitials() + "_" + prod.getProductCode());
		
		SupplierProductInfo s2= new SupplierProductInfo();
		s2.setProduct(prod);
		s2.setSupplier(supp2);
		s2.setSupplierProductName(supp2.getInitials() + "_" + prod.getProductCode());
		
		List<SupplierProductInfo> spiList = new ArrayList<>();
		spiList.add(s1);
		spiList.add(s2);
		
		prod.setSuppProdInfo(spiList);
		
		productService.persist(prod);
	}
}
