package com.oscasistemas.appgestionlogistica.Recepciones;

import java.util.ArrayList;
import java.util.Locale;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.BusquedaArticulos.ActividadBusquedaArticulos;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.articulo.SendInfo;
import com.oscasistemas.appgestionlogistica.picking.PickingActivity;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import com.oscasistemas.appgestionlogistica.utils.SendInfoAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class ReceivedActivity extends Activity {

	private ImageButton backButton, homeBtn;;
	private TextView title;
	private int MODE, position, nOrder;
	private Spinner ordenSpinner;
	private ArrayList<SendInfo> sendOrders;
	private SendInfoAdapter inf;
	private ListView receivedList;
	private String articleCode;
	private EditText busquedaTexto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendorder_activity);
		this.sendOrders = new ArrayList<SendInfo>();
//		this.setInf(new SendInfoAdapter(ReceivedActivity.this, sendOrders));
		this.position = 0;
		
		
		AuxiliarFunctions.hideKeyboard(ReceivedActivity.this);

		this.MODE = getIntent().getIntExtra("MODE", -1);
		this.nOrder = getIntent().getIntExtra("ORDER", -1);

		this.title = (TextView) findViewById(R.id.sendTitle);
		this.backButton = (ImageButton) findViewById(R.id.backButton);
		this.ordenSpinner = (Spinner) findViewById(R.id.OrdenSpinner);
		if (nOrder != -1)
			this.ordenSpinner.setVisibility(View.INVISIBLE);
		this.receivedList = (ListView) findViewById(R.id.Orders);

		// get ArticleCode
		articleCode = getIntent().getStringExtra("CODE");
		if (articleCode == null) {
			if (this.MODE == 0) {
				this.title.setText("Recibos Pendientes");
				new ConexionRecibosPendientes(ReceivedActivity.this, receivedList, nOrder, position).execute();
			} else {
				this.title.setText("Recibos Recibidos");
				new ConexionRecibosRecibidos(ReceivedActivity.this, receivedList, nOrder, position).execute();
			}
		} else {
			this.title.setText("Recibos Articulo");
			new ConexionArticulosRecibidos(ReceivedActivity.this, receivedList).execute(articleCode);
			this.ordenSpinner.setVisibility(View.GONE);

		}

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
				if (articleCode == null) {
					if (MODE == 0)
						new ConexionRecibosPendientes(ReceivedActivity.this, receivedList, nOrder, position).execute();
					else
						new ConexionRecibosRecibidos(ReceivedActivity.this, receivedList, nOrder, position).execute();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		this.receivedList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				SendInfo selItem = (SendInfo) parent.getAdapter().getItem(position); //
				Intent i = new Intent(ReceivedActivity.this, PickingActivity.class);
				i.putExtra("MODE", 0);
				i.putExtra("CODE", "" + selItem.getPedido());
				i.putExtra("REF", selItem.getReferencia());
				i.putExtra("PERSON", selItem.getCliente_proveedor());
				i.putExtra("ALBARAN", MODE != 0);
				startActivity(i);
			}
		});

		this.receivedList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		this.busquedaTexto = (EditText) findViewById(R.id.busquedaTexto);
		this.busquedaTexto.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if (MODE == 0) {
					// busquedaTexto.getText().toString().toUpperCase(Locale.ENGLISH);
					new ConexionRecibosPendientesBusqueda(ReceivedActivity.this, receivedList, nOrder, position)
							.execute(busquedaTexto.getText().toString().toUpperCase(Locale.ENGLISH));

				} else {
					new ConexionRecibosRecibidosBusqueda(ReceivedActivity.this, receivedList, nOrder, position)
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

	public SendInfoAdapter getInf() {
		return inf;
	}

	public void setInf(SendInfoAdapter inf) {
		this.inf = inf;
	}

}
