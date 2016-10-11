package com.dps.web.service.controller;

import java.io.File;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.dps.domain.entity.Configurations;
import com.dps.service.ConfigurationsService;
import com.dps.web.service.model.ConfigDTO;

/**
 * Controller class that handles all the web service request for config changes.
 *
 * @see
 *
 * @Date 15-Aug-2016
 *
 * @author akshay
 */
@Path("/config")
public class ConfigController
{
	@Autowired
	private ConfigurationsService configService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ConfigDTO getConfigurations()
	{
		Configurations config = configService.getConfigurations();
		
		ConfigDTO configDto = new ConfigDTO();
		if(config != null)
		{
			configDto.setCostPerCbm(config.getPricePerCbm());
			configDto.setCostPerGrossWeight(config.getPricePerWeight());
			configDto.setExchangeRate(config.getExchangeRate());
			configDto.setBasePath(config.getBasePath());
		}
		
		return configDto;
	}
	
	@POST
	@Path("/modify")
	public void updateConfigurations(ConfigDTO configDto)
	{
		Configurations config = configService.getConfigurations();
		
		config.setExchangeRate(configDto.getExchangeRate());
		config.setPricePerWeight(configDto.getCostPerGrossWeight());
		config.setPricePerCbm(configDto.getCostPerCbm());
		
		String basePath = configDto.getBasePath();
		
		if(!basePath.endsWith(File.separator))
		{
			basePath = basePath + File.separator;
		}
		config.setBasePath(basePath);
		
		configService.merge(config);
	}
}
