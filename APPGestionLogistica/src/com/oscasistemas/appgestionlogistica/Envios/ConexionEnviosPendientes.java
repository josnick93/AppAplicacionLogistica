package com.oscasistemas.appgestionlogistica.Envios;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.MapeoBD.Utils.Variables;
import com.oscasistemas.appgestionlogistica.articulo.SendInfo;
import com.oscasistemas.appgestionlogistica.utils.RegistroLog;
import com.oscasistemas.appgestionlogistica.utils.SendInfoAdapter;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConexionEnviosPendientes extends AsyncTask<String, Void, String> {

	private ListView pedidoListView;
	private ArrayList<SendInfo> pedidos;
	private Activity activity;
	private int nOrder,position;

	public ConexionEnviosPendientes(Activity act, ListView pedidoListView,int nOrder,int position) {
		// TODO Auto-generated constructor stub
		this.pedidoListView = pedidoListView;
		this.activity = act;
		this.pedidos=new ArrayList<SendInfo>();
		this.nOrder=nOrder;
		this.position=position;
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(Variables.db_URL, Variables.user,
					Variables.pass);

			Statement st = null;
			ResultSet rs = null;

			try {
				/**
				 * Info Articulo
				 */
				String QSLEjecutar = query();

				st = conn.createStatement();
				st.execute("USE erp_osca_test;");
				rs = st.executeQuery(QSLEjecutar);
				RegistroLog.appendLog("Consulta envios Pedidos Pendientes -> "+QSLEjecutar);
				this.pedidos.clear();
				while (rs.next()) {
					this.pedidos.add(new SendInfo(rs.getString("p.pedido"), rs.getString("c.nombre"),
							rs.getString("p.Referencia"), rs.getDate("p.FechaEntrega"), rs.getDate("p.FechaEntrega")));
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

	private String query() {
		if (this.nOrder != -1)
			return "Select c.nombre ,p.* From pedido p,cliente c WHERE  p.Cliente=c.Cliente  AND p.referencia<>('eliminar pedido' OR 'CANTERO  (BORRAR)')  AND p.swPicking=-1 ORDER BY p.FechaEntrega DESC LIMIT 50";

		switch (this.position) {
		// Fecha descendente
		case 0:
			return "Select c.nombre ,p.* From pedido p,cliente c WHERE p.Cliente=c.Cliente  AND p.swPicking=-1 ORDER BY p.FechaEntrega DESC LIMIT 50 ";
		// Fecha ascendente
		case 1:
			return "Select c.nombre ,p.* From pedido p,cliente c WHERE p.Cliente=c.Cliente AND p.referencia<>('eliminar pedido' OR 'CANTERO  (BORRAR)') AND p.swPicking=-1  ORDER BY p.FechaEntrega ASC LIMIT 50 ";
		// Cliente Ascendente
		case 2:
			return "Select c.nombre ,p.* From pedido p,cliente c WHERE p.Cliente=c.Cliente AND p.referencia<>('eliminar pedido' OR 'CANTERO  (BORRAR)') AND p.swPicking=-1  ORDER BY c.Nombre DESC LIMIT 50 ";
		// Cliente Descendente
		case 3:
			return "Select c.nombre ,p.* From pedido p,cliente c WHERE p.Cliente=c.Cliente AND p.referencia<>('eliminar pedido' OR 'CANTERO  (BORRAR)') AND p.swPicking=-1  ORDER BY c.Nombre ASC LIMIT 50 ";
		// Identificador ascendente
		case 4:
			return "Select c.nombre ,p.* From pedido p,cliente c WHERE p.Cliente=c.Cliente AND p.referencia<>('eliminar pedido' OR 'CANTERO  (BORRAR)') AND p.swPicking=-1  ORDER BY p.albaran DESC LIMIT 50 ";
		// Identificador descendente
		case 5:
			return "Select c.nombre ,p.* From pedido p,cliente c WHERE p.Cliente=c.Cliente AND p.referencia<>('eliminar pedido' OR 'CANTERO  (BORRAR)') AND p.swPicking=-1  ORDER BY p.albaran ASC LIMIT 50 ";

		default:
			return "Select c.nombre ,p.* From pedido p,cliente c WHERE p.Cliente=c.Cliente AND p.referencia<>('eliminar pedido' OR 'CANTERO  (BORRAR)') AND p.swPicking=-1  LIMIT 50";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		SendInfoAdapter inf = new SendInfoAdapter(this.activity, this.pedidos);
//		this.pedidoListView.setAdapter(inf);
//		inf.notifyDataSetChanged();
	}

}
