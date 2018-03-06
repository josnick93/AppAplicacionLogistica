package com.oscasistemas.appgestionlogistica;

import java.util.Locale;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.loggin.MapeoUsuario;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	private Button loginButton;
	private ImageButton exitButton;
	private EditText userTextFiled, passTextField;
	private SharedPreferences logginSession;
	public static final String MyPREFERENCES = "MyPrefs";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.logginSession = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

		this.userTextFiled = (EditText) findViewById(R.id.UserField);
		this.userTextFiled.setText(logginSession.getString("LOGGIN", ""));

		this.passTextField = (EditText) findViewById(R.id.PasswordField);
		this.passTextField.setText(logginSession.getString("PASSWORD", ""));

		this.loginButton = (Button) findViewById(R.id.LogginButton);
		this.loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new IniciarSesion(MainActivity.this).execute(
						userTextFiled.getText().toString().toUpperCase(Locale.ENGLISH),
						passTextField.getText().toString().toUpperCase(Locale.ENGLISH));
			}
		});

		this.exitButton = (ImageButton) findViewById(R.id.exitButton);
		this.exitButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishAffinity();

			}
		});

	}

	public void writelogginSession(MapeoUsuario s) {
		SharedPreferences.Editor editor = logginSession.edit();
		editor.putString("LOGGIN", s.getName());
		editor.putString("PASSWORD", s.getPass());
		editor.commit();
	}
}
