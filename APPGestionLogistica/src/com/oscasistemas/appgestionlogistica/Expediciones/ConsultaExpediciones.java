package com.oscasistemas.appgestionlogistica.Expediciones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

public class ConsultaExpediciones extends AsyncTask<String, Void, String> {

	private Activity activity;
	private ListView expediciones;
	private ArrayList<InformacionExpedicion> informacionExpediciones;

	public ConsultaExpediciones(Activity act, ListView expediciones) {
		// TODO Auto-generated constructor stub
		this.expediciones = expediciones;
		this.activity = act;
		this.informacionExpediciones = new ArrayList<InformacionExpedicion>();
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
			rs = st.executeQuery(
					"Select u.Description, c.Nombre ,e.* From expedicion  e , users u ,cliente c WHERE "
					+ "e.iUser=u.iUser AND e.Cliente=c.Cliente ORDER BY e.Expedicion DESC LIMIT 221");
			this.informacionExpediciones.clear();
			while (rs.next())
				this.informacionExpediciones.add(new InformacionExpedicion(rs.getString("e.Expedicion"),
						rs.getString("e.Cliente"),rs.getString("c.Nombre"), rs.getString("e.Referencia"), rs.getString("u.Description")));
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
		InformacionExpedicionAdaptador adp = new InformacionExpedicionAdaptador(this.activity,
				this.informacionExpediciones);
		this.expediciones.setAdapter(adp);
		adp.notifyDataSetChanged();

	}

}
