package com.oscasistemas.appgestionlogistica.Produccion;


import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion.InformacionComponenteOrdenProd;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.informacionArticulo.ActividadInformacionArticulo;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ProductionDispActivity extends Activity {

	private ImageButton backButton, homeBtn;
	private ArrayList<InformacionComponenteOrdenProd> components;
	private ListView componentsList;
	private int position;
	private Button dispButton;
	private TextView nOrdenProd;
	private EditText cantidad;
	private AutoResizeTextView ref, client;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.production_info_activity);
		AuxiliarFunctions.hideKeyboard(this);
		this.components = (ArrayList<InformacionComponenteOrdenProd>) getIntent().getExtras().getSerializable("COMPONENTS");
		components.remove(0);
		components.remove(0);
		
		AuxiliarFunctions.hideKeyboard(ProductionDispActivity.this);

		this.nOrdenProd = (TextView) findViewById(R.id.ArticleCode);
		this.ref = (AutoResizeTextView) findViewById(R.id.ArticleAlias);
		this.client = (AutoResizeTextView) findViewById(R.id.ClientText);
		this.cantidad = (EditText) findViewById(R.id.avaiableArticles);

		this.dispButton = (Button) findViewById(R.id.dispButton);
		this.dispButton.setVisibility(View.INVISIBLE);

		this.nOrdenProd.setText(String.valueOf(getIntent().getExtras().getString("PRODORDER")));
		this.ref.setText(getIntent().getExtras().getString("REF"));
		this.client.setText(getIntent().getExtras().getString("CLIENT"));
		this.cantidad.setText(getIntent().getExtras().getString("CANTIDAD"));

		this.backButton = (ImageButton) findViewById(R.id.backButton);
		this.componentsList = (ListView) findViewById(R.id.infoArticleList);

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
				finishAffinity();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

			}
		});

		new ConexionArticulosOrden(ProductionDispActivity.this, componentsList)
				.execute(nOrdenProd.getText().toString());

		this.componentsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				InformacionComponenteOrdenProd selItem = (InformacionComponenteOrdenProd) parent.getAdapter().getItem(position);
				if (AuxiliarFunctions.isNumeric(selItem.getTitle())) {
					int code = Integer.valueOf(selItem.getTitle());
					if (code != -1) {
						Intent intent = new Intent(getBaseContext(), ActividadInformacionArticulo.class);
						if (String.valueOf(code).length() < 9)
							intent.putExtra("CODE", String.format("%0" + (9 - String.valueOf(code).length()) + "d%s", 0,
									String.valueOf(code)));
						else
							intent.putExtra("CODE", String.valueOf(code));
						startActivity(intent);
					}
				}

			}
		});

		this.componentsList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int pos, long id) {

				final InformacionComponenteOrdenProd selItem = (InformacionComponenteOrdenProd) parent.getAdapter().getItem(pos);
//				if (selItem.getColor() == Color.WHITE || selItem.getColor() == Color.GREEN ) {
//
//					position = pos;
//					AlertDialog.Builder builder = new AlertDialog.Builder(ProductionDispActivity.this);
//					builder.setTitle("Marcar/Desmarcar articulo:");
//					final EditText input = new EditText(ProductionDispActivity.this);
//					input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//					input.setRawInputType(Configuration.KEYBOARD_12KEY);
//					input.setText(selItem.getQuality());
//					builder.setView(input);
//					builder.setMessage(" " + AuxiliarFunctions.format(selItem.getTitle(), "xx.xxx.xxxx"));
//					builder.setPositiveButton("Sacar", new android.content.DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							// new
//							// connectMarkItem().execute(selItem.getTitle());
//
//							new ConexionMarcarArticuloOrdenSacado(ProductionDispActivity.this, componentsList, position)
//									.execute(nOrdenProd.getText().toString(), selItem.getTitle(), selItem.getStock(),
//											input.getText().toString());
//
//							new ConexionArticulosOrden(ProductionDispActivity.this, componentsList)
//									.execute(nOrdenProd.getText().toString());
//
//						}
//
//					});
//					builder.setNeutralButton("Cancelar", null);
//					builder.setNegativeButton("Marcar/Desm", new android.content.DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							// new
//							// connectMarkItem().execute(selItem.getTitle());
//							new ConexionMarcarArticuloOrdenEnviado(ProductionDispActivity.this, componentsList,
//									selItem.getPosicion()).execute(nOrdenProd.getText().toString(), selItem.getTitle(),
//											selItem.getQuality(), input.getText().toString());
//							new ConexionArticulosOrden(ProductionDispActivity.this, componentsList)
//									.execute(nOrdenProd.getText().toString());
//
//						}
//					});
//
//					builder.create().show();
//					return true;
//				} else
//					return true;
//			}
				return false;
			}
		});

	}

}
