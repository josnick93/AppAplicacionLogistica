package com.oscasistemas.appgestionlogistica.Menu;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.BusquedaArticulos.ActividadBusquedaArticulos;
import com.oscasistemas.appgestionlogistica.BusquedaOrdenesProduccion.ActividadBusquedaOrdenesProduccion;
import com.oscasistemas.appgestionlogistica.LocalizacionArticulos.ActividadLocalizacionArticulos;
import com.oscasistemas.appgestionlogistica.MenuEnvios.ActividadMenuEnvios;
import com.oscasistemas.appgestionlogistica.R.id;
import com.oscasistemas.appgestionlogistica.R.layout;
import com.oscasistemas.appgestionlogistica.Recepciones.ReceivedMenuActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuActivity extends Activity{
	private Button articleButton,locationButton,
		sendButton,receivedButton,productionButton;
	private ImageButton exitButton;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_activity);
		
		this.exitButton=(ImageButton) findViewById(R.id.exitButton);
		this.exitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishAffinity();
                System.exit(1);
			}
		});
		
		this.articleButton=(Button) findViewById(R.id.ArticleButton);
		this.articleButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), ActividadBusquedaArticulos.class);                      
				//i.putExtra("User", getIntent().getSerializableExtra("User"));
				finish(); 
				startActivity(i);
				
			}
		});
		
		this.locationButton=(Button) findViewById(R.id.LocationButton);
		this.locationButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), ActividadLocalizacionArticulos.class);                      
				//i.putExtra("User", getIntent().getSerializableExtra("User"));
				finish(); 
				startActivity(i);
				
			}
		});
		
		this.sendButton=(Button) findViewById(R.id.SendButton);
		this.sendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), ActividadMenuEnvios.class);                      
				//i.putExtra("User", getIntent().getSerializableExtra("User"));
				finish(); 
				startActivity(i);
				
			}
		});
		
		this.receivedButton=(Button) findViewById(R.id.ReceiveButton);
		this.receivedButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), ReceivedMenuActivity.class);                      
				//i.putExtra("User", getIntent().getSerializableExtra("User"));
				finish(); 
				startActivity(i);
				
			}
		});
		
		this.productionButton=(Button) findViewById(R.id.PrductionButton);
		this.productionButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), ActividadBusquedaOrdenesProduccion.class);                      
				//i.putExtra("User", getIntent().getSerializableExtra("User"));
				finish(); 
				startActivity(i);
				
			}
		});
		
	}

}
