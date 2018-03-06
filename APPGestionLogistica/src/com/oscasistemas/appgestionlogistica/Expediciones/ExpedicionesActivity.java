package com.oscasistemas.appgestionlogistica.Expediciones;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.BusquedaArticulos.ActividadBusquedaArticulos;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class ExpedicionesActivity extends Activity {

	private Button crearExpedicion;
	private ImageButton backButton, homeBtn;
	private ListView expediciones;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expedicion_activity);
		
		AuxiliarFunctions.hideKeyboard(ExpedicionesActivity.this);

		this.backButton = (ImageButton) findViewById(R.id.backButton);
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

		this.crearExpedicion = (Button) findViewById(R.id.crearExpedicionBtn);
		this.crearExpedicion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), CreateExpeditionActivity.class);
				finish();
				startActivity(i);
			}
		});

		this.expediciones = (ListView) findViewById(R.id.Expediciones);
		new ConsultaExpediciones(this, this.expediciones).execute();
		this.expediciones.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				/*
				 * Mostrar expedicion
				 */
				InformacionExpedicion selItem = (InformacionExpedicion) parent.getAdapter().getItem(position); //
				Intent i = new Intent(ExpedicionesActivity.this, InformacionExpedicionActivity.class);
				i.putExtra("CODE", selItem.getExpedicion());
				i.putExtra("CLIENTEID", selItem.getIdcliente());
				i.putExtra("CLIENTE", selItem.getCliente());
				i.putExtra("REF", selItem.getReferencia());
				startActivity(i);

			}
		});

	}

}
