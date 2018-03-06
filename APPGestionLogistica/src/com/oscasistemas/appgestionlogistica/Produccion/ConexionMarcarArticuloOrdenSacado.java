package com.oscasistemas.appgestionlogistica.Produccion;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.articulo.ArticlePickingInfo;

import android.app.Activity;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConexionMarcarArticuloOrdenSacado extends AsyncTask<String, Void, String> {

	private int posicion;

	public ConexionMarcarArticuloOrdenSacado(Activity activity, ListView articleInfo, int posicion) {
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
				Log.d("SCAN", urls[0]+"---"+urls[1]+"---"+urls[2]+"---"+urls[3]);
				String QSLEjecutar = "";
				if (Double.valueOf(urls[3]) >= Double.valueOf(urls[2])) {
					QSLEjecutar = "UPDATE pickingdetalle SET swSelect01 = CASE WHEN (swSelect01=-1) "
							+ "THEN 0 WHEN (swSelect01=0) THEN -1 ELSE swSelect01  END WHERE Picking=" + urls[0]
							+ " AND Articulo=" + urls[1] + " AND Registro=" + posicion;
					Log.d("SCAN", QSLEjecutar);
					st = conn.prepareStatement(QSLEjecutar);
					st.addBatch("USE erp_osca");

					st.executeUpdate();
					st.execute("UPDATE ordendetalle SET Cantidad =" + urls[3] + " WHERE Orden=" + urls[0]
							+ " AND Articulo=" + urls[1] + " AND posicion=" + posicion);
					
				} else if (Double.valueOf(urls[3]) < Double.valueOf(urls[2])) {
					Log.d("SCAN", "aaaaaaaaaa");
					st = conn.prepareStatement("UPDATE pickingdetalle SET Cantidad =" + urls[3]
							+ ", swSelect01 = CASE WHEN (swSelect01=-1) "
							+ "THEN 0 WHEN (swSelect01=0) THEN -1 ELSE swSelect01  END WHERE Picking=" + urls[0]
							+ " AND Articulo=" + urls[1] + " AND Registro=" + posicion);
					st.addBatch("USE erp_osca");

					st.executeUpdate();

					st.execute("UPDATE ordendetalle SET Cantidad =" + urls[3] + " WHERE Orden=" + urls[0]
							+ " AND Articulo=" + urls[1] + " AND posicion=" + posicion);
					st.addBatch("USE erp_osca");

					// crear nuevo articulo con los descontados
					double resto = Double.valueOf(urls[2]) - Integer.valueOf(urls[3]);
					QSLEjecutar = "INSERT into ordendetalle(idRegistro,Orden,Posicion,sPosicion,Grupo,Pedido,Cliente,Fecha,Articulo,Alias,Descripcion,ArticuloCliente,"
							+ "Peso,Almacen,Pack,Cantidad,Unidad,CantidadUnidad,Coste,Precio,Price,PVP,"
							+ "Descuento,strDescuento,swAsignada,Serial,Lote,Caducidad,swFichaTecnica,ts)"
							+ "SELECT idRegistro,Orden,Posicion+1,sPosicion+1,Grupo,Pedido,Cliente,Fecha,Articulo,Alias,Descripcion,ArticuloCliente,"
							+ "Peso,Almacen,Pack," + resto + ",Unidad,CantidadUnidad,Coste,Precio,Price,PVP,Descuento,"
							+ "strDescuento,swAsignada,Serial,Lote,Caducidad,swFichaTecnica,ts FROM ordendetalle WHERE  Orden="
							+ urls[0] + " AND Posicion=" + posicion;

					java.sql.PreparedStatement preparedStmt = conn.prepareStatement("");

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
