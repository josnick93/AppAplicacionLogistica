package com.oscasistemas.appgestionlogistica.Produccion;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConexionMarcarArticuloOrdenEnviado extends AsyncTask<String, Void, String> {

	private int posicion;

	public ConexionMarcarArticuloOrdenEnviado(Activity activity, ListView articleInfo, int posicion) {
		// TODO Auto-generated constructor stub
		this.posicion = posicion;
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
				Log.d("SCAN", urls[0] + "---" + urls[1] + "---" + urls[2] + "---" + urls[3]);
				if (Double.valueOf(urls[3]) >= Double.valueOf(urls[2])) {
					st = conn.prepareStatement(
							"UPDATE pickingdetalle SET swSelect01=0,swAsignada = CASE WHEN (swAsignada=-1) "
									+ "THEN 0 WHEN (swAsignada=0) THEN -1 ELSE swAsignada  END	WHERE (Picking="
									+ urls[0] + " AND Articulo=" + urls[1] + " AND Registro=" + posicion + ")");
					Log.d("SCAN",
							"UPDATE pickingdetalle SET swSelect01=0,swAsignada = CASE WHEN (swAsignada=-1) "
									+ "THEN 0 WHEN (swAsignada=0) THEN -1 ELSE swAsignada  END	WHERE (Picking="
									+ urls[0] + " AND Articulo=" + urls[1] + " AND Registro=" + posicion + ")");
					st.addBatch("USE erp_osca");
					// actualizar picking
					st.executeUpdate();

				} else if (Double.valueOf(urls[3]) < Double.valueOf(urls[2])) {
					st = conn.prepareStatement("UPDATE pickingdetalle SET Cantidad =" + urls[3]
							+ ",swSelect01=0, swAsignada = CASE WHEN (swAsignada=-1)"
							+ "THEN 0 WHEN (swAsignada=0) THEN -1 ELSE swAsignada END" + " WHERE (Picking=" + urls[0]
							+ " AND Articulo=" + urls[1] + " AND Registro=" + posicion + ")");
					Log.d("SCAN",
							"UPDATE pickingdetalle SET Cantidad =" + urls[3]
									+ ",swSelect01=0, swAsignada = CASE WHEN (swAsignada=-1)"
									+ "THEN 0 WHEN (swAsignada=0) THEN -1 ELSE swAsignada END" + "WHERE (Picking="
									+ urls[0] + " AND Articulo=" + urls[1] + " AND Registro=" + posicion + ")");
					st.executeUpdate();
					//
					// // st.executeUpdate("UPDATE pedidodetalle SET Cantidad ="
					// +
					// // urls[3]
					// // + ", swAsignada = CASE WHEN (swAsignada=-1) "
					// // + "THEN 0 WHEN (swAsignada=0) THEN -1 ELSE swAsignada
					// END
					// // WHERE (Pedido=" + urls[0]
					// // + " AND Articulo=" + urls[1] + " AND posicion=" +
					// // posicion + ")");
					// // actualizar picking
					// st.executeUpdate();

					// crear nuevo articulo con los descontados
					double resto = Double.valueOf(urls[2]) - Double.valueOf(urls[3]);
					// QSLEjecutar = "INSERT into
					// pedidodetalle(Alias,Almacen,Articulo,ArticuloCliente,BaseImponible,"
					// +
					// "bReserva,Caducidad,Cantidad,CantidadPreparada,CantidadUnidad,CentroCoste,Cliente,"
					// +
					// "Contenedor,Coste,Cuenta,CuotaIva,CuotaRecargo,CuotaRetencion,Descripcion,Descuento,"
					// +
					// "DescuentoMaximo,Division,Envase,FechaEntrega,Grupo,ISP,Iva,Lote,Mensaje,"
					// +
					// "Oferta,Pack,Partida,Pedido,Peso,Posicion,Precio,Price,PVP,Serial,sPosicion,strCantidad,"
					// +
					// "strDescuento,swAsignada,swFabricacion,swFichaTecnica,TipoComision,TipoIVA,TipoRecargo,"
					// + "TipoRetencion,ts)"
					// + "SELECT
					// Alias,Almacen,Articulo,ArticuloCliente,BaseImponible,bReserva,Caducidad,"
					// + resto
					// +
					// ",CantidadPreparada,CantidadUnidad,CentroCoste,Cliente,Contenedor,Coste,Cuenta,"
					// +
					// "CuotaIva,CuotaRecargo,CuotaRetencion,Descripcion,Descuento,DescuentoMaximo,"
					// +
					// "Division,Envase,FechaEntrega,Grupo,ISP,Iva,Lote,Mensaje,Oferta,Pack,"
					// + "Partida,Pedido,Peso, (Select COUNT(*) FROM
					// pedidodetalle pd WHERE pd.Pedido="
					// + urls[0] + ")+1,"
					// + "Precio,Price,PVP,Serial,(Select COUNT(*) FROM
					// pickingdetalle pd WHERE pd.Picking="
					// + urls[0] +
					// ")+1,strCantidad,strDescuento,swAsignada,swFabricacion,swFichaTecnica,"
					// + "TipoComision,TipoIVA,TipoRecargo,TipoRetencion,ts FROM
					// pedidodetalle pd where pd.Pedido="
					// + urls[0] + " AND pd.Articulo=" + urls[1] + " AND
					// pd.Posicion=" + posicion;

					java.sql.PreparedStatement preparedStmt = conn.prepareStatement("");

					Log.d("SCAN",
							"INSERT INTO pickingdetalle (TipoPicking,Picking,Posicion,Registro,Raiz,Profundo,TipoDetallePicking,"
									+ "TipoOrden,Grupo,Articulo,Alias,Descripcion,Serial,Lote,Caducidad,Cantidad,CantidadCortada,"
									+ "CantidadEnsamblada,CantidadPreparada,Coste,FechaEntrega,Visible,Almacen,swAsignada,PendienteServir,"
									+ "PendienteRecibir,Pedido,Albaran,Cliente,Proveedor,Lift,Shelf,swSelect01,swSelect02,bReserva,idRegistroLogIO,"
									+ "ts)"
									+ "SELECT TipoPicking,Picking,(Select Max(posicion) FROM pickingdetalle pd WHERE pd.Picking="
									+ urls[0] + " AND Articulo=" + urls[1]
									+ ")+1,(Select Max(Registro) FROM pickingdetalle pd WHERE pd.Picking=" + urls[0]
									+ ")+1,Raiz,Profundo,TipoDetallePicking,TipoOrden,Grupo,Articulo,"
									+ "Alias,Descripcion,Serial,Lote,Caducidad," + resto
									+ ",CantidadCortada,CantidadEnsamblada,CantidadPreparada,"
									+ "Coste,FechaEntrega,Visible,Almacen,swAsignada,PendienteServir,PendienteRecibir,Pedido,Albaran,Cliente,"
									+ "Proveedor,Lift,Shelf,0,swSelect02,bReserva,idRegistroLogIO,ts "
									+ "FROM pickingdetalle WHERE TipoPicking=0 AND Picking=" + urls[0] + " "
									+ " AND Articulo=" + urls[1]
									+ " AND Registro=(Select max(Registro) From pickingdetalle WHERE TipoPicking=0 AND Picking="
									+ urls[0] + " " + " AND Articulo=" + urls[1] + " AND Registro=" + posicion + " )");
					// crear picking nuevo
					preparedStmt.execute("INSERT INTO pickingdetalle (TipoPicking,Picking,Posicion,Registro,Raiz,Profundo,TipoDetallePicking,"
							+ "TipoOrden,Grupo,Articulo,Alias,Descripcion,Serial,Lote,Caducidad,Cantidad,CantidadCortada,"
							+ "CantidadEnsamblada,CantidadPreparada,Coste,FechaEntrega,Visible,Almacen,swAsignada,PendienteServir,"
							+ "PendienteRecibir,Pedido,Albaran,Cliente,Proveedor,Lift,Shelf,swSelect01,swSelect02,bReserva,idRegistroLogIO,"
							+ "ts)"
							+ "SELECT TipoPicking,Picking,(Select Max(posicion) FROM pickingdetalle pd WHERE pd.Picking="
							+ urls[0] + " AND Articulo=" + urls[1]
							+ ")+1,(Select Max(Registro) FROM pickingdetalle pd WHERE pd.Picking=" + urls[0]
							+ ")+1,Raiz,Profundo,TipoDetallePicking,TipoOrden,Grupo,Articulo,"
							+ "Alias,Descripcion,Serial,Lote,Caducidad," + resto
							+ ",CantidadCortada,CantidadEnsamblada,CantidadPreparada,"
							+ "Coste,FechaEntrega,Visible,Almacen,swAsignada,PendienteServir,PendienteRecibir,Pedido,Albaran,Cliente,"
							+ "Proveedor,Lift,Shelf,0,swSelect02,bReserva,idRegistroLogIO,ts "
							+ "FROM pickingdetalle WHERE TipoPicking=0 AND Picking=" + urls[0] + " "
							+ " AND Articulo=" + urls[1]
							+ " AND Registro=(Select max(Registro) From pickingdetalle WHERE TipoPicking=0 AND Picking="
							+ urls[0] + " " + " AND Articulo=" + urls[1] + " AND Registro=" + posicion + " )");

					preparedStmt.close();
				}

				// comprobar si esta completo
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
