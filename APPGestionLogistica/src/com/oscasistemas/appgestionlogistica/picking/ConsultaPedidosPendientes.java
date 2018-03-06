package com.oscasistemas.appgestionlogistica.picking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.articulo.ArticlePickingInfo;
import com.oscasistemas.appgestionlogistica.listViews.ArticlePickingAdapter;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

public class ConsultaPedidosPendientes extends AsyncTask<String, Void, String> {
	private ArrayList<ArticlePickingInfo> articleInfoData;
	private Activity activity;
	private ListView articleInfo;

	public ConsultaPedidosPendientes(Activity activity, ArrayList<ArticlePickingInfo> articleInfoData, ListView articleInfo) {
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
						//recibo albaran
						QSLEjecutar = "SELECT pd.Articulo, pd.Alias , pd.Cantidad , 0 AS pedidoEnviado , 0 AS pedidoSacado FROM palbarandetalle pd  WHERE pd.Albaran="
								+ params[2] + " ORDER BY pd.Posicion ASC ";
						//recibo pedido
					else
						QSLEjecutar = "SELECT pid.Articulo, pid.Alias , pid.Cantidad , pid.swAsignada AS pedidoEnviado, pid.swSelect01 AS pedidoSacado "
								+ "FROM pickingdetalle pid WHERE pid.Picking="
								+ params[2] +" ORDER BY pd.Posicion ASC ";
				} else {
					if (albaran)
						//envio albaran
						QSLEjecutar = "SELECT pd.Articulo, pd.Alias , pd.Cantidad , pd.swAsignada AS pedidoEnviado, pid.swSelect01 AS pedidoSacado FROM albarandetalle pd WHERE pd.Albaran="
								+ params[2] +" ORDER BY pd.Posicion ASC ";
					else
						//envio cliente
						QSLEjecutar = "SELECT pd.Articulo, pd.Alias , pd.Cantidad , pd.swAsignada AS pedidoEnviado, pid.swSelect01 AS pedidoSacado FROM pedidodetalle pd , pickingdetalle pid WHERE pd.Pedido="
								+ params[2] + " AND pid.Picking=" + params[2]
								+ " AND pd.Posicion=pid.Posicion ORDER BY pd.Posicion ASC  ";
				}
				Log.d("SCAN", QSLEjecutar);
				st = conn.createStatement();
				st.execute("USE erp_osca_test;");
				rs = st.executeQuery(QSLEjecutar);
				articleInfoData.clear();
				articleInfoData.add(new ArticlePickingInfo("Articulo", "Alias", "Cantidad", Color.WHITE,(byte) 0));
				while (rs.next()) {
					if (!albaran) {
						if (rs.getInt("pedidoEnviado") == -1 && rs.getInt("pedidoSacado") == 0)
							articleInfoData.add(new ArticlePickingInfo(rs.getString("pd.Articulo"),
									rs.getString("pd.Alias"), String.valueOf(rs.getInt("pd.Cantidad")),
									Color.rgb(255, 165, 0), (byte) 2));
						else if (rs.getInt("pedidoEnviado") == 0 && rs.getInt("pedidoSacado") == -1)
							articleInfoData.add(new ArticlePickingInfo(rs.getString("pd.Articulo"),
									rs.getString("pd.Alias"), String.valueOf(rs.getInt("pd.Cantidad")),
									Color.rgb(255, 165, 0), (byte) 1));
						else if (rs.getInt("pedidoEnviado") == 0 && rs.getInt("pedidoSacado") == 0)
							articleInfoData.add(new ArticlePickingInfo(rs.getString("pd.Articulo"),
									rs.getString("pd.Alias"), String.valueOf(rs.getInt("pd.Cantidad")), Color.WHITE,(byte) 0));
					} else
						articleInfoData.add(new ArticlePickingInfo(rs.getString("pd.Articulo"),
								rs.getString("pd.Alias"), String.valueOf(rs.getInt("pd.Cantidad")), Color.WHITE,(byte) 0));

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
//		ArticlePickingAdapter loc = new ArticlePickingAdapter(this.activity, articleInfoData);
//		articleInfo.setAdapter(loc);
//		loc.notifyDataSetChanged();
	}

}
