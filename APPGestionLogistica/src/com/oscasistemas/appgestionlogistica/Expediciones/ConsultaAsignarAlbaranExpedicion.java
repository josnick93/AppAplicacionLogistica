package com.oscasistemas.appgestionlogistica.Expediciones;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import android.os.AsyncTask;

public class ConsultaAsignarAlbaranExpedicion extends AsyncTask<String, Void, String> {

	public ConsultaAsignarAlbaranExpedicion() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String doInBackground(String... params) {
		Connection conn = null;
		Statement st = null;
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
			
			st.executeUpdate("INSERT INTO expediciondetalle (Expedicion,Posicion,Albaran,Referencia,Fecha,FechaValor,PortesPagados,Portes,Bultos,Peso,Observaciones,swAsignada,ts)"
					+ "SELECT "+params[0]+",(Select COUNT(*)+1 FROM expediciondetalle pd WHERE pd.Expedicion="+params[0]+"),"
					+params[1]+",(SELECT a.Referencia FROM albaran a WHERE a.Albaran="+params[1]+"),"
					+ "(SELECT a.Fecha FROM albaran a WHERE a.Albaran="+params[1]+"),"
					+ "(SELECT a.FechaValor FROM albaran a WHERE a.Albaran="+params[1]+"),"
					+ "(SELECT a.PortesPagados FROM albaran a WHERE a.Albaran="+params[1]+"),"
					+ "(SELECT a.Portes FROM albaran  a WHERE a.Albaran="+params[1]+"),"
					+ "(SELECT a.Bultos FROM albaran  a WHERE a.Albaran="+params[1]+"),"
					+ "(SELECT a.Peso FROM albaran a WHERE a.Albaran="+params[1]+"),"
					+ "(SELECT e.Observaciones FROM expedicion e WHERE e.Expedicion="+params[0]+"),"
					+ "0,"
					+ "(SELECT ts FROM expedicion a WHERE a.Expedicion="+params[0]+")");
			st.close();
				
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
