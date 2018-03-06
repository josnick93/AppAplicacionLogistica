package com.oscasistemas.appgestionlogistica.informacionArticulo;

import java.io.Serializable;

import java.sql.SQLException;
import com.MapeoBD.Articulo.MapeoDespiece;
import com.MapeoBD.Caja.MapeoCaja;
import com.MapeoBD.Cliente.MapeoArticuloCliente;
import com.MapeoBD.Cliente.MapeoCliente;
import com.MapeoBD.Localizacion.MapeoLocalizacion;
import com.MapeoBD.Localizacion.MapeoShelf;
import com.MapeoBD.Ordenes.MapeoOrdenDetalle;
import com.MapeoBD.Pedidos.MapeoPedidoDetalle;
import com.MapeoBD.Pedidos.MapeoPedidoProveedorDetalle;
import com.MapeoBD.Proveedor.MapeoArticuloProveedor;
import com.MapeoBD.Proveedor.MapeoProveedor;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;

public class InformacionArticulo {
	private Serializable objeto;
	private String campo1, campo2,campo3;
	private int articleCode;

	public enum TipoInfo {
		TITULO, LOCALIZACION , PROVEEDOR , CLIENTE ,DESPIECE , ORDEN , PEDIDOSRECIBIDOS
	};

	private TipoInfo tipoInformacionArticulo;

	private boolean isGroupHeader = false;
	private boolean isOrder = false;

	public InformacionArticulo(String title) {
		this(title, null, -1);
		isGroupHeader = true;
		tipoInformacionArticulo=TipoInfo.TITULO;
	}

	/**
	 * Localizacion de un articulo
	 * 
	 * @throws SQLException
	 */
	public InformacionArticulo(MapeoLocalizacion loc) throws SQLException {
		this.objeto=loc;
		this.isGroupHeader = false;
		// campo 1 localizacion
		tipoInformacionArticulo=TipoInfo.LOCALIZACION;
		this.campo1 = new MapeoShelf(loc.getLift(), loc.getShelf()).getDescription();
		this.campo2 = new MapeoCaja(loc.getBox()).getDescription();
		this.campo3 = String.valueOf(loc.getCantidad());
	}
	
	/**
	 * Proveedor de un articulo
	 * 
	 * @throws SQLException
	 */
	public InformacionArticulo(MapeoArticuloProveedor proveedor) throws SQLException {
		this.isGroupHeader = false;
		this.objeto=proveedor;
		// campo 1 localizacion
		tipoInformacionArticulo=TipoInfo.PROVEEDOR;
		//proveedor
		this.campo1 = new MapeoProveedor(proveedor.getProveedor()).getNombre();
		this.campo3 = proveedor.getArticuloProveedor();
	}
	
	/**
	 * cliente de un articulo
	 * 
	 * @throws SQLException
	 */
	public InformacionArticulo(MapeoArticuloCliente cliente) throws SQLException {
		this.isGroupHeader = false;
		this.objeto=cliente;
		// campo 1 localizacion
		tipoInformacionArticulo=TipoInfo.CLIENTE;
		//proveedor
		this.campo1 = new MapeoCliente(cliente.getCliente()).getNombre();
		this.campo3 = cliente.getArticuloCliente();
	}
	
	/**
	 * despiece de un articulo
	 * 
	 * @throws SQLException
	 */
	public InformacionArticulo(MapeoDespiece despiece) throws SQLException {
		this.isGroupHeader = false;
		this.objeto=despiece;
		// campo 1 localizacion
		tipoInformacionArticulo=TipoInfo.DESPIECE;
		//proveedor
		this.campo1=AuxiliarFunctions.format(despiece.getIngrediente(),"xx.xxx.xxxx");
		this.campo3=String.valueOf(despiece.getCantidad());
	}
	
	/**
	 * orden de un articulo
	 * 
	 * @throws SQLException
	 */
	public InformacionArticulo(MapeoOrdenDetalle despiece) throws SQLException {
		this.isGroupHeader = false;
		this.objeto=despiece;
		// campo 1 localizacion
		tipoInformacionArticulo=TipoInfo.ORDEN;
		//proveedor
		this.campo1=String.valueOf(despiece.getOrden());
		this.campo3=despiece.getAlias();
	}
	
	public InformacionArticulo(MapeoPedidoDetalle pedido) throws SQLException {
		// TODO Auto-generated constructor stub
		this.isGroupHeader = false;
		this.objeto=pedido;
		// campo 1 localizacion
		tipoInformacionArticulo=TipoInfo.PEDIDOSRECIBIDOS;
		//proveedor
		this.campo1=String.valueOf(pedido.getPedido() + "	" + pedido.getFechaEntrega());
		this.campo2= String.valueOf(pedido.getCantidad());
		this.campo3=new MapeoCliente(pedido.getCliente()).getNombre();
	}
	public InformacionArticulo(MapeoPedidoProveedorDetalle pedido) throws SQLException {
		// TODO Auto-generated constructor stub
		this.isGroupHeader = false;
		this.objeto=pedido;
		// campo 1 localizacion
		tipoInformacionArticulo=TipoInfo.PEDIDOSRECIBIDOS;
		//proveedor
		this.campo1=String.valueOf(pedido.getPedido() + "	" + pedido.getFechaEntrega());
		this.campo2= String.valueOf(pedido.getCantidad());
		this.campo3=new MapeoProveedor(pedido.getProveedor()).getNombre();
	}

	public InformacionArticulo(String title, String counter) {
		this(title, counter, -1);

	}

	public InformacionArticulo(String title, String counter, int articleCode) {
		super();
		this.campo1 = " " + title;
		this.campo2 = counter;
		this.articleCode = articleCode;
		this.isOrder = false;
	}

	public InformacionArticulo(String title, String counter, int articleCode, boolean isOrder) {
		super();
		this.campo1 = " " + title;
		this.campo2 = counter;
		this.articleCode = articleCode;
		this.isOrder = isOrder;
	}

	

	

	public String getTitle() {
		return this.campo1;
	}

	public String getCounter() {
		String[] split = this.campo2.split("	");
		if (split.length == 1)
			return this.campo2;
		else
			return split[split.length - 1];

	}

	public boolean isGroupHeader() {
		return this.isGroupHeader;
	}

	public int getArticleCode() {
		return this.articleCode;
	}

	public boolean isOrder() {
		return isOrder;
	}

	public TipoInfo getTipoInformacionArticulo() {
		return tipoInformacionArticulo;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.campo1 + "---" + this.campo2 + "----" + this.articleCode;
	}
	
	public String getCampo1() {
		return campo1;
	}public String getCampo2() {
		return campo2;
	}public String getCampo3() {
		return campo3;
	}
	public Serializable getObjeto() {
		return objeto;
	}
}
