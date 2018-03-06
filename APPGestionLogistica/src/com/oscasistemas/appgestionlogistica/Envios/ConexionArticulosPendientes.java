package com.oscasistemas.appgestionlogistica.Envios;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.MapeoBD.Utils.Variables;
import com.oscasistemas.appgestionlogistica.articulo.SendInfo;
import com.oscasistemas.appgestionlogistica.utils.RegistroLog;
import com.oscasistemas.appgestionlogistica.utils.SendInfoAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConexionArticulosPendientes extends AsyncTask<String, Void, String> {

	private ListView pedidoListView;
	private ArrayList<SendInfo> pedidos;
	private Activity activity;

	public ConexionArticulosPendientes(Activity act, ListView pedidoListView) {
		// TODO Auto-generated constructor stub
		this.pedidoListView = pedidoListView;
		this.activity = act;
		this.pedidos = new ArrayList<SendInfo>();
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.2.2:3306/erp_osca", "usuario",
					"osca-SiStEmAs");

			Statement st = null;
			ResultSet rs = null;

			try {
				/**
				 * Info Articulo
				 */
				String QSLEjecutar = "SELECT p.Referencia ,c.Nombre, pd.Articulo,pd.Alias,pd.Descripcion ,p.Fecha ,pd.FechaEntrega, pd.cantidad From pedido p ,"
						+ " pedidodetalle pd, cliente c WHERE pd.Pedido=p.Pedido AND pd.Cliente=c.Cliente AND pd.Articulo="
						+ urls[0];

				st = conn.createStatement();
				st.execute("USE erp_osca_test;");
				RegistroLog.appendLog("Conexion articulos pendientes -> " + QSLEjecutar);
				rs = st.executeQuery(QSLEjecutar);

				pedidos.clear();
				while (rs.next()) {
					pedidos.add(new SendInfo(rs.getString("p.pedido"), rs.getString("c.nombre"),
							rs.getString("p.Referencia"), rs.getDate("p.Fecha"), rs.getDate("p.FechaEntrega")));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				conn.close();
				rs.close();
				st.close();
				return "";
			} finally {
				rs.close();
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
//		SendInfoAdapter inf = new SendInfoAdapter(this.activity, pedidos);
//		pedidoListView.setAdapter(inf);
//		inf.notifyDataSetChanged();
	}

}
