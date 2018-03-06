package com.oscasistemas.appgestionlogistica.MenuEnvios;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.BusquedaAlbaranesSalida.ActividadAlbaranesSalida;
import com.oscasistemas.appgestionlogistica.BusquedaEnviosPendientes.ActividadEnviosPendientes;
import com.oscasistemas.appgestionlogistica.Expediciones.ExpedicionesActivity;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class ActividadMenuEnvios extends Activity {

	private ImageButton backButton;
	private Button enviosPendientesBtn, albaranesSalidaBtn, expedicionesBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendmenu_activity);

		botonAtras();
		botonEnviosPendientes();
		botonAlbaranesSalida();
		botonExpediciones();

	}

	private void botonExpediciones() {
		this.expedicionesBtn = (Button) findViewById(R.id.ExpeditionButton);
		this.expedicionesBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), ExpedicionesActivity.class);
				// PENDMODE
				startActivity(i);

			}
		});

	}

	private void botonAlbaranesSalida() {
		this.albaranesSalidaBtn = (Button) findViewById(R.id.sendSendOrders);
		this.albaranesSalidaBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), ActividadAlbaranesSalida.class);
				// PENDMODE
				startActivity(i);

			}
		});

	}

	private void botonEnviosPendientes() {
		this.enviosPendientesBtn = (Button) findViewById(R.id.pendSendOrder);
		this.enviosPendientesBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), ActividadEnviosPendientes.class);
				// PENDMODE
				startActivity(i);

			}
		});

	}

	private void botonAtras() {
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

}
