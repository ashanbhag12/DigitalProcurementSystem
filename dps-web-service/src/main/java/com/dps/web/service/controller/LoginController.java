package com.dps.web.service.controller;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dps.domain.entity.Login;
import com.dps.service.LoginService;
import com.dps.web.service.model.LoginDTO;

/**
 * Controller class that handles all the login interactions
 *
 * @see
 *
 * @Date 17-Sep-2016
 *
 * @author akshay
 */
@Path("/login")
public class LoginController
{
	@Autowired
	private LoginService loginService;
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Boolean validateCredentials(LoginDTO loginDto)
	{
		List<Login> loginList = loginService.findAll();
		
		for(Login login : loginList)
		{
			if(StringUtils.equalsIgnoreCase(loginDto.getUsername(), login.getUsername()))
			{
				if(StringUtils.equals(loginDto.getPassword(), login.getPassword()))
				{
					return true;
				}
				else
				{
					break;
				}
			}
		}
		
		return false;
	}
}
