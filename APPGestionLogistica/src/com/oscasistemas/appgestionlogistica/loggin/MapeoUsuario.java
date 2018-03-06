package com.oscasistemas.appgestionlogistica.loggin;

import java.io.Serializable;

public class MapeoUsuario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int permission;
	private String name,pass;
	
	public MapeoUsuario(String name,String pass,int permission) {
		// TODO Auto-generated constructor stub
		this.name=name;
		this.pass=pass;
		this.permission=permission;
	}
	
	public String getName() {
		return name;
	}
	public String getPass() {
		return pass;
	}
	public int getPermission() {
		return permission;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void setPermission(int permission) {
		this.permission = permission;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "nombre: "+this.name +" contraseña: "+this.pass;
	}

}
