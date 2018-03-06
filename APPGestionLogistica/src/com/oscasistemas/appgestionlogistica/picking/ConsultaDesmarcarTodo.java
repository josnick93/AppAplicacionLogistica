package com.oscasistemas.appgestionlogistica.picking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import android.os.AsyncTask;
import android.util.Log;

public class ConsultaDesmarcarTodo extends AsyncTask<String, Void, String> {
	
	

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
			st.execute("USE erp_osca;");
			Log.d("SCAN", "UPDATE pedidodetalle, pickingdetalle SET pickingDetalle.swAsignada=0 ,"
					+ " pedidoDetalle.swAsignada=0 WHERE pedidodetalle.Pedido="+params[0]+" AND pickingdetalle.Picking="+params[0]
					+ "AND pedidodetalle.Posicion=pickingdetalle.Posicion AND pickingdetalle.swSelect01=-1");
			st.executeUpdate("UPDATE pedidodetalle, pickingdetalle SET pickingDetalle.swAsignada=0 ,"
					+ " pedidoDetalle.swAsignada=0 WHERE pedidodetalle.Pedido="+params[0]+" AND pickingdetalle.Picking="+params[0]
					+ "AND pedidodetalle.Posicion=pickingdetalle.Posicion AND pickingdetalle.swSelect01=-1");
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
