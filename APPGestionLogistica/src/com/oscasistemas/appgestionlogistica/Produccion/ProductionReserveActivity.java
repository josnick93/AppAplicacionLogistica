package com.oscasistemas.appgestionlogistica.Produccion;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.BusquedaArticulos.ActividadBusquedaArticulos;
import com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion.AdaptadorComponentesOrdenProd;
import com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion.InformacionComponenteOrdenProd;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ProductionReserveActivity extends Activity {

	private ImageButton backButton, homeBtn;
	private ArrayList<InformacionComponenteOrdenProd> components;
	private ListView componentsList;

	private TextView nOrdenProd;
	private EditText cantidad;
	private AutoResizeTextView ref, client;
	private String articleCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.production_info_activity);
		AuxiliarFunctions.hideKeyboard(this);
		articleCode = getIntent().getExtras().getString("ARTICLE");
		this.components = new ArrayList<InformacionComponenteOrdenProd>();

		
		AuxiliarFunctions.hideKeyboard(ProductionReserveActivity.this);
		
		
		this.nOrdenProd = (TextView) findViewById(R.id.ArticleCode);
		this.ref = (AutoResizeTextView) findViewById(R.id.ArticleAlias);
		this.client = (AutoResizeTextView) findViewById(R.id.ClientText);
		this.cantidad = (EditText) findViewById(R.id.avaiableArticles);

		this.nOrdenProd.setText(String.valueOf(getIntent().getExtras().getString("PRODORDER")));
		this.ref.setText(getIntent().getExtras().getString("REF"));
		this.client.setText(getIntent().getExtras().getString("CLIENT"));
		this.cantidad.setText(getIntent().getExtras().getString("CANTIDAD"));

		this.backButton = (ImageButton) findViewById(R.id.backButton);

		this.componentsList = (ListView) findViewById(R.id.infoArticleList);
		new connect().execute();

		this.backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		this.homeBtn = (ImageButton) findViewById(R.id.homeBtn);
		this.homeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
				finishAffinity();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});
	}

	/**
	 * Ordenes pendientes
	 * 
	 */
	private class connect extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.2.2:3306/erp_osca", "usuario",
						"osca-SiStEmAs");

				Statement st = null;
				ResultSet rs = null;

				try {
					String QSLEjecutar = "SELECT a.Articulo, a.Alias, i.Cantidad, a.Stock, a.PendienteServir FROM articulo a, ingrediente i WHERE i.Articulo="
							+ articleCode + " AND i.Ingrediente=a.Articulo";
					st = conn.createStatement();
					st.addBatch("USE erp_osca");
					rs = st.executeQuery(QSLEjecutar);
					components.clear();
//					components.add(new InformacionComponenteOrdenProd("Componentes"));
//					components.add(new InformacionComponenteOrdenProd("Articulo", "Alias", "Cantidad"));
//					while (rs.next()) {
//						double result = rs.getDouble("a.Stock") - rs.getDouble("a.PendienteServir")
//								- (Double.valueOf(cantidad.getText().toString()) * rs.getDouble("i.Cantidad"));
//						components.add(new InformacionComponenteOrdenProd(rs.getString("a.Articulo"), rs.getString("a.Alias"),
//								rs.getString("i.Cantidad"), String.valueOf(result), Color.WHITE));
//					}
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
			AdaptadorComponentesOrdenProd inf = new AdaptadorComponentesOrdenProd(ProductionReserveActivity.this, components);
			componentsList.setAdapter(inf);
			inf.notifyDataSetChanged();
		}

	}

}
