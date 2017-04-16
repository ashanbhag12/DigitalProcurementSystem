package com.dps.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.cache.impl.CacheImpl;
import com.dps.dao.LoginDao;
import com.dps.domain.entity.Login;
import com.dps.service.LoginService;

/**
 * Default implementation of {@link LoginService} interface.
 *
 * @see
 *
 * @Date 17-Sep-2016
 *
 * @author akshay
 */
public class LoginServiceImpl  extends BaseServiceImpl<Login> implements LoginService
{
	@Autowired
	private LoginDao loginDao;

	@PostConstruct
	@Override
	protected void init()
	{
		super.setDao(loginDao);
	}

	@Override
	protected void initializeCache()
	{
		cache = new CacheImpl<>(0);
	}

}
