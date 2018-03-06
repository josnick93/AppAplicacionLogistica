package com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion;

import java.sql.SQLException;


import java.util.ArrayList;

import com.MapeoBD.Articulo.MapeoArticulo;
import com.MapeoBD.Cliente.MapeoCliente;
import com.MapeoBD.Ordenes.MapeoOrden;
import com.MapeoBD.Ordenes.MapeoOrdenDetalle;
import com.MapeoBD.Picking.MapeoPickingDetalle;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.informacionArticulo.ActividadInformacionArticulo;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ActividadInformacionOrdenProd extends Activity {

	private ImageButton backButton, homeBtn;
	private ArrayList<InformacionComponenteOrdenProd> picking;
	ArrayList<InformacionComponenteOrdenProd> componentes;
	private ListView listaComponentes;
	private Button dispButton;
	private TextView nOrdenProd;
	private AutoResizeTextView ref, client;
	private boolean modoDisponibilidad;

	private MapeoOrden ordenProduccion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.production_info_activity);
		AuxiliarFunctions.hideKeyboard(ActividadInformacionOrdenProd.this);
		modoDisponibilidad = false;
		// inicializacion

		inicializarBotonAtras();
		inicializarInformacionOrden();
		inicializarBotonHome();
		inicializarBotonDisponibilidad();
		inicializarListaComponentes();

	}

	private void inicializarInformacionOrden() {
		ordenProduccion = (MapeoOrden) getIntent().getExtras().getSerializable("OrdenProduccion");
		this.nOrdenProd = (TextView) findViewById(R.id.ArticleCode);
		this.ref = (AutoResizeTextView) findViewById(R.id.ArticleAlias);
		this.client = (AutoResizeTextView) findViewById(R.id.ClientText);

		this.nOrdenProd.setText(String.valueOf(ordenProduccion.getOrden()));
		this.ref.setText(ordenProduccion.getReferencia());
		this.client.setText(obtenerCliente(ordenProduccion.getCliente()).getNombre());

	}

	private void inicializarListaComponentes() {
		this.listaComponentes = (ListView) findViewById(R.id.infoArticleList);
		this.listaComponentes.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				InformacionComponenteOrdenProd componenteDetalle = (InformacionComponenteOrdenProd) parent.getAdapter()
						.getItem(position);

				// obtener articulo
				Intent intent = new Intent(getBaseContext(), ActividadInformacionArticulo.class);
				MapeoArticulo articulo = null;
				switch (componenteDetalle.getTipoComponente()) {
				case COMPONENTE:
					articulo = obtenerArticulo(((MapeoOrdenDetalle) componenteDetalle.getObjeto()).getArticulo());
					intent.putExtra("Articulo", articulo);
					startActivity(intent);
					break;
				case PICKING:
					articulo = obtenerArticulo(((MapeoPickingDetalle) componenteDetalle.getObjeto()).getArticulo());
					intent.putExtra("Articulo", articulo);
					startActivity(intent);
					break;
				case TITULO:
					break;
				default:
					break;

				}

			}
		});

		this.listaComponentes.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Builder builder = new Builder(ActividadInformacionOrdenProd.this);
				final InformacionComponenteOrdenProd componenteDetalle = (InformacionComponenteOrdenProd) parent
						.getAdapter().getItem(position);

				switch (componenteDetalle.getTipoComponente()) {
				case COMPONENTE:
					builder.setMessage("Marcar/Desmarcar articulo: " + AuxiliarFunctions
							.format(((MapeoOrdenDetalle) componenteDetalle.getObjeto()).getArticulo(), "xx.xxx.xxxx"));
					builder.setPositiveButton("Confirmar", new android.content.DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// marcado
							marcarComponente(componenteDetalle, -1);
						}
					});
					builder.setNegativeButton("Cancelar", null);
					builder.create().show();
					return true;
				case PICKING:
					// numero elegir cantidad
					seleccionarPicking(componenteDetalle);
					break;
				case TITULO:
					break;
				default:
					break;

				}
				return true;
			}
		});
		if (modoDisponibilidad) {
			mostrarPickingOrdenProduccion();
		} else {
			mostrarComponentesOrdenProduccion();
		}

	}

	private void seleccionarPicking(final InformacionComponenteOrdenProd pickingdetalle) {
		AlertDialog.Builder alert = new AlertDialog.Builder(ActividadInformacionOrdenProd.this);
		alert.setTitle("Componentes recibidos:");
		final EditText input = new EditText(ActividadInformacionOrdenProd.this);
		input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		input.setRawInputType(Configuration.KEYBOARD_12KEY);
		input.setText(String.valueOf(((MapeoPickingDetalle) pickingdetalle.getObjeto()).getCantidad()));
		alert.setView(input);

		alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				marcarComponente(pickingdetalle, Double.valueOf(input.getText().toString()));
			}
		});
		if (((MapeoPickingDetalle) pickingdetalle.getObjeto()).getStock()
				- ((MapeoPickingDetalle) pickingdetalle.getObjeto()).getCantidad() >= 0.0)
			alert.show();
	}

	private void mostrarComponentesOrdenProduccion() {
		new AsyncTask<Object, Object, Object>() {
			ArrayList<InformacionComponenteOrdenProd> aux = new ArrayList<InformacionComponenteOrdenProd>();
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadInformacionOrdenProd.this);

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando componentes de la orden de produccion.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					ArrayList<MapeoOrdenDetalle> aux1 = new MapeoOrdenDetalle()
							.getComponentesOrden(ordenProduccion.getOrden());
					// crear la informacion de la orden detalle
					aux.add(new InformacionComponenteOrdenProd("Componentes"));
					for (MapeoOrdenDetalle od : aux1) {
						aux.add(new InformacionComponenteOrdenProd(od));
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				AdaptadorComponentesOrdenProd adapter = new AdaptadorComponentesOrdenProd(getApplicationContext(), aux);
				listaComponentes.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				componentes = aux;
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}

	private void mostrarPickingOrdenProduccion() {
		new AsyncTask<Object, Object, Object>() {
			ArrayList<InformacionComponenteOrdenProd> aux = new ArrayList<InformacionComponenteOrdenProd>();
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadInformacionOrdenProd.this);

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando picking de la orden de produccion.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					ArrayList<MapeoPickingDetalle> aux1 = new MapeoPickingDetalle()
							.getComponentesOrden(ordenProduccion.getOrden());

					// crear la informacion de la orden detalle
					aux.add(new InformacionComponenteOrdenProd("Disponibilidad"));
					for (MapeoPickingDetalle od : aux1) {
						aux.add(new InformacionComponenteOrdenProd(od));
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				AdaptadorComponentesOrdenProd adapter = new AdaptadorComponentesOrdenProd(getApplicationContext(), aux);
				listaComponentes.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				picking = aux;
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}

	private void inicializarBotonDisponibilidad() {
		this.dispButton = (Button) findViewById(R.id.dispButton);
		this.dispButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				modoDisponibilidad = !modoDisponibilidad;
				if (modoDisponibilidad) {
					dispButton.setText("Disponibilidad");
					if (picking == null)
						mostrarPickingOrdenProduccion();
					else {
						dispButton.setText("Componentes");
						AdaptadorComponentesOrdenProd adapter = new AdaptadorComponentesOrdenProd(
								getApplicationContext(), picking);
						listaComponentes.setAdapter(adapter);
						adapter.notifyDataSetChanged();

					}
				} else {
					if (componentes == null)
						mostrarComponentesOrdenProduccion();
					else {
						AdaptadorComponentesOrdenProd adapter = new AdaptadorComponentesOrdenProd(
								getApplicationContext(), componentes);
						listaComponentes.setAdapter(adapter);
						adapter.notifyDataSetChanged();

					}
				}
			}
		});

	}

	private void inicializarBotonHome() {
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

	private void inicializarBotonAtras() {
		this.backButton = (ImageButton) findViewById(R.id.backButton);
		this.backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	private MapeoCliente obtenerCliente(final int idCliente) {
		AsyncTask<MapeoCliente, MapeoCliente, MapeoCliente> tarea = new AsyncTask<MapeoCliente, MapeoCliente, MapeoCliente>() {
			@Override
			protected MapeoCliente doInBackground(MapeoCliente... params) {
				MapeoCliente cliente = null;
				try {
					cliente = new MapeoCliente(idCliente);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return cliente;

			}
		};
		try {
			return tarea.execute().get();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	private MapeoArticulo obtenerArticulo(final String codigoArticulo) {
		AsyncTask<MapeoArticulo, MapeoArticulo, MapeoArticulo> tarea = new AsyncTask<MapeoArticulo, MapeoArticulo, MapeoArticulo>() {
			@Override
			protected MapeoArticulo doInBackground(MapeoArticulo... params) {
				MapeoArticulo articulo = null;
				try {
					articulo = new MapeoArticulo(codigoArticulo);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return articulo;

			}
		};
		try {
			return tarea.execute().get();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	private void marcarComponente(final InformacionComponenteOrdenProd componente, final double i) {
		new AsyncTask<Object, Object, Object>() {

			protected void onPreExecute() {
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					switch (componente.getTipoComponente()) {

					case COMPONENTE:
						// marcar Componente
						MapeoOrdenDetalle detalle = (MapeoOrdenDetalle) componente.getObjeto();
						if (detalle.getSwAsignada() == 0)
							detalle.setSwAsignada((byte) -1);
						else if (detalle.getSwAsignada() == -1)
							detalle.setSwAsignada((byte) 0);
						detalle.actualizarOrdenDetalle(detalle);
						break;
					case PICKING:

						MapeoPickingDetalle detallepicking = (MapeoPickingDetalle) componente.getObjeto();
						// desdoblar pedido
						if (i < detallepicking.getCantidad()) {
							int registroAnterior = detallepicking.getRegistro();
							Log.d("DEBUG", detallepicking.getPicking()+" "+detallepicking.getPosicion());
							detallepicking.setRegistro(detallepicking.getMaxRegistro(detallepicking.getPicking(),
									detallepicking.getPosicion()) + 1);
							Log.d("DEBUG", ""+detallepicking.getRegistro());
							detallepicking.setCantidad(detallepicking.getCantidad()-i);
							//insertar picking detalle
							detallepicking.insertarPickingDetalle(detallepicking);
							detallepicking.setRegistro(registroAnterior);
							detallepicking.setCantidad(i);
							
						} // marcar picking
						if (detallepicking.getSwAsignada() == 0)
							detallepicking.setSwAsignada((byte) -1);
						else if (detallepicking.getSwAsignada() == -1)
							detallepicking.setSwAsignada((byte) 0);
						detallepicking.actualizarPickingDetalle(detallepicking);

						break;
					case TITULO:
						break;
					default:
						break;

					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {

				if (modoDisponibilidad) {
					mostrarPickingOrdenProduccion();
				} else {
					mostrarComponentesOrdenProduccion();
				}
			};

		}.execute();

	}
}
