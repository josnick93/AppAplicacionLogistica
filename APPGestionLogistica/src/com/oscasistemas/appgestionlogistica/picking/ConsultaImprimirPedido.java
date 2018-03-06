package com.oscasistemas.appgestionlogistica.picking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import android.os.AsyncTask;
import android.util.Log;

public class ConsultaImprimirPedido extends AsyncTask<String, Void, String> {

	/**
	 * Mode Albaran CodigoPedido
	 * 
	 */
	@Override
	protected String doInBackground(String... params) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.2.2:3306/erp_osca", "usuario",
					"osca-SiStEmAs");

			boolean albaran = Boolean.valueOf(params[1]);
			Statement st = null;
			ResultSet rs = null;

			try {
				String QSLEjecutar = "";
				if (Integer.valueOf(params[0]) == 0) {

				} else {
					if (albaran)
						;
					// envio albaran
					// QSLEjecutar = "SELECT pd.Articulo, pd.Alias , pd.Cantidad
					// , pd.swAsignada AS pedidoEnviado, 0 AS pedidoSacado FROM
					// albarandetalle pd WHERE pd.Albaran="
					// + params[2] +" ORDER BY pd.Posicion ASC ";
					else
						// envio cliente
						QSLEjecutar = "Select c.Nombre,p.ArticuloCliente,p.Descripcion,p.Pedido FROM pedidodetalle p, cliente c "
								+ "WHERE p.Pedido=" + params[2] + " AND c.Cliente=p.Cliente " + "AND Articulo="
								+ params[3];
				}

				st = conn.createStatement();
				st.execute("USE erp_osca_test;");
				rs = st.executeQuery(QSLEjecutar);
				Log.d("SCAN",
						"Select c.Nombre,p.ArticuloCliente,p.Descripcion,p.Pedido FROM pedidodetalle p, cliente c "
								+ "WHERE p.Pedido=" + params[2] + " AND c.Cliente=p.Cliente " + "AND Articulo="
								+ params[3]);
				rs.next();
				AuxiliarFunctions.imprimirEtiqueta(
						"p//" + rs.getString("c.Nombre") + "//" + rs.getString("p.ArticuloCliente") + "//"
								+ rs.getString("p.Pedido") + "//" + rs.getString("p.Descripcion") + "//" + params[4]+"//"+params[5]);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				conn.close();

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
	}

}
