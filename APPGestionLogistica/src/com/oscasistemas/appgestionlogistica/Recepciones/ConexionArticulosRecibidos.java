package com.oscasistemas.appgestionlogistica.Recepciones;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.articulo.SendInfo;
import com.oscasistemas.appgestionlogistica.utils.SendInfoAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConexionArticulosRecibidos extends AsyncTask<String, Void, String> {

	private ListView recibosListView;
	private ArrayList<SendInfo> pedidos;
	private Activity activity;

	public ConexionArticulosRecibidos(Activity act, ListView recibosListView) {
		// TODO Auto-generated constructor stub
		this.recibosListView = recibosListView;
		this.activity = act;
		this.pedidos=new ArrayList<SendInfo>();
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
				String QSLEjecutar = "SELECT p.Referencia ,pd.Articulo,c.Nombre, pd.Descripcion ,p.Fecha , pd.cantidad From ppedido p , ppedidodetalle pd,"
						+ " proveedor c WHERE pd.Pedido=p.Pedido AND pd.Proveedor=c.Proveedor AND pd.Articulo="
						+ urls[0];

				st = conn.createStatement();
				st.addBatch("USE erp_osca");
				rs = st.executeQuery(QSLEjecutar);
				pedidos.clear();
				if (rs.next()) {
					pedidos.add(new SendInfo(rs.getString("p.Albaran"), rs.getString("p.proveedor"), "",
							rs.getDate("p.FechaPedido"), rs.getDate("p.FechaEntrega")));
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
//		recibosListView.setAdapter(inf);
//		inf.notifyDataSetChanged();
	}


}
