package com.oscasistemas.appgestionlogistica.Expediciones;

import java.sql.Connection;



import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

public class ConsultaCrearExpedicion extends AsyncTask<String, Void, String> {

	private Activity activity;
	
	public ConsultaCrearExpedicion(Activity activity) {
		this.activity=activity;
		// TODO Auto-generated constructor stub
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
			conn = DriverManager.getConnection("jdbc:mysql://192.168.2.2:3306/erp_osca", "osca", "osca");

			

			st = conn.createStatement();
			/**
			 * Argumentos params 0 ... => TipoExpedicion, EstadoExpedicion,
			 * Cliente, Domicilio, Alias, Referencia, Fecha, FechaRecogida,
			 * iUser, Transportista, Portes, PortesPagados, Agente, Precio,
			 * Descuento, Bultos, Peso, Observaciones, ts
			 */


			
			st.executeUpdate("SET @id=(SELECT Max(expedicion) FROM expedicion As id)+1; ");
			st.executeUpdate("INSERT INTO expedicion (Expedicion,TipoExpedicion,EstadoExpedicion,Cliente,"
					+ "Domicilio,Alias,Referencia,Fecha,FechaRecogida,iUser,Transportista,Portes,PortesPagados,Agente,Precio,Descuento,Bultos,Peso,Observaciones,ts)"
					+ "VALUES (@id,0,0," + params[0] + "," + params[1]
					+ "," + "CONCAT('REF',@id),'" + params[2] + "', CURDATE(), (null), 12 ," + params[3] + ","
					+ params[4] + "," + params[5] + ", null, 0.0, 0.0, 1," + params[6] + ", (null), CURDATE());");
			rs=st.executeQuery("SELECT Count(*) FROM expedicion; ");
			rs.next();
			String id=String.valueOf(rs.getInt(1)+1);
			rs.close();
			st.close();
			
			
			
			/**
			 * Crear nueva actividad
			 */
			
			Intent i = new Intent(this.activity,InformacionExpedicionActivity.class);
			i.putExtra("CODE", id);
			i.putExtra("CLIENTE",  params[0]);
			i.putExtra("REF", params[2]);
			this.activity.startActivity(i);
			this.activity.finish();
			
			
			
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

				st.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return "";
	}
}
