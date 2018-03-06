package com.oscasistemas.appgestionlogistica.Expediciones;

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

public class ConsultaAlbaranesCliente extends AsyncTask<String, Void, String> {
	private Activity activity;
	private ListView expediciones;
	private ArrayList<InformacionComponenteOrdenProd> albaranes, albaranesActuales;

	public ConsultaAlbaranesCliente(Activity act, ListView expediciones, ArrayList<InformacionComponenteOrdenProd> albaranes) {
		// TODO Auto-generated constructor stub
		this.expediciones = expediciones;
		this.activity = act;
		this.albaranesActuales = albaranes;
		this.albaranes = new ArrayList<InformacionComponenteOrdenProd>();
	}

	/**
	 * argumento 1 palabra alfabeto
	 */
	@Override
	protected String doInBackground(String... params) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		/**
		 * Info Articulo
		 */
		try {
			/**
			 * Codigo
			 */
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://192.168.2.2:3306/erp_osca", "usuario", "osca-SiStEmAs");
			st = conn.createStatement();
			st.addBatch("USE erp_osca");
			Log.d("SCAN", "Select a.* FROM albaran a WHERE a.Cliente=" + params[0]);
			rs = st.executeQuery("Select a.* FROM albaran a WHERE a.Cliente=" + params[0]);

			this.albaranes.clear();
//			this.albaranes.add(new InformacionComponenteOrdenProd("Alabaran", "Alias", "Peso"));
//			while (rs.next()) {
//				boolean encontrado = false;
//				for (InformacionComponenteOrdenProd p : albaranesActuales) {
//					if (p.getTitle().equals(rs.getString("Albaran")))
//						encontrado = true;
//				}
//				if (!encontrado)
//					this.albaranes.add(new InformacionComponenteOrdenProd(rs.getString("Albaran"),
//							rs.getString("Referencia"), rs.getString("Peso"), Color.rgb(255, 165, 0)));
//			}
			/**
			 * Depuracion errores
			 */
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		// añadir resultados a la lista
		AdaptadorComponentesOrdenProd adp = new AdaptadorComponentesOrdenProd(this.activity.getApplicationContext(),
				this.albaranes);
		this.expediciones.setAdapter(adp);
		adp.notifyDataSetChanged();

	}
}
