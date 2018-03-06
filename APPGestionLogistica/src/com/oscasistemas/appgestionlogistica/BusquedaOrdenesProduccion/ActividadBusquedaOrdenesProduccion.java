package com.oscasistemas.appgestionlogistica.BusquedaOrdenesProduccion;

import java.sql.SQLException;
import java.util.ArrayList;
import com.MapeoBD.Ordenes.MapeoOrden;
import com.MapeoBD.Ordenes.TipoOrden;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion.ActividadInformacionOrdenProd;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.R.color;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ActividadBusquedaOrdenesProduccion extends Activity {

	private Button searchCodeButton;
	private ImageButton backButton, homeBtn;
	private Button generadas, terminadas;
	private ListView listaOrdenesProduccion;
	private EditText busquedaTexto;
	private AdaptadorListaOrdenesProduccion adapter;

	private TipoOrden tipo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.production_activity);
		AuxiliarFunctions.hideKeyboard(ActividadBusquedaOrdenesProduccion.this);

		inicializarBotonAtras();
		inicializarBotonHome();
		inicializarListaOrdenesProduccion();
		inicializarBotonGeneradas();
		inicializarBotonTerminadas();
		inicializarCampoBusqueda();
		inicializarBusquedaCodigoBarras();
		

	}

	private void inicializarListaOrdenesProduccion() {
		// TODO Auto-generated method stub
		this.listaOrdenesProduccion = (ListView) findViewById(R.id.Orders);
		this.listaOrdenesProduccion.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getBaseContext(), ActividadInformacionOrdenProd.class);
				// send article
				MapeoOrden ordenProduccion = (MapeoOrden) parent.getAdapter().getItem(position);
				intent.putExtra("OrdenProduccion", ordenProduccion);
				startActivity(intent);

			}
		});
		tipo = TipoOrden.GENERADA;
		obtenerListaOrdenes();

	}
	private void inicializarBusquedaCodigoBarras() {
		// TODO Auto-generated method stub
		this.searchCodeButton = (Button) findViewById(R.id.searchCodeButton);
		this.searchCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {

					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					startActivityForResult(intent, 0);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

	}

	private void inicializarCampoBusqueda() {
		// TODO Auto-generated method stub
		this.busquedaTexto = (EditText) findViewById(R.id.busquedaTexto);
		this.busquedaTexto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				listaOrdenesProduccion.setAdapter(null);
				obtenerListaOrdenes();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void inicializarBotonTerminadas() {
		// TODO Auto-generated method stub
		this.terminadas = (Button) findViewById(R.id.boton_terminadas);
		this.terminadas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				generadas.setBackgroundColor(Color.WHITE);
				generadas.setTextColor(Color.BLACK);
				terminadas.setBackgroundColor(color.button_material_dark);
				terminadas.setTextColor(Color.WHITE);
				tipo=TipoOrden.TERMINADA;
				obtenerListaOrdenes();
			}
		});

	}

	private void inicializarBotonGeneradas() {
		// TODO Auto-generated method stub
		this.generadas = (Button) findViewById(R.id.boton_generadas);
		this.generadas.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				generadas.setBackgroundColor(color.button_material_dark);
				generadas.setTextColor(Color.WHITE);
				terminadas.setBackgroundColor(Color.WHITE);
				terminadas.setTextColor(Color.BLACK);
				tipo = TipoOrden.GENERADA;
				// new ConexionMostrarOrdenes(ProductionActivity.this,
				// receivedList, modo).execute();
				obtenerListaOrdenes();
			}
		});

	}

	private void inicializarBotonHome() {
		// TODO Auto-generated method stub
		this.homeBtn = (ImageButton) findViewById(R.id.homeBtn);
		this.homeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finishAffinity();
				startActivity(intent);

			}
		});

	}

	private void inicializarBotonAtras() {
		// TODO Auto-generated method stub
		this.backButton = (ImageButton) findViewById(R.id.backButton);
		this.backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), MenuActivity.class);
				finish();
				startActivity(i);

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {

			if (resultCode == RESULT_OK) {
				Intent intent = new Intent(getBaseContext(), ActividadInformacionOrdenProd.class);
				intent.putExtra("PRODORDER", data.getStringExtra("SCAN_RESULT"));
				intent.putExtra("REF", "");
				intent.putExtra("CLIENT", "");
				startActivity(intent);

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this.getBaseContext(), "No se ha encontrado el campo.", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	
	private void obtenerListaOrdenes() {
		new AsyncTask<Object, Object, Object>() {
			ArrayList<MapeoOrden> aux = null;
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadBusquedaOrdenesProduccion.this);
			
			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando ordenes de produccion.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					Log.d("SCAN", "Inicio busqueda codigo");
						if(busquedaTexto.getText().toString().equals("")){
							aux = new MapeoOrden().getOrdenesProduccion(tipo, 50);
						}
						//busqueda por orden de produccion
						else if(AuxiliarFunctions.isNumeric(busquedaTexto.getText().toString()))
							aux = new MapeoOrden().getOrdenesProduccion(Integer.valueOf(busquedaTexto.getText().toString()),tipo, 50);
						//busqueda por texto
						else
							aux = new MapeoOrden().getOrdenesProduccion(busquedaTexto.getText().toString(),tipo, 50);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				adapter = new AdaptadorListaOrdenesProduccion(getApplicationContext(), aux);
				listaOrdenesProduccion.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}

}
