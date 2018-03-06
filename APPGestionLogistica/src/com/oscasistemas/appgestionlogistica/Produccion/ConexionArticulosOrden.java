package com.oscasistemas.appgestionlogistica.Produccion;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion.AdaptadorComponentesOrdenProd;
import com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion.InformacionComponenteOrdenProd;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConexionArticulosOrden extends AsyncTask<String, Void, String> {

	private ListView pedidoListView;
	private ArrayList<InformacionComponenteOrdenProd> pedidos;
	private Activity activity;

	public ConexionArticulosOrden(Activity act, ListView pedidoListView) {
		// TODO Auto-generated constructor stub
		this.pedidoListView = pedidoListView;
		this.activity = act;
		this.pedidos = new ArrayList<InformacionComponenteOrdenProd>();
	}

	/**
	 * nOrdenProd.getText().toString()
	 */
	@Override
	protected String doInBackground(String... urls) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.2.2:3306/erp_osca", "usuario",
					"osca-SiStEmAs");

			Statement st = null;
			ResultSet rs = null;

//			try {
//
//				st = conn.createStatement();
//				st.addBatch("USE erp_osca");
//				// para cada componente si es un kit buscamos su despiece
//				String QSLEjecutar = "SELECT location.Cantidad AS Cantidad, pickingdetalle.Cantidad AS CantidadNecesaria, pickingdetalle.swAsignada AS asignada, pickingdetalle.* FROM pickingdetalle"
//						+ " LEFT JOIN (Select Articulo,Sum(Cantidad) "
//						+ " As Cantidad From location GROUP BY Articulo) As location ON pickingdetalle.Articulo=location.Articulo "
//						+ " WHERE  pickingdetalle.Picking=" + urls[0] + " AND TipoDetallePicking=2 ORDER BY Articulo ASC";
//				rs = st.executeQuery(QSLEjecutar);
//				Log.d("SCAN", QSLEjecutar);
//				pedidos.clear();
//				pedidos.add(new InformacionComponenteOrdenProd("Disponible"));
//				pedidos.add(new InformacionComponenteOrdenProd("Articulo", "Alias", "Necesario"));
//				while (rs.next()) {
//
//					InformacionComponenteOrdenProd aux = null;
//					double cantidadTotal = rs.getDouble("Cantidad");
//					double cantidadNecesaria = rs.getDouble("CantidadNecesaria");
//
//					// double avaiable = rs.getDouble("ar.Stock") -
//					// rs.getDouble("a.Cantidad");
//					if (cantidadTotal >= cantidadNecesaria) {
//
//						if (rs.getDouble("asignada") == 0) {
//							aux = new InformacionComponenteOrdenProd(rs.getString("pickingdetalle.Articulo"),
//									rs.getString("pickingdetalle.Alias"), String.valueOf(cantidadNecesaria),
//									String.valueOf(cantidadTotal - rs.getDouble("pickingdetalle.PendienteServir")),
//									Color.WHITE, rs.getInt("pickingdetalle.Registro"));
//
//							Log.d("SCAN", "-------------------------");
//						} else
//							aux = new InformacionComponenteOrdenProd(rs.getString("pickingdetalle.Articulo"),
//									rs.getString("pickingdetalle.Alias"), String.valueOf(cantidadNecesaria),
//									String.valueOf(cantidadTotal - rs.getDouble("pickingdetalle.PendienteServir")),
//									Color.GREEN, rs.getInt("pickingdetalle.Registro"));
//					}
//					// no hay disponible
//					else {
//
//						if (cantidadTotal + rs.getDouble("pickingdetalle.PendienteRecibir") >= cantidadNecesaria)
//							aux = new InformacionComponenteOrdenProd(rs.getString("pickingdetalle.Articulo"),
//									rs.getString("pickingdetalle.Alias"), String.valueOf(cantidadNecesaria),
//									String.valueOf(cantidadTotal - rs.getDouble("pickingdetalle.PendienteServir")),
//									Color.rgb(255, 140, 0), rs.getInt("pickingdetalle.Registro"));
//						else
//							aux = new InformacionComponenteOrdenProd(rs.getString("pickingdetalle.Articulo"),
//									rs.getString("pickingdetalle.Alias"), String.valueOf(cantidadNecesaria),
//									String.valueOf(cantidadTotal - rs.getDouble("pickingdetalle.PendienteServir")),
//									Color.RED, rs.getInt("pickingdetalle.Registro"));
//
//					}
//					pedidos.add(aux);
//
//				}
//
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				conn.close();
//				rs.close();
//				st.close();
//				return "";
//			} finally {
//				rs.close();
//				st.close();
//				conn.close();
//			}
		} catch (

		Exception e) {
			Log.e("Error", e.getMessage());
		}
		return "";

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		AdaptadorComponentesOrdenProd inf = new AdaptadorComponentesOrdenProd(this.activity, pedidos);
		pedidoListView.setAdapter(inf);
		inf.notifyDataSetChanged();
	}

}
