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

public class ConsultaClientes extends AsyncTask<String, Void, String> {

	private Spinner ClienteSpinner;
	private Activity activity;
	private LinkedHashMap<String,Integer> clientes;
	

	public ConsultaClientes(Activity act,Spinner ClienteSpinner) {
		// TODO Auto-generated constructor stub
		this.ClienteSpinner = ClienteSpinner;
		this.activity=act;
		this.clientes=new LinkedHashMap<String,Integer>();
	}


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
			rs = st.executeQuery("SELECT * FROM Cliente c  WHERE c.Nombre Like '"+params[0]+"%'" + " ORDER BY c.Nombre ASC" );
					
			
			
			while(rs.next())
				this.clientes.put(rs.getString("c.Nombre"), rs.getInt("c.Cliente"));
			
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
	
	
	public LinkedHashMap<String, Integer> getClientes() {
		return clientes;
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		ClienteSpinner.setAdapter(new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(clientes.keySet())));
		
	}

}
