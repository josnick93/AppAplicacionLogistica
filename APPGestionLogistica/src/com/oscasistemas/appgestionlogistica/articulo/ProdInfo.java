package com.oscasistemas.appgestionlogistica.articulo;

import java.sql.Date;

public class ProdInfo {
//	private String nOrden,articleCode,articleAlias, reference, client,number;
//	private Date recvD, sendD;
//	
	private String ordenProduccion,cliente,referencia,usuario;
	private Date envio,recepcion;
//	public ProdInfo(String nOrden, String articleCode,String articleAlias,String reference, String client, Date recvD, Date sendD) {
//
//		this.nOrden = nOrden;
//		this.articleCode=articleCode;
//		this.articleAlias=articleAlias;
//		this.reference = reference;
//		this.client = client;
//		this.recvD = recvD;
//		this.sendD = sendD;
//	}
	
//	public ProdInfo(String nOrden, String articleCode,String articleAlias,String reference, String client, Date recvD, Date sendD,String number) {
//
//		this.nOrden = nOrden;
//		this.articleCode=articleCode;
//		this.articleAlias=articleAlias;
//		this.reference = reference;
//		this.client = client;
//		this.recvD = recvD;
//		this.sendD = sendD;
//		this.number=number;
//	}
	
	public ProdInfo(String nOrden, String cliente,String referencia, String usuario, Date sendD ,Date recvD) {

		this.ordenProduccion=nOrden;
		this.cliente=cliente;
		this.referencia=referencia;
		this.usuario=usuario;
		this.envio=sendD;
		this.recepcion=recvD;
	}
	
	public String getCliente() {
		return cliente;
	}public Date getEnvio() {
		return envio;
	}public String getOrdenProduccion() {
		return ordenProduccion;
	}public Date getRecepcion() {
		return recepcion;
	}public String getReferencia() {
		return referencia;
	}public String getUsuario() {
		return usuario;
	}
}
