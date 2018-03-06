package com.oscasistemas.appgestionlogistica.picking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.articulo.ArticlePickingInfo;
import com.oscasistemas.appgestionlogistica.listViews.ArticlePickingAdapter;
import com.oscasistemas.appgestionlogistica.utils.RegistroLog;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConsultaPedidos extends AsyncTask<String, Void, String> {
	private ArrayList<ArticlePickingInfo> articleInfoData;
	private Activity activity;
	private ListView articleInfo;

	public ConsultaPedidos(Activity activity, ArrayList<ArticlePickingInfo> articleInfoData, ListView articleInfo) {
		// TODO Auto-generated constructor stub
		this.articleInfoData = new ArrayList<ArticlePickingInfo>();
		this.activity = activity;
		this.articleInfo = articleInfo;
	}

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
					if (albaran)
						// recibo albaran
						QSLEjecutar = "SELECT pd.Articulo AS Articulo,pd.Posicion,1 AS Registro, pd.Alias AS Alias, pd.Cantidad AS Cantidad, 0 AS pedidoEnviado , 0 AS pedidoSacado FROM palbarandetalle pd  WHERE pd.Albaran="
								+ params[2] + " ORDER BY pd.Posicion ASC ";
					// recibo pedido
					else
						QSLEjecutar = "SELECT pd.Articulo AS Articulo,pd.Posicion,pd.Registro AS Registro, pd.Alias AS Alias , pd.Cantidad AS Cantidad, pd.swAsignada AS pedidoEnviado, pd.swSelect01 AS pedidoSacado "
								+ "FROM pickingdetalle pd WHERE pd.Picking=" + params[2] + " ORDER BY pd.Posicion ASC ";
				} else {
					if (albaran)
						// envio albaran
						QSLEjecutar = "SELECT pd.Articulo AS Articulo,pd.Posicion,1 AS Registro, pd.Alias AS Alias , pd.Cantidad AS Cantidad, pd.swAsignada AS pedidoEnviado, 0 AS pedidoSacado FROM albarandetalle pd WHERE pd.Albaran="
								+ params[2] + " ORDER BY pd.Posicion ASC ";
					else
						// envio cliente
						QSLEjecutar = "SELECT pid.Articulo AS Articulo,pd.Posicion,1 AS Registro, pid.Alias AS Alias, pid.Cantidad AS Cantidad, pid.swAsignada AS pedidoEnviado, pid.swSelect01 AS pedidoSacado FROM pedidodetalle pd , pickingdetalle pid WHERE pd.Pedido="
								+ params[2] + " AND pid.Picking=" + params[2]
								+ " AND pd.Posicion=pid.Posicion ORDER BY pd.Posicion ASC  ";
				}
				Log.d("SCAN", QSLEjecutar);
				st = conn.createStatement();
				st.execute("USE erp_osca_test;");
				
				rs = st.executeQuery(QSLEjecutar);
				articleInfoData.clear();
				articleInfoData.add(new ArticlePickingInfo("Articulo", "Alias", "Cantidad", Color.WHITE, (byte) 0));
				RegistroLog.appendLog("Consulta Articulo Pedido " + params[2] + " Pendientes -> " + QSLEjecutar);
				while (rs.next()) {
					if (!albaran) {
						if (rs.getInt("pedidoEnviado") == -1)
							articleInfoData.add(new ArticlePickingInfo(rs.getString("Articulo"), rs.getString("Alias"),
									String.valueOf(rs.getInt("Cantidad")), Color.GREEN, (byte) 3,
									rs.getInt("pd.Posicion"), rs.getInt("Registro")));

						// else if (rs.getInt("pedidoEnviado") == -1 &&
						// rs.getInt("pedidoSacado") == 0)
						// articleInfoData.add(new
						// ArticlePickingInfo(rs.getString("pd.Articulo"),
						// rs.getString("pd.Alias"),
						// String.valueOf(rs.getInt("pd.Cantidad")),
						// Color.GREEN, (byte) 2));
						// else if (rs.getInt("pedidoEnviado") == 0 &&
						// rs.getInt("pedidoSacado") == -1)
						// articleInfoData.add(new
						// ArticlePickingInfo(rs.getString("pd.Articulo"),
						// rs.getString("pd.Alias"),
						// String.valueOf(rs.getInt("pd.Cantidad")),
						// Color.rgb(255, 165, 0), (byte) 1));
						else
							articleInfoData.add(new ArticlePickingInfo(rs.getString("Articulo"), rs.getString("Alias"),
									String.valueOf(rs.getInt("Cantidad")), Color.WHITE, (byte) 0,
									rs.getInt("pd.Posicion"), rs.getInt("Registro")));
					} else
						articleInfoData.add(new ArticlePickingInfo(rs.getString("Articulo"), rs.getString("Alias"),
								String.valueOf(rs.getInt("Cantidad")), Color.WHITE, (byte) 0, rs.getInt("pd.Posicion"),
								rs.getInt("Registro")));

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
//		super.onPostExecute(result);
//		ArticlePickingAdapter loc = new ArticlePickingAdapter(this.activity, articleInfoData);
//		articleInfo.setAdapter(loc);
//		loc.notifyDataSetChanged();
	}

}
