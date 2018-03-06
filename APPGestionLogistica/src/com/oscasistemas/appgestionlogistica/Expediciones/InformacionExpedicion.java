package com.oscasistemas.appgestionlogistica.Expediciones;

public class InformacionExpedicion {
	private String expedicion, idcliente,cliente, referencia, usuario;

	public InformacionExpedicion(String expedicion, String cliente, String referencia, String usuario) {

		this.expedicion = expedicion;
		this.cliente = cliente;
		this.idcliente= "-1";
		this.referencia = referencia;
		this.usuario = usuario;

	}
	
	public InformacionExpedicion(String expedicion,String idCliente, String cliente, String referencia, String usuario) {

		this.expedicion = expedicion;
		this.idcliente= idCliente;
		this.cliente = cliente;
		this.referencia = referencia;
		this.usuario = usuario;

	}

	public String getCliente() {
		return cliente;
	}
	
	public String getIdcliente() {
		return idcliente;
	}

	public String getExpedicion() {
		return expedicion;
	}

	public String getReferencia() {
		return referencia;
	}

	public String getUsuario() {
		return usuario;
	}
}
