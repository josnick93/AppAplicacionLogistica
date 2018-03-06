package com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion;

import java.io.Serializable;
import java.sql.SQLException;

import com.MapeoBD.Articulo.MapeoArticulo;
import com.MapeoBD.Ordenes.MapeoOrdenDetalle;
import com.MapeoBD.Picking.MapeoPickingDetalle;

import android.os.AsyncTask;

public class InformacionComponenteOrdenProd implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -5131555817017734040L;

	private String title;
	private Serializable objeto;

	public enum TipoComponente {
		TITULO, PICKING, COMPONENTE
	};

	private TipoComponente tipoComponente;

	public InformacionComponenteOrdenProd(String title) {
		this.tipoComponente = TipoComponente.TITULO;
		this.title = title;
		this.objeto = null;
	}

	public InformacionComponenteOrdenProd(MapeoOrdenDetalle orden) {
		tipoComponente = TipoComponente.COMPONENTE;
		this.objeto = orden;
	}

	public InformacionComponenteOrdenProd(MapeoPickingDetalle orden) {
		tipoComponente = TipoComponente.PICKING;
		this.objeto = orden;
	}

	public boolean isGroupHeader() {
		return this.tipoComponente == TipoComponente.TITULO;
	}
	
	public TipoComponente getTipoComponente() {
		return tipoComponente;
	}

	public String getTitle() {
		return title;
	}

	public Serializable getObjeto() {
		return objeto;
	}


}
