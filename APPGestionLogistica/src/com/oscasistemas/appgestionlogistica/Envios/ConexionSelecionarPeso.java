package com.oscasistemas.appgestionlogistica.Envios;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.oscasistemas.appgestionlogistica.utils.RegistroLog;

import android.os.AsyncTask;
import android.util.Log;

public class ConexionSelecionarPeso extends AsyncTask<String, Void, String> {

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
				String QSLEjecutar = "UPDATE albaran SET Peso = " + urls[1] + " WHERE Albaran=" + urls[0];
				RegistroLog.appendLog("Cambio peso albaran -> "+QSLEjecutar);

				st = conn.prepareStatement(QSLEjecutar);
				st.addBatch("USE erp_osca");
				st.executeUpdate();
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
