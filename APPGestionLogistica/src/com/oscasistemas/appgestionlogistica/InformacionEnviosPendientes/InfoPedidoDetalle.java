package com.oscasistemas.appgestionlogistica.InformacionEnviosPendientes;


import com.MapeoBD.Pedidos.MapeoPedidoDetalle;

public class InfoPedidoDetalle {

	private boolean isCabecera = false;
	private String titulo, c1, c2, c3;
	private MapeoPedidoDetalle pedidoDetalle;

	public InfoPedidoDetalle(String titulo) {
		isCabecera = true;
		this.titulo = titulo;
		this.pedidoDetalle = null;
	}

	public InfoPedidoDetalle(String c1, String c2, String c3) {
		this.c1 = c1;
		this.c2 = c2;
		this.c3 = c3;
		this.pedidoDetalle = null;
	}
	public InfoPedidoDetalle(MapeoPedidoDetalle pd) {
		isCabecera = false;
		this.pedidoDetalle=pd;
	}

	public String getC1() {
		return c1;
	}
	
	public boolean isCabecera() {
		return isCabecera;
	}

	public String getC2() {
		return c2;
	}

	public String getC3() {
		return c3;
	}

	public MapeoPedidoDetalle getPedidoDetalle() {
		return pedidoDetalle;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public void setC3(String c3) {
		this.c3 = c3;
	}

	public void setCabecera(boolean isCabecera) {
		this.isCabecera = isCabecera;
	}

	public void setPedidoDetalle(MapeoPedidoDetalle pedidoDetalle) {
		this.pedidoDetalle = pedidoDetalle;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}
