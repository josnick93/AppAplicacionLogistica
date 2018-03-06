package com.oscasistemas.appgestionlogistica.LocalizacionArticulos;

import java.sql.SQLException;
import java.util.ArrayList;
import com.MapeoBD.Articulo.MapeoArticulo;
import com.MapeoBD.Localizacion.MapeoAlmacen;
import com.MapeoBD.Localizacion.MapeoLift;
import com.MapeoBD.Localizacion.MapeoLocalizacion;
import com.MapeoBD.Localizacion.MapeoShelf;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.BusquedaArticulos.ActividadBusquedaArticulos;
import com.oscasistemas.appgestionlogistica.BusquedaArticulos.AdaptadorListaArticulos;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.informacionArticulo.ActividadInformacionArticulo;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ActividadLocalizacionArticulos extends Activity {
	private ImageButton backButton, homeBtn;
	private Spinner almacenSpinner, estanteriaSpinner, locationSpinner;
	private MapeoAlmacen almacenSelecionado;
	private MapeoLift estanteriaSelecionada;
	private MapeoShelf localizacionSeleccionada;
	private EditText origin, dest, movementText;
	private LinearLayout movement;

	private AdaptadorListaArticulos adapter;

	private ListView articleList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_activity);

		inicializarBackButton();
		inicializarHomeButton();
		inicializarAlmacen();
		inicializarEstanteria();
		inicializarLocalizacion();
		inicializarListaArticulos();

		this.movement = (LinearLayout) findViewById(R.id.movement);
		this.movementText = (EditText) findViewById(R.id.movementText);

		/**
		 * Movement
		 */

		this.origin = (EditText) findViewById(R.id.originEditText);
		this.origin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					startActivityForResult(intent, 0);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "ERROR:" + e, Toast.LENGTH_SHORT).show();
				}
			}
		});

		this.dest = (EditText) findViewById(R.id.destinyEditText);
		this.dest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					Intent intent = new Intent("com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					startActivityForResult(intent, 1);

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "ERROR:" + e, Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this.getBaseContext(), "Articulo encontrado.", Toast.LENGTH_SHORT).show();
				// show articled searched info
				Intent i = new Intent(getBaseContext(), ActividadInformacionArticulo.class);
				this.origin.setText(data.getStringExtra("SCAN_RESULT"));
				this.origin.getBackground().setColorFilter(Color.GREEN, Mode.MULTIPLY);
				i.putExtra("code", data.getStringExtra("SCAN_RESULT"));
				startActivity(i);

			} else if (resultCode == RESULT_CANCELED) {
				this.origin.getBackground().setColorFilter(Color.RED, Mode.MULTIPLY);
				Toast.makeText(this.getBaseContext(), "No se ha encontrado el campo.", Toast.LENGTH_SHORT).show();
			}
		} else if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				Toast.makeText(this.getBaseContext(), "Articulo encontrado.", Toast.LENGTH_SHORT).show();
				// show articled searched info
				Intent i = new Intent(getBaseContext(), ActividadInformacionArticulo.class);
				this.dest.setText(data.getStringExtra("SCAN_RESULT"));
				this.dest.getBackground().setColorFilter(Color.GREEN, Mode.MULTIPLY);
				i.putExtra("code", data.getStringExtra("SCAN_RESULT"));
				startActivity(i);

			} else if (resultCode == RESULT_CANCELED) {
				this.dest.getBackground().setColorFilter(Color.RED, Mode.MULTIPLY);
				Toast.makeText(this.getBaseContext(), "No se ha encontrado el campo.", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void inicializarBackButton() {
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

	private void inicializarHomeButton() {
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

	private void inicializarAlmacen() {
		/**
		 * warehouse
		 */
		this.almacenSpinner = (Spinner) findViewById(R.id.WarehouseSpinner);
		this.almacenSpinner.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// Your code
					buscarAlmacen();
				}

				return false;
			}
		});
		/**
		 * Obtener el almacen seleccionado
		 */
		this.almacenSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				almacenSelecionado = (MapeoAlmacen) parent.getAdapter().getItem(position);
				if (almacenSelecionado != null && estanteriaSelecionada != null && localizacionSeleccionada != null)
					buscarArticulos();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void buscarAlmacen() {
		new AsyncTask<Object, Object, Object>() {
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadLocalizacionArticulos.this);
			ArrayList<MapeoAlmacen> almacenes = new ArrayList<MapeoAlmacen>();

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando almacenes.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					almacenes = new MapeoAlmacen().getAlmacenes();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				ArrayAdapter<MapeoAlmacen> dataAdapter = new ArrayAdapter<MapeoAlmacen>(
						ActividadLocalizacionArticulos.this, android.R.layout.simple_spinner_item, almacenes);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				almacenSpinner.setAdapter(dataAdapter);
				almacenSelecionado = almacenes.get(0);
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}

	/**
	 * Inicializacion de la estanteria
	 */
	private void inicializarEstanteria() {
		this.estanteriaSpinner = (Spinner) findViewById(R.id.UbicationSpinner);
		if (almacenSelecionado != null)
			buscarEstanterias();
		this.estanteriaSpinner.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// Your code
					if (almacenSelecionado != null) {
						buscarEstanterias();
					}
				}

				return false;
			}
		});
		this.estanteriaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				estanteriaSelecionada = (MapeoLift) parent.getAdapter().getItem(position);
				if (almacenSelecionado != null && estanteriaSelecionada != null && localizacionSeleccionada != null)
					buscarArticulos();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

	}

	/**
	 * Metodo que busca las estanterias de un almacen
	 */
	private void buscarEstanterias() {
		new AsyncTask<Object, Object, Object>() {
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadLocalizacionArticulos.this);
			ArrayList<MapeoLift> estanterias = new ArrayList<MapeoLift>();

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando estanterias.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					estanterias = new MapeoLift().getLifts(almacenSelecionado.getAlmacen());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				ArrayAdapter<MapeoLift> dataAdapter = new ArrayAdapter<MapeoLift>(ActividadLocalizacionArticulos.this,
						android.R.layout.simple_spinner_item, estanterias);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				estanteriaSpinner.setAdapter(dataAdapter);
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}

	private void inicializarLocalizacion() {
		this.locationSpinner = (Spinner) findViewById(R.id.LocationSpinner);
		this.locationSpinner.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// Your code
					if (almacenSelecionado != null && estanteriaSelecionada != null) {
						buscarLocalizacion();
					}
				}

				return false;
			}
		});

		this.locationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				localizacionSeleccionada = (MapeoShelf) parent.getAdapter().getItem(position);
				if (almacenSelecionado != null && estanteriaSelecionada != null && localizacionSeleccionada != null)
					buscarArticulos();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
			}
		});
	}

	/**
	 * Metodo que busca las estanterias de un almacen
	 */
	private void buscarLocalizacion() {
		new AsyncTask<Object, Object, Object>() {
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadLocalizacionArticulos.this);
			ArrayList<MapeoShelf> estanterias = new ArrayList<MapeoShelf>();

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando estanterias.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					estanterias = new MapeoShelf().getShelfs(estanteriaSelecionada.getLift());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				ArrayAdapter<MapeoShelf> dataAdapter = new ArrayAdapter<MapeoShelf>(ActividadLocalizacionArticulos.this,
						android.R.layout.simple_spinner_item, estanterias);
				dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				locationSpinner.setAdapter(dataAdapter);
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}

	private void inicializarListaArticulos() {
		this.articleList = (ListView) findViewById(R.id.Articles);
		this.articleList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(), ActividadInformacionArticulo.class);
				// send article
				MapeoArticulo selItem = (MapeoArticulo) parent.getAdapter().getItem(position); //
				intent.putExtra("Articulo", selItem);
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

				new AlertDialog.Builder(ActividadLocalizacionArticulos.this).setTitle("Imprimimr")
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

	private void buscarArticulos() {
		new AsyncTask<Object, Object, Object>() {
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadLocalizacionArticulos.this);
			ArrayList<MapeoArticulo> articulos = new ArrayList<MapeoArticulo>();

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando  Articulos.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					articulos = new MapeoLocalizacion().getLocationArticles(localizacionSeleccionada.getLift(),
							localizacionSeleccionada.getShelf());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				adapter = new AdaptadorListaArticulos(getApplicationContext(), articulos);
				articleList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}
}
