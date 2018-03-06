package com.oscasistemas.appgestionlogistica.Expediciones;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ConsultaTransportista extends AsyncTask<String, Void, String> {

	private Spinner trasportistapinner;
	private Activity activity;
	private LinkedHashMap<String,Integer> trasportista;
	

	public ConsultaTransportista(Activity act,Spinner trasportistapinner) {
		// TODO Auto-generated constructor stub
		this.trasportistapinner = trasportistapinner;
		this.activity=act;
		this.trasportista=new LinkedHashMap<String,Integer>();
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
			rs = st.executeQuery("SELECT * FROM transportista t Where t.Nombre like '"+params[0]+"%'" );
			
			while(rs.next())
				this.trasportista.put(rs.getString("t.Nombre"), rs.getInt("t.Transportista"));
			
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
	
	
	public LinkedHashMap<String, Integer> gettrasportista() {
		return trasportista;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		trasportistapinner.setAdapter(new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(trasportista.keySet())));
		
	}

}
