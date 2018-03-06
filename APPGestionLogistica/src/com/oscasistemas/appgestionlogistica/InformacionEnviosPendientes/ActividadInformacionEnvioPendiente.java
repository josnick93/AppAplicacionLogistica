package com.oscasistemas.appgestionlogistica.InformacionEnviosPendientes;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.MapeoBD.Articulo.MapeoArticulo;
import com.MapeoBD.Cliente.MapeoCliente;
import com.MapeoBD.Pedidos.MapeoPedido;
import com.MapeoBD.Pedidos.MapeoPedidoDetalle;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.BusquedaEnviosPendientes.ActividadEnviosPendientes;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.articulo.ArticlePickingInfo;
import com.oscasistemas.appgestionlogistica.informacionArticulo.ActividadInformacionArticulo;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.CursorJoiner.Result;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Mode 0 = RECV Mode 1 = SEND
 * 
 * @author Portátil1
 *
 */
public class ActividadInformacionEnvioPendiente extends Activity {

	private TextView codigoPedido, referenciaPedido;
	private ImageButton backButton, homeBtn;
	private ListView listaArticulos;
	private EditText descripcion;
	private Button filtrado;
	private boolean total;
	private int mode;
	ArrayList<InfoPedidoDetalle> aux;
	private int posicion;

