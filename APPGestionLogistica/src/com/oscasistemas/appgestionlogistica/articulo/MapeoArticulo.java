package com.oscasistemas.appgestionlogistica.articulo;

import java.io.Serializable;

public class MapeoArticulo implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2724973018977045148L;
	private String name,description;
	private int code,avaiable,size;
	public MapeoArticulo(int code,String name,String description,int avaiable,int size) {
		// TODO Auto-generated constructor stub
		this.code=code;
		this.name=name;
		this.description=description;
		this.avaiable=avaiable;
		this.size=size;
	}
	public int getCode() {
		return this.code;
	}public String getName() {
		return this.name;
	}public String getDescription() {
		return  this.description;
	}public int getAvaiable() {
		return  this.avaiable;
	}public int getSize() {
		return  this.size;
	}
	
	
	public void setCode(int code) {
		this.code = code;
	}public void setName(String name) {
		this.name = name;
	}public void setDescription(String description) {
		this.description = description;
	}public void setAvaiable(int avaiable) {
		this.avaiable = avaiable;
	}public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Name "+this.name+" Avaiable: "+avaiable+" Total: "+this.size;
	}
}
