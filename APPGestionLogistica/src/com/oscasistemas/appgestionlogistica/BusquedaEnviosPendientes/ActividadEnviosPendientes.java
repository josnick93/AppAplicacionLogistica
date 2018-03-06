package com.oscasistemas.appgestionlogistica.BusquedaEnviosPendientes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import com.MapeoBD.Pedidos.MapeoPedido;
import com.MapeoBD.Pedidos.OrdenacionPedidoSalida;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.Envios.ConexionSelecionarPeso;
import com.oscasistemas.appgestionlogistica.InformacionEnviosPendientes.ActividadInformacionEnvioPendiente;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.articulo.SendInfo;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import com.oscasistemas.appgestionlogistica.utils.SendInfoAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class ActividadEnviosPendientes extends Activity {
	private ImageButton backButton, homeBtn;;
	private TextView title;
	private Spinner ordenSpinner;
	private ListView sendOrdersList;
	private EditText busquedaTexto;

	private OrdenacionPedidoSalida ordenacion;
	private final int limite = 50;
	private Timer timer = new Timer(); // timer busqueda texto
	private final long DELAY = 500; // milliseconds

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendorder_activity);

		AuxiliarFunctions.hideKeyboard(ActividadEnviosPendientes.this);

		titulo();
		botonAtras();
		homeBtn();
		busquedaEnvios();
		spinnerOrdenacion();
		listaEnvios();
		this.ordenacion = OrdenacionPedidoSalida.FECHADESC;
		buscarPedidos(busquedaTexto.getText().toString());
	}

	private void buscarPedidos(final String text) {
		new AsyncTask<Object, Object, Object>() {
			ArrayList<MapeoPedido> aux = null;
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadEnviosPendientes.this);

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando pedidos Pendientes.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {

					// crear la informacion de la orden detalle
					if (!AuxiliarFunctions.isNumeric(text.toUpperCase()))
						aux = new MapeoPedido().getPedidos(text.toUpperCase(), ordenacion, limite);
					else
						aux = new MapeoPedido().getPedidos(Integer.valueOf(text.toUpperCase()), ordenacion, limite);

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				AdaptadorEnviosPendientes inf = new AdaptadorEnviosPendientes(getApplicationContext(), aux);
				sendOrdersList.setAdapter(inf);
				inf.notifyDataSetChanged();
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();

	}

	private void busquedaEnvios() {
		this.busquedaTexto = (EditText) findViewById(R.id.busquedaTexto);
		this.busquedaTexto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				timer.cancel();
				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						new Handler(Looper.getMainLooper()).post(new Runnable() {
							@Override
							public void run() {
								buscarPedidos(busquedaTexto.getText().toString());
							}

						});
					}
				}, DELAY);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}

		});

	}

	private void listaEnvios() {
		this.sendOrdersList = (ListView) findViewById(R.id.Orders);
		this.sendOrdersList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				MapeoPedido mapeoPedido = (MapeoPedido) parent.getAdapter().getItem(position); //
				Intent i = new Intent(ActividadEnviosPendientes.this, ActividadInformacionEnvioPendiente.class);
				i.putExtra("PEDIDO", mapeoPedido);
				startActivity(i);
			}
		});

		this.sendOrdersList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				final MapeoPedido selItem = (MapeoPedido) parent.getAdapter().getItem(position);
				AlertDialog.Builder alert = new AlertDialog.Builder(ActividadEnviosPendientes.this);
				alert.setTitle("Peso del pedido (KG)");
				final EditText input = new EditText(ActividadEnviosPendientes.this);
				input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
				input.setRawInputType(Configuration.KEYBOARD_12KEY);
				alert.setView(input);
				alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Put actions for OK button here
						//new ConexionSelecionarPeso().execute(selItem.getPedido(), input.getText().toString());
					}
				});
				alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
				alert.show();
				return true;

			}
		});

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

	private void titulo() {
		this.title = (TextView) findViewById(R.id.sendTitle);
		this.title.setText("Envios Pendientes");

	}

	private void spinnerOrdenacion() {
		this.ordenSpinner = (Spinner) findViewById(R.id.OrdenSpinner);
		this.ordenSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.orden_names)));
		this.ordenSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// TODO Auto-generated method stub
				switch (pos) {
				case 0:
					ordenacion = OrdenacionPedidoSalida.FECHADESC;
					break;
				case 1:
					ordenacion = OrdenacionPedidoSalida.FECHASASC;
					break;
				case 2:
					ordenacion = OrdenacionPedidoSalida.CLIENTEASC;
					break;
				case 3:
					ordenacion = OrdenacionPedidoSalida.CLIENTEDESC;
					break;
				case 4:
					ordenacion = OrdenacionPedidoSalida.IDASC;
					break;
				case 5:
					ordenacion = OrdenacionPedidoSalida.IDDESC;
					break;
				case 6:
					ordenacion = OrdenacionPedidoSalida.FECHADESC;
					break;

				default:
					break;
				}
				buscarPedidos(busquedaTexto.getText().toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void botonAtras() {
		this.backButton = (ImageButton) findViewById(R.id.backButton);
		this.backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				finish();
			}
		});

	}
}
