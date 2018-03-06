package com.oscasistemas.appgestionlogistica.Recepciones;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class ReceivedMenuActivity extends Activity {
	private ImageButton backButton;
	private Button pendButton,receivedButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pendorder_menu_activity);

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
		
		this.pendButton=(Button) findViewById(R.id.PendButton);
		this.pendButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), ReceivedActivity.class);
				//PENDMODE
				i.putExtra("MODE", 0);
				startActivity(i);
				
			}
		});
		
		this.receivedButton=(Button) findViewById(R.id.ReceivedButton);
		this.receivedButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getBaseContext(), ReceivedActivity.class);
				//PENDMODE
				i.putExtra("MODE", 1);
				startActivity(i);
				
			}
		});

	}
}
