package com.oscasistemas.appgestionlogistica.Expediciones;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

public class CreateExpeditionActivity extends Activity {

	private Button crearExpedicion;
	private ImageButton backButton, homeBtn;

	// consultas
	private ConsultaClientes obtenerClientes;
	private ConsultaDireccionCliente obtenerDireccionCliente;
	private ConsultaTransportista obtenerTransportista;
	private String alfabeto = "";
	private String alfabetoTrasportista = "";

	private int idCliente = -1;
	private int idDireccion = -1;
	private int idtransportista = -1;
	private int idPortes = -1;
	private int idtipoPorte = -1;

	private Spinner ClienteSpinner, ClienteAlfabetoSpinner, DireccionSpinner, TransSpinner, PortesSpinner, TipoSpinner,
			TrasnporsistaAlfabetoSpinner;
	private EditText ReferenciaEdit, PesoEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expedition_create_activity);

		/**
		 * Referencias
		 */

		this.ClienteSpinner = (Spinner) findViewById(R.id.ClienteSpinner);
		this.ClienteAlfabetoSpinner = (Spinner) findViewById(R.id.ClienteAlfabetoSpinner);
		this.DireccionSpinner = (Spinner) findViewById(R.id.DireccionSpinner);
		this.TransSpinner = (Spinner) findViewById(R.id.TransSpinner);
		this.PortesSpinner = (Spinner) findViewById(R.id.PortesSpinner);
		this.TipoSpinner = (Spinner) findViewById(R.id.TipoSpinner);
		this.TrasnporsistaAlfabetoSpinner = (Spinner) findViewById(R.id.TrasnporsistaAlfabetoSpinner);

		this.ReferenciaEdit = (EditText) findViewById(R.id.ReferenciaEdit);
		this.PesoEdit = (EditText) findViewById(R.id.PesoEdit);

		this.crearExpedicion = (Button) findViewById(R.id.CrearExpedicionBtn);
		this.backButton = (ImageButton) findViewById(R.id.backButton);

		/**
		 * Conexiones
		 */

		/**
		 * Eventos
		 */

		this.ClienteSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String cliente = (String) parent.getAdapter().getItem(position);
				obtenerDireccionCliente = new ConsultaDireccionCliente(CreateExpeditionActivity.this, DireccionSpinner);
				idCliente = obtenerClientes.getClientes().get(cliente);
				obtenerDireccionCliente.execute(String.valueOf(idCliente));

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		this.ClienteAlfabetoSpinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Alfabeto)));
		this.ClienteAlfabetoSpinner.setSelection(0);
		this.ClienteAlfabetoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				alfabeto = (String) parent.getAdapter().getItem(position);
				obtenerClientes = new ConsultaClientes(CreateExpeditionActivity.this, ClienteSpinner);
				obtenerClientes.execute(alfabeto);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		this.TrasnporsistaAlfabetoSpinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.Alfabeto)));
		this.TrasnporsistaAlfabetoSpinner.setSelection(0);
		this.TrasnporsistaAlfabetoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				alfabetoTrasportista = (String) parent.getAdapter().getItem(position);
				obtenerTransportista = new ConsultaTransportista(CreateExpeditionActivity.this, TransSpinner);
				obtenerTransportista.execute(alfabetoTrasportista);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		this.DireccionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			/*
			 * Añadir direccion
			 */
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String direccion = (String) parent.getAdapter().getItem(position);
				idDireccion = obtenerDireccionCliente.getDirecciones().get(direccion);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		this.TransSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				String transportista = (String) parent.getAdapter().getItem(position);
				idtransportista = obtenerTransportista.gettrasportista().get(transportista);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		this.PortesSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.Portes)));
		this.PortesSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				idPortes = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		this.TipoSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.Tipo_portes)));
		this.TipoSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				idtipoPorte = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		this.ReferenciaEdit.addTextChangedListener(new TextWatcher() {

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
				// TODO Auto-generated method stub

			}
		});

		this.PesoEdit.addTextChangedListener(new TextWatcher() {

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
				// TODO Auto-generated method stub

			}
		});

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
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finishAffinity();
				startActivity(intent);

			}
		});

		this.crearExpedicion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CrearExpedicion();
				// TODO Auto-generated method stub
				// Intent i = new Intent(getBaseContext(), MenuActivity.class);
				// finish();
				// startActivity(i);
			}
		});

	}

	private void CrearExpedicion() {
		boolean error = false;
		String message = "";
		if (idCliente == -1) {
			error = true;
			message += "-Debes Selecionar un cliente\n";
		}
		if (idDireccion == -1) {
			error = true;
			message += "-Debes Selecionar una direccion\n";
		}
		if (idtransportista == -1) {
			error = true;
			message += "-Debes Selecionar un transportista\n";
		}
		if (idPortes == -1) {
			error = true;
			message += "-Debes Selecionar un porte\n";
		}
		if (idtipoPorte == -1) {
			error = true;
			message += "-Debes Selecionar un tipo de porte\n";
		}
		if (PesoEdit.getText().toString().trim().length() == 0) {
			error = true;
			message += "-Debes Selecionar un peso\n";
		}

		// comprobacion
		if (error) {
			Builder builder = new Builder(CreateExpeditionActivity.this);
			builder.setMessage(message);
			builder.setPositiveButton("ok", null);
			builder.create().show();
			// crear expedicion
		} else {
			/**
			 * Argumentos params 0 ... => TipoExpedicion, EstadoExpedicion,
			 * Cliente, Domicilio, Alias, Referencia, Fecha, FechaRecogida,
			 * iUser, Transportista, Portes, PortesPagados, Agente, Precio,
			 * Descuento, Bultos, Peso, Observaciones, ts
			 */
			new ConsultaCrearExpedicion(this).execute(String.valueOf(idCliente), String.valueOf(idDireccion), ReferenciaEdit.getText().toString(),
					String.valueOf(idtransportista), String.valueOf(idPortes), String.valueOf(idtipoPorte), PesoEdit.getText().toString());
			
			
		}
	}

}
