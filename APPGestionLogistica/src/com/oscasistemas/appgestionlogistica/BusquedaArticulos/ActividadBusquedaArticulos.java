package com.oscasistemas.appgestionlogistica.BusquedaArticulos;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import com.MapeoBD.Articulo.MapeoArticulo;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.informacionArticulo.ActividadInformacionArticulo;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ActividadBusquedaArticulos extends Activity {
	private Button searchCodeButton;
	private ImageButton backButton, homeBtn;

	private enum Ordenacion {
		DESCRIPCION, CODIGO
	};

	private Ordenacion tipoOrdenacion;

	private Spinner ordenacion;
	private ListView articleList;
	private EditText textQuery;
	private AdaptadorListaArticulos adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		tipoOrdenacion = Ordenacion.CODIGO;

		this.setContentView(R.layout.articlesearch_activity);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		AuxiliarFunctions.hideKeyboard(ActividadBusquedaArticulos.this);

		inicializarCampoBusqueda();
		inicializarBotonHome();
		inicializarBotonAtras();
		inicializarBotonBusquedaTexto();
		inicializarListaArticulos();
		inicializarOrdenacionArticulos();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {

			if (resultCode == RESULT_OK) {
				Intent i = new Intent(getBaseContext(), ActividadInformacionArticulo.class);
				i.putExtra("CODE", data.getStringExtra("SCAN_RESULT"));
				startActivity(i);

			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this.getBaseContext(), "No se ha encontrado el campo.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void inicializarCampoBusqueda() {
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

		this.backButton = (ImageButton) findViewById(R.id.backButton);
		this.backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(), MenuActivity.class);
				startActivityForResult(intent, 1);

			}
		});
	}

	private void inicializarBotonBusquedaTexto() {
		this.textQuery = (EditText) findViewById(R.id.busquedaTexto);
		this.textQuery.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (AuxiliarFunctions.isNumeric(textQuery.getText().toString().toUpperCase(Locale.ENGLISH)))
					buscarArticulosPorCodigo();
				else
					buscarArticulosPorTexto();

			}
		});
		this.textQuery.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
					AuxiliarFunctions.hideKeyboard(ActividadBusquedaArticulos.this);
					return true;
				}
				return false;
			}
		});
	}

	private void buscarArticulosPorCodigo() {
		new AsyncTask<Object, Object, Object>() {
			ArrayList<MapeoArticulo> aux = null;

			@Override
			protected Object doInBackground(Object... params) {
				try {
					switch (tipoOrdenacion) {
					case CODIGO:
						aux = new MapeoArticulo().getArticulosCodigoASC(AuxiliarFunctions.articlePointFormatSearch(
								textQuery.getText().toString().toUpperCase(Locale.ENGLISH)), 30);

						break;
					case DESCRIPCION:
						aux = new MapeoArticulo().getArticulosCodigoOrdDescripcionASC(AuxiliarFunctions
								.articlePointFormatSearch(textQuery.getText().toString().toUpperCase(Locale.ENGLISH)),
								30);
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				adapter = new AdaptadorListaArticulos(getApplicationContext(), aux);
				articleList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			};

		}.execute();

	}

	private void buscarArticulosPorTexto() {
		new AsyncTask<Object, Object, Object>() {
			ArrayList<MapeoArticulo> aux = null;

			@Override
			protected Object doInBackground(Object... params) {
				try {
					switch (tipoOrdenacion) {
					case CODIGO:
						aux = new MapeoArticulo().getArticulosTextoORDArticuloASC(
								textQuery.getText().toString().toUpperCase(Locale.ENGLISH), 30);
						break;
					case DESCRIPCION:
						aux = new MapeoArticulo().getArticulosTextoORDDescripcionASC(
								textQuery.getText().toString().toUpperCase(Locale.ENGLISH), 30);
						break;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				adapter = new AdaptadorListaArticulos(getApplicationContext(), aux);
				articleList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			};

		}.execute();

	}

	private void inicializarListaArticulos() {
		/**
		 * Article list
		 */

		this.articleList = (ListView) findViewById(R.id.Articles);
		this.articleList.setOnItemClickListener(new OnItemClickListener() {

			/**
			 * Mostrar informacion del articulo
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(), ActividadInformacionArticulo.class);
				MapeoArticulo articulo = (MapeoArticulo) parent.getAdapter().getItem(position);
				intent.putExtra("Articulo", articulo);
				startActivity(intent);

			}
		});

		this.articleList.setOnItemLongClickListener(new OnItemLongClickListener() {

			/**
			 * Imprimir etiqueta
			 */
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final MapeoArticulo selItem = (MapeoArticulo) parent.getAdapter().getItem(position);

				new AlertDialog.Builder(ActividadBusquedaArticulos.this).setTitle("Imprimimr")
						.setMessage("Quieres imprimir el articulo :" + selItem.getArticulo())
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// continue with delete
								AuxiliarFunctions.imprimirEtiqueta("a//" + selItem.getArticulo() + "//"
										+ selItem.getAlias() + "//" + selItem.getDescripcion());
							}
						}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// do nothing
							}
						}).setIcon(android.R.drawable.ic_dialog_alert).show();

				return true;
			}
		});
	}

	private void inicializarOrdenacionArticulos() {
		this.ordenacion = (Spinner) findViewById(R.id.OrdenSpinner);
		this.ordenacion.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.Busqueda_Articulo)));

		this.ordenacion.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// TODO Auto-generated method stub
				if (pos == 0) {

					tipoOrdenacion = Ordenacion.CODIGO;
					if (AuxiliarFunctions.isNumeric(textQuery.getText().toString().toUpperCase(Locale.ENGLISH)))
						buscarArticulosPorCodigo();
					else
						buscarArticulosPorTexto();
				} else if (pos == 1) {
					tipoOrdenacion = Ordenacion.DESCRIPCION;
					if (AuxiliarFunctions.isNumeric(textQuery.getText().toString().toUpperCase(Locale.ENGLISH)))
						buscarArticulosPorCodigo();
					else
						buscarArticulosPorTexto();

				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}
}
