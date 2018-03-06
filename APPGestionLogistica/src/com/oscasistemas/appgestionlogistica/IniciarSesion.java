package com.oscasistemas.appgestionlogistica;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.loggin.MapeoUsuario;
import com.oscasistemas.appgestionlogistica.utils.RegistroLog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class IniciarSesion extends AsyncTask<String, Void, String> {

	private MainActivity actividad;
	private ProgressDialog dialogoEspera;

	public IniciarSesion(MainActivity act) {
		this.dialogoEspera = new ProgressDialog(act);
		this.actividad = act;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialogoEspera.setMessage("Iniciando Session");
		dialogoEspera.setCancelable(false);
		dialogoEspera.show();
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
				st = conn.createStatement();
				st.addBatch("USE erp_osca_test");
				RegistroLog.appendLog("Consulta usuarios -> SELECT * FROM users WHERE User='" + urls[0] + "'");
				rs = st.executeQuery("SELECT * FROM users WHERE User='" + urls[0] + "'");
				if (rs.next()) {
					loggin(new MapeoUsuario(rs.getString("User"), rs.getString("Password"), -1),urls[1]);
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
		// Show AlertDialog to user
		if (dialogoEspera.isShowing()) {
			dialogoEspera.dismiss();
		}
	}

	private void loggin(MapeoUsuario s,String pass) {
		if (s == null) {
			Toast.makeText(this.actividad, "No existe el usuario", Toast.LENGTH_LONG).show();
		} else {
			if (!s.getPass().toUpperCase(Locale.ENGLISH).equals(pass.toUpperCase(Locale.ENGLISH)))
				Toast.makeText(this.actividad, "Constraseña incorrecta", Toast.LENGTH_LONG).show();
			else {
				// LOGGIN
				RegistroLog.appendLog("Inicio sesion: -> "+s.toString());
				Intent i = new Intent(this.actividad, MenuActivity.class);
				this.actividad.writelogginSession(s);
				this.actividad.startActivity(i);
			}

		}
	}

}