	private MapeoPedido pedido;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picking_activity);

		botonAtras();
		homeBtn();
		inicializarCampos();
		listaArticulos();
		filtrado();
		AuxiliarFunctions.hideKeyboard(ActividadInformacionEnvioPendiente.this);
		buscarArticulosPedido();

		// this.consultaPedidos = new ConsultaPedidos(this, articleInfoData,
		// articleInfo);
		// this.consultaPedidos.execute(String.valueOf(this.mode),
		// String.valueOf(this.albaran),
		// codigoPedido.getText().toString());

		// this.articleInfo.setOnItemLongClickListener(new
		// OnItemLongClickListener() {
		//
		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view, int
		// position, long id) {
		//
		// final ArrayAdapter<String> arrayAdapter = new
		// ArrayAdapter<String>(PickingActivity.this,
		// android.R.layout.select_dialog_item);
		// arrayAdapter.add(" Marcar/Desmarcar");
		// arrayAdapter.add(" Imprimir");
		//
		// selItem = (ArticlePickingInfo) parent.getAdapter().getItem(position);
		// AlertDialog.Builder alert = new
		// AlertDialog.Builder(PickingActivity.this);
		// posicion = position;
		// alert.setTitle("Articulos a enviar:");
		// final EditText input = new EditText(PickingActivity.this);
		// input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		// input.setRawInputType(Configuration.KEYBOARD_12KEY);
		// input.setText(selItem.getCantidad());
		// alert.setView(input);
		//
		// alert.setNegativeButton("cancel", new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		// alert.setAdapter(arrayAdapter, new DialogInterface.OnClickListener()
		// {
		//

	}

	private void filtrado() {
		this.filtrado = (Button) findViewById(R.id.BotonFiltrado);
		this.filtrado.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				total = !total;
				if (total) {
					filtrado.setText("Pendientes");
					ArrayList<InfoPedidoDetalle> ipd = new ArrayList<InfoPedidoDetalle>();
					for (InfoPedidoDetalle a : aux) {
						if (a.getPedidoDetalle() != null && a.getPedidoDetalle().getSwAsignada() != -1)
							ipd.add(a);
					}
					AdaptadorEnviosPendientesDetalle inf = new AdaptadorEnviosPendientesDetalle(getApplicationContext(),
							ipd);
					listaArticulos.setAdapter(inf);
					inf.notifyDataSetChanged();
				} else {
					filtrado.setText("Total");
					AdaptadorEnviosPendientesDetalle inf = new AdaptadorEnviosPendientesDetalle(getApplicationContext(),
							aux);
					listaArticulos.setAdapter(inf);
					inf.notifyDataSetChanged();
				}

			}
		});

	}

	private void listaArticulos() {
		this.listaArticulos = (ListView) findViewById(R.id.listaArticulosOrden);
		this.listaArticulos.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				InfoPedidoDetalle selItem = (InfoPedidoDetalle) parent.getAdapter().getItem(position);
				if (!selItem.isCabecera() && selItem.getPedidoDetalle() != null) {
					Intent intent = new Intent(getBaseContext(), ActividadInformacionArticulo.class);
					// send article

					intent.putExtra("Articulo", obtenerArticulo(selItem.getPedidoDetalle()));
					startActivity(intent);
				}
			}
		});

		this.listaArticulos.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final InfoPedidoDetalle selItem = (InfoPedidoDetalle) parent.getAdapter().getItem(position);
				final String[] opciones = { "Marcar","Imprimir","Cancelar" };
				final AlertDialog.Builder myDialog = new AlertDialog.Builder(ActividadInformacionEnvioPendiente.this);
				myDialog.setTitle("Seleccion articulo");
				final EditText input = new EditText(ActividadInformacionEnvioPendiente.this);
				input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
				input.setRawInputType(Configuration.KEYBOARD_12KEY);
				myDialog.setView(input);

				myDialog.setItems(opciones, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							break;
							//marcar
							
						case 1:
							//imprimir
							new AsyncTask<Object, Object, Object>(){

								@Override
								protected Result doInBackground(Object... params) {
									// TODO Auto-generated method stub
									try {
										MapeoCliente c= new MapeoCliente(selItem.getPedidoDetalle().getCliente());
										Log.d("DEBUG",input.getText().toString());
										String[] salida=input.getText().toString().split(";");
										AuxiliarFunctions.imprimirEtiqueta(
												"p//" + c.getNombre() + "//" + selItem.getPedidoDetalle().getArticuloCliente() + "//"
														+ selItem.getPedidoDetalle().getPedido() + "//" + selItem.getPedidoDetalle().getDescripcion() + "//" + salida[0] +"//"+salida[1]);

									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									return null;
								}

							
								
							}.execute();
							break;
						case 2:
							break;
						}

					}
				});

				myDialog.setNegativeButton("Cancel", null);

				myDialog.show();

				return true;
			}

		});

		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// // TODO Auto-generated method stub
		// switch (which) {
		// case 0:
		// // if (selItem.getEstado() == (byte) 0 ||
		// // selItem.getEstado() == (byte) 1)
		// // new
		// // ConexionMarcarArticuloSacado(PickingActivity.this,
		// // articleInfo, articleInfoData,
		// // mode, posicion,
		// // albaran).execute(codigoPedido.getText().toString(),
		// // selItem.getCodigoArticulo(),
		// // selItem.getCantidad(),
		// // input.getText().toString());
		// new ConexionMarcarArticuloEnviado(PickingActivity.this, articleInfo,
		// articleInfoData, mode,
		// selItem.getPosicion(),selItem.getRegistro(),
		// albaran).execute(codigoPedido.getText().toString(),
		// selItem.getCodigoArticulo(), selItem.getCantidad(),
		// input.getText().toString());
		// break;
		// case 1:
		// dialog.dismiss();
		// final NumberPicker picker = new NumberPicker(PickingActivity.this);
		// picker.setValue(1);
		// picker.setMinValue(1);
		// picker.setMaxValue(1000);
		//
		// final FrameLayout layout = new FrameLayout(PickingActivity.this);
		// layout.addView(picker, new FrameLayout.LayoutParams(
		// FrameLayout.LayoutParams.WRAP_CONTENT,
		// FrameLayout.LayoutParams.WRAP_CONTENT,
		// Gravity.CENTER));
		//
		// new AlertDialog.Builder(PickingActivity.this).setTitle("Numero
		// etiquetas a imprimir")
		// .setView(layout)
		// .setPositiveButton(android.R.string.ok, new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialogInterface, int i) {
		// // do something with picker.getValue()
		// new ConsultaImprimirPedido().execute(String.valueOf(mode),
		// String.valueOf(albaran),
		// codigoPedido.getText().toString(), selItem.getCodigoArticulo(),
		// input.getText().toString(),String.valueOf(picker.getValue()));
		// }
		// })
		// .setNegativeButton(android.R.string.cancel, null)
		// .show();
		//
		// break;
		// case 2:
		// // p//ROSROCA//S21600-B28T-0020//474742//FLEXIBLE
		// // ROSROCA ET16 HGM36 - HG90M36 L-2100 S/PLANO
		// // 1042295_002//50
		//// dialog.dismiss();
		//// final NumberPicker picker = new NumberPicker(PickingActivity.this);
		//// picker.setMinValue(1);
		////
		//// final FrameLayout layout = new FrameLayout(PickingActivity.this);
		//// layout.addView(picker, new FrameLayout.LayoutParams(
		//// FrameLayout.LayoutParams.WRAP_CONTENT,
		//// FrameLayout.LayoutParams.WRAP_CONTENT,
		//// Gravity.CENTER));
		////
		//// new AlertDialog.Builder(PickingActivity.this)
		//// .setView(layout)
		//// .setPositiveButton(android.R.string.ok, new
		// DialogInterface.OnClickListener() {
		//// @Override
		//// public void onClick(DialogInterface dialogInterface, int i) {
		//// // do something with picker.getValue()
		//// new ConsultaImprimirPedido().execute(String.valueOf(mode),
		// String.valueOf(albaran),
		//// codigoPedido.getText().toString(), selItem.getCodigoArticulo(),
		//// input.getText().toString(),String.valueOf(i));
		//// }
		//// })
		//// .setNegativeButton(android.R.string.cancel, null)
		//// .show();
		////
		// break;
		// default:
		// break;
		// }
		// }
		// });
		// alert.show();
		// return true;
		//
		// }
		// });

	}

	private void inicializarCampos() {

		this.total = true;
		pedido = (MapeoPedido) getIntent().getExtras().get("PEDIDO");

		this.codigoPedido = (TextView) findViewById(R.id.codigoPedido);
		this.codigoPedido.setText(String.valueOf(pedido.getPedido()));

		this.referenciaPedido = (TextView) findViewById(R.id.referenciaPedido);
		this.referenciaPedido.setText(" " + pedido.getReferencia());

		this.descripcion = (EditText) findViewById(R.id.Cliente);
		this.descripcion.setText(pedido.getNombreCliente());

	}

	private void homeBtn() {
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

	private void botonAtras() {
		this.backButton = (ImageButton) findViewById(R.id.backButton);
		this.backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

			}
		});

	}

	private void buscarArticulosPedido() {
		aux = new ArrayList<InfoPedidoDetalle>();
		new AsyncTask<Object, Object, Object>() {

			ProgressDialog dialogoEspera = new ProgressDialog(ActividadInformacionEnvioPendiente.this);

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando pedidos Pendientes.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {

					for (MapeoPedidoDetalle p : new MapeoPedidoDetalle().getPedidoDetalle(pedido.getPedido())) {
						Log.d("DEBUG", p.toString());
						aux.add(new InfoPedidoDetalle(p));
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				AdaptadorEnviosPendientesDetalle inf = new AdaptadorEnviosPendientesDetalle(getApplicationContext(),
						aux);
				listaArticulos.setAdapter(inf);
				inf.notifyDataSetChanged();
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}

	private MapeoArticulo obtenerArticulo(final MapeoPedidoDetalle pd) {
		AsyncTask<MapeoArticulo, MapeoArticulo, MapeoArticulo> tarea = new AsyncTask<MapeoArticulo, MapeoArticulo, MapeoArticulo>() {
			MapeoArticulo a = null;
			ArrayList<InfoPedidoDetalle> aux = new ArrayList<InfoPedidoDetalle>();
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadInformacionEnvioPendiente.this);

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando pedidos Pendientes.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			protected void onPostExecute(MapeoArticulo result) {
				AdaptadorEnviosPendientesDetalle inf = new AdaptadorEnviosPendientesDetalle(getApplicationContext(),
						aux);
				listaArticulos.setAdapter(inf);
				inf.notifyDataSetChanged();
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			}

			@Override
			protected MapeoArticulo doInBackground(MapeoArticulo... params) {
				try {

					a = new MapeoArticulo(pd.getArticulo());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return a;
			};

		};
		tarea.execute();
		try {
			return tarea.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	private void marcarArticulo() {

	}
}
