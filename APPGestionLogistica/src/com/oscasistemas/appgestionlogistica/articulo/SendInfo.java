package com.oscasistemas.appgestionlogistica.articulo;

import java.sql.Date;

public class SendInfo {
	private String pedido,cliente_proveedor,referencia;
	private Date fechaRecivo,fechaEnvio;

	public SendInfo(String pedido, String cliente_proveedor, String refencia, Date sendDate, Date recvDate) {
		this.pedido=pedido;
		this.cliente_proveedor=cliente_proveedor;
		this.referencia=refencia;
		this.fechaEnvio=sendDate;
		this.fechaRecivo=recvDate;
		
	}

	public String getCliente_proveedor() {
		return cliente_proveedor;
	}public Date getFechaEnvio() {
		return fechaEnvio;
	}public Date getFechaRecivo() {
		return fechaRecivo;
	}public String getPedido() {
		return pedido;
	}public String getReferencia() {
		return referencia;
	}
}
