package com.oscasistemas.appgestionlogistica.Envios;



import java.util.Locale;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.articulo.SendInfo;
import com.oscasistemas.appgestionlogistica.picking.PickingActivity;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
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

public class ActividadEnviosEnviados extends Activity {
	private ImageButton backButton,homeBtn;;
	private TextView title;
	private int MODE, position, nOrder;
	private Spinner ordenSpinner;
	private ListView sendOrdersList;
	private String articleCode;
	private EditText busquedaTexto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendorder_activity);
		this.position = 0;
		this.MODE = getIntent().getIntExtra("MODE", -1);
		this.nOrder = getIntent().getIntExtra("ORDER", -1);
		Log.d("SCAN", String.valueOf(this.nOrder));
		this.articleCode = getIntent().getStringExtra("CODE");
		this.backButton = (ImageButton) findViewById(R.id.backButton);
		this.ordenSpinner = (Spinner) findViewById(R.id.OrdenSpinner);
		if (nOrder != -1)
			this.ordenSpinner.setVisibility(View.INVISIBLE);
		this.sendOrdersList = (ListView) findViewById(R.id.Orders);
		
		AuxiliarFunctions.hideKeyboard(ActividadEnviosEnviados.this);

		this.title = (TextView) findViewById(R.id.sendTitle);
		if (this.articleCode == null) {
			if (this.MODE == 0) {
				this.title.setText("Envios Pendientes");
				new ConexionEnviosPendientes(ActividadEnviosEnviados.this, sendOrdersList, nOrder, position).execute();
			} else {
				this.title.setText("Envios Enviados");
				new ConexionEnvios(ActividadEnviosEnviados.this, sendOrdersList, nOrder, position).execute();
			}
		} else {
			this.title.setText("Pendiende envio");
			new ConexionArticulosPendientes(ActividadEnviosEnviados.this, sendOrdersList).execute(articleCode);
			this.ordenSpinner.setVisibility(View.GONE);
		}
		this.backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		this.homeBtn= (ImageButton) findViewById(R.id.homeBtn);
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
		

		this.ordenSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.orden_names)));
		this.ordenSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				// TODO Auto-generated method stub
				position = pos;
				if (MODE == 0) {
					new ConexionEnviosPendientes(ActividadEnviosEnviados.this, sendOrdersList, nOrder, position).execute();
				} else {
					new ConexionEnvios(ActividadEnviosEnviados.this, sendOrdersList, nOrder, position).execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		this.sendOrdersList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				SendInfo selItem = (SendInfo) parent.getAdapter().getItem(position); //
				Intent i = new Intent(ActividadEnviosEnviados.this, PickingActivity.class);
				i.putExtra("MODE", 1);
				i.putExtra("CODE", "" + selItem.getPedido());
				i.putExtra("REF", selItem.getReferencia());
				i.putExtra("PERSON", selItem.getCliente_proveedor());
				i.putExtra("ALBARAN", MODE != 0);
				startActivity(i);
			}
		});

		this.sendOrdersList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (MODE == 0) {
					final SendInfo selItem = (SendInfo) parent.getAdapter().getItem(position);
					AlertDialog.Builder alert = new AlertDialog.Builder(ActividadEnviosEnviados.this);
					alert.setTitle("Peso del pedido (KG)");
					final EditText input = new EditText(ActividadEnviosEnviados.this);
					input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
					input.setRawInputType(Configuration.KEYBOARD_12KEY);
					alert.setView(input);
					alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// Put actions for OK button here
							new ConexionSelecionarPeso().execute(selItem.getPedido(), input.getText().toString());
						}
					});
					alert.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// Put actions for CANCEL button here, or leave in
						}
					});
					alert.show();
					return true;
				} else {
					return false;
				}
			}
		});

		this.busquedaTexto = (EditText) findViewById(R.id.busquedaTexto);
		this.busquedaTexto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (MODE == 0) {
					new ConexionEnviosPendientesBusqueda(ActividadEnviosEnviados.this, sendOrdersList, nOrder, position)
							.execute(busquedaTexto.getText().toString().toUpperCase(Locale.ENGLISH));
					//
				} else {
					new ConexionEnviosBusqueda(ActividadEnviosEnviados.this, sendOrdersList, nOrder, position)
					.execute(busquedaTexto.getText().toString().toUpperCase(Locale.ENGLISH));

				}

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
}
