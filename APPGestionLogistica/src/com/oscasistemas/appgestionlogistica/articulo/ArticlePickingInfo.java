package com.oscasistemas.appgestionlogistica.articulo;

public class ArticlePickingInfo {
	private String codigoArticulo;
	private String alias, cantidad;
	private int color,posicion,registro;
	private byte estado;

	public ArticlePickingInfo(String codigoArticulo, String alias, String cantidad, int color) {
		this.codigoArticulo = codigoArticulo;
		this.alias = alias;
		this.cantidad = cantidad;
		this.color=color;
		this.estado=0;
		this.posicion=-1;
		this.registro=-1;


	}
	
	public ArticlePickingInfo(String codigoArticulo, String alias, String cantidad, int color , byte estado) {
		this.codigoArticulo = codigoArticulo;
		this.alias = alias;
		this.cantidad = cantidad;
		this.color=color;
		this.estado=estado;
		this.posicion=-1;
		this.registro=-1;

	}
	
	public ArticlePickingInfo(String codigoArticulo, String alias, String cantidad, int color , byte estado , int posicion , int registro) {
		this.codigoArticulo = codigoArticulo;
		this.alias = alias;
		this.cantidad = cantidad;
		this.color=color;
		this.estado=estado;
		this.posicion=posicion;
		this.registro=registro;

	}

	public String getCodigoArticulo() {
		return this.codigoArticulo;
	}

	public String getAlias() {
		return this.alias;
	}

	public String getCantidad() {
		return this.cantidad;
	}

	public int getColor() {
		return this.color;
	}
	
	public byte getEstado() {
		return this.estado;
	}

	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}
	public int getPosicion() {
		return this.posicion;
	}public int getRegistro() {
		return this.registro;
	}
}
