package com.oscasistemas.appgestionlogistica.picking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.articulo.ArticlePickingInfo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConexionMarcarArticuloSacado extends AsyncTask<String, Void, String> {

	private Activity actividad;
	private ArrayList<ArticlePickingInfo> articleInfoData;
	private ListView articleInfo;
	private int mode, posicion;
	private boolean albaran;

	public ConexionMarcarArticuloSacado(Activity activity, ListView articleInfo,
			ArrayList<ArticlePickingInfo> articleInfoData, int mode, int posicion, boolean albaran) {
		// TODO Auto-generated constructor stub
		this.actividad = activity;
		this.articleInfo = articleInfo;
		this.mode = mode;
		this.albaran = albaran;
		this.posicion = posicion;
		this.articleInfoData = articleInfoData;
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.2.2:3306/erp_osca", "osca", "osca");

			java.sql.PreparedStatement st = null;

			try {
				/**
				 * Info Articulo
				 */
				String QSLEjecutar = "";
				if (mode != 0) {
					Log.d("SCAN", urls[3] + "--" + urls[2]);
					if (!albaran && Integer.valueOf(urls[3]) >= Integer.valueOf(urls[2])) {

						QSLEjecutar = "UPDATE pickingdetalle SET swSelect01 = CASE WHEN (swSelect01=-1) "
								+ "THEN 0 WHEN (swSelect01=0) THEN -1 ELSE swSelect01  END WHERE Picking=" + urls[0]
								+ " AND Articulo=" + urls[1] + " AND posicion=" + posicion;
						Log.d("SCAN", QSLEjecutar);
						st = conn.prepareStatement(QSLEjecutar);
						st.execute("USE erp_osca_test;");

						st.executeUpdate();
					} else if (!albaran && Integer.valueOf(urls[3]) < Integer.valueOf(urls[2])) {
						st = conn.prepareStatement("UPDATE pickingdetalle SET Cantidad =" + urls[3]
								+ ", swSelect01 = CASE WHEN (swSelect01=-1) "
								+ "THEN 0 WHEN (swSelect01=0) THEN -1 ELSE swSelect01  END WHERE Picking=" + urls[0]
								+ " AND Articulo=" + urls[1] + " AND posicion=" + posicion);
						st.execute("USE erp_osca_test;");

						st.executeUpdate();

						st.execute("UPDATE pedidodetalle SET Cantidad =" + urls[3] + " WHERE Pedido=" + urls[0]
								+ " AND Articulo=" + urls[1] + " AND posicion=" + posicion);
						st.addBatch("USE erp_osca");

						// crear nuevo articulo con los descontados
						int resto = Integer.valueOf(urls[2]) - Integer.valueOf(urls[3]);
						QSLEjecutar = "INSERT into pedidodetalle(Alias,Almacen,Articulo,ArticuloCliente,BaseImponible,"
								+ "bReserva,Caducidad,Cantidad,CantidadPreparada,CantidadUnidad,CentroCoste,Cliente,"
								+ "Contenedor,Coste,Cuenta,CuotaIva,CuotaRecargo,CuotaRetencion,Descripcion,Descuento,"
								+ "DescuentoMaximo,Division,Envase,FechaEntrega,Grupo,ISP,Iva,Lote,Mensaje,"
								+ "Oferta,Pack,Partida,Pedido,Peso,Posicion,Precio,Price,PVP,Serial,sPosicion,strCantidad,"
								+ "strDescuento,swAsignada,swFabricacion,swFichaTecnica,TipoComision,TipoIVA,TipoRecargo,"
								+ "TipoRetencion,ts)"
								+ "SELECT Alias,Almacen,Articulo,ArticuloCliente,BaseImponible,bReserva,Caducidad,"
								+ resto
								+ ",CantidadPreparada,CantidadUnidad,CentroCoste,Cliente,Contenedor,Coste,Cuenta,"
								+ "CuotaIva,CuotaRecargo,CuotaRetencion,Descripcion,Descuento,DescuentoMaximo,"
								+ "Division,Envase,FechaEntrega,Grupo,ISP,Iva,Lote,Mensaje,Oferta,Pack,"
								+ "Partida,Pedido,Peso, (Select COUNT(*) FROM pedidodetalle pd WHERE pd.Pedido="
								+ urls[0] + ")+1,"
								+ "Precio,Price,PVP,Serial,(Select COUNT(*) FROM pickingdetalle pd WHERE pd.Picking="
								+ urls[0] + ")+1,strCantidad,strDescuento,swAsignada,swFabricacion,swFichaTecnica,"
								+ "TipoComision,TipoIVA,TipoRecargo,TipoRetencion,ts FROM pedidodetalle pd where pd.Pedido="
								+ urls[0] + " AND pd.Articulo=" + urls[1] + " AND pd.Posicion=" + posicion;

						Log.d("SCAN", QSLEjecutar);
						java.sql.PreparedStatement preparedStmt = conn.prepareStatement(QSLEjecutar);
						preparedStmt.execute();

						// crear picking nuevo
						preparedStmt.execute(
								"INSERT INTO pickingdetalle (TipoPicking,Picking,Posicion,Registro,Raiz,Profundo,TipoDetallePicking,"
										+ "TipoOrden,Grupo,Articulo,Alias,Descripcion,Serial,Lote,Caducidad,Cantidad,CantidadCortada,"
										+ "CantidadEnsamblada,CantidadPreparada,Coste,FechaEntrega,Visible,Almacen,swAsignada,PendienteServir,"
										+ "PendienteRecibir,Pedido,Albaran,Cliente,Proveedor,Lift,Shelf,swSelect01,swSelect02,bReserva,idRegistroLogIO,"
										+ "ts)"
										+ "SELECT TipoPicking,Picking,(Select COUNT(*) FROM pickingdetalle pd WHERE pd.Picking="
										+ urls[0]
										+ ")+1,Registro+1,Raiz,Profundo,TipoDetallePicking,TipoOrden,Grupo,Articulo,"
										+ "Alias,Descripcion,Serial,Lote,Caducidad," + resto
										+ ",CantidadCortada,CantidadEnsamblada,CantidadPreparada,"
										+ "Coste,FechaEntrega,Visible,Almacen,swAsignada,PendienteServir,PendienteRecibir,Pedido,Albaran,Cliente,"
										+ "Proveedor,Lift,Shelf,swSelect01,swSelect02,bReserva,idRegistroLogIO,ts "
										+ "FROM pickingdetalle WHERE TipoPicking=2 AND Picking=" + urls[0] + " "
										+ " AND Articulo=" + urls[1] + " AND Posicion=" + posicion
										+ " AND Registro=(Select max(Registro) From pickingdetalle WHERE TipoPicking=2 AND Picking="
										+ urls[0] + " " + " AND Articulo=" + urls[1] + " AND Posicion=" + posicion
										+ " )");
						preparedStmt.close();
					}
				}

				// comprobar si esta completo
				new ConsultaPedidos(this.actividad, articleInfoData, articleInfo).execute(String.valueOf(mode),
						String.valueOf(albaran), urls[0]);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				conn.close();
				st.close();
				return "";
			} finally {
				st.close();
				conn.close();
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}
		return "";

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
