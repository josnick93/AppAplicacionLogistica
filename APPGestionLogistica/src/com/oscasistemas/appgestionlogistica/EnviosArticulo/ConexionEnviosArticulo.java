package com.oscasistemas.appgestionlogistica.EnviosArticulo;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.informacionArticulo.AdaptadorInformacionArticulo;
import com.oscasistemas.appgestionlogistica.informacionArticulo.InformacionArticulo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConexionEnviosArticulo extends AsyncTask<String, Void, String> {

	private Activity actividad;
	private ArrayList<InformacionArticulo> articleInfoData;
	private ListView articleInfo;

	public ConexionEnviosArticulo(Activity activity, ListView articleInfo) {
		// TODO Auto-generated constructor stub
		this.actividad = activity;
		this.articleInfo = articleInfo;
		this.articleInfoData = new ArrayList<InformacionArticulo>();
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
				/**
				 * Info Order
				 */
				String QSLEjecutar = "SELECT pd.Pedido , p.Referencia, pr.Nombre FROM pedidodetalle pd, cliente pr , pedido p WHERE pd.Articulo="
						+ urls[0] + " AND pd.Pedido=p.Pedido AND pr.Cliente=p.Cliente";
				st = conn.createStatement();
				st.addBatch("USE erp_osca");
				rs = st.executeQuery(QSLEjecutar);
				articleInfoData.clear();
				if (rs.first()) {
					articleInfoData.add(new InformacionArticulo("Pedido", "Cliente"));
					while (rs.next()) {
						articleInfoData.add(new InformacionArticulo(rs.getString("p.Referencia"), rs.getString("pr.Nombre"),
								rs.getInt("pd.Pedido")));
					}
				}
				/**
				 * Ordenes de produccion
				 */
				rs.close();
				Log.d("SCAN",
						"SELECT o.Orden ,o.Referencia, u.Description FROM pickingdetalle pid ,orden o,users u WHERE pid.Articulo="
								+ urls[0] + "  AND u.iUser=o.iUser"
								+ " AND TipoPicking=0 AND pid.Picking=o.Orden GROUP BY Picking");
				ResultSet rs1 = st.executeQuery(
						"SELECT o.Orden ,o.Referencia, u.Description FROM pickingdetalle pid ,orden o,users u WHERE pid.Articulo="
								+ urls[0] + "  AND u.iUser=o.iUser"
								+ " AND TipoPicking=0 AND pid.Picking=o.Orden GROUP BY Picking");
				if (rs1.first()) {
					articleInfoData.add(new InformacionArticulo("Orden", "Usuario"));
					while (rs1.next()) {
						articleInfoData.add(new InformacionArticulo(rs1.getString("o.Referencia"), rs1.getString("u.Description"),
								rs1.getInt("o.Orden")));
					}
				}

				rs1.close();

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
		AdaptadorInformacionArticulo loc = new AdaptadorInformacionArticulo(this.actividad, articleInfoData);
		articleInfo.setAdapter(loc);
		loc.notifyDataSetChanged();
	}

}
