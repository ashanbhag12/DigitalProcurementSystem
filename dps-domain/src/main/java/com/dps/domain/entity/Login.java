package com.dps.domain.entity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * This class holds the login information.
 *
 * @see
 *
 * @Date 17-Sep-2016
 *
 * @author akshay
 */
@Entity
@Table(name = "DPS_LOGIN")
public class Login  extends EntityBase
{
	private static final long serialVersionUID = 1L;

	@Id
	@TableGenerator(name="DPS_LOGIN_ID", table="DPS_ID_GEN", pkColumnName="GEN_NAME",
					valueColumnName="GEN_VAL", pkColumnValue="DPS_LOGIN_ID")
	@GeneratedValue(strategy=GenerationType.TABLE, generator="DPS_LOGIN_ID")
	private Long id;
	
	@Basic
	private String username;
	
	@Basic
	private String password;

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Long getId()
	{
		return id;
	}
}
