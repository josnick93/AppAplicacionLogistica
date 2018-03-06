package com.oscasistemas.appgestionlogistica.Expediciones;

import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion.AdaptadorComponentesOrdenProd;
import com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion.InformacionComponenteOrdenProd;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.picking.PickingActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class InformacionExpedicionActivity extends Activity {

	private String expedicion;
	private ListView listaAlbaranesExpedicion;
	private boolean estadoBoton;

	private ImageButton backButton, homeBtn;
	private Button asignarbtn;
	private ArrayList<InformacionComponenteOrdenProd> albaranes;

	private TextView codigoExpedicion, referenciaExpedicion, clienteExpedicion;

	@TargetApi(23)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expedicion_info_activity);
		this.expedicion = getIntent().getExtras().getString("CODE");
		estadoBoton = false;
		this.listaAlbaranesExpedicion = (ListView) findViewById(R.id.listaAlbaranesExpedicion);

		this.codigoExpedicion = (TextView) findViewById(R.id.ExpeditionCode);
		this.referenciaExpedicion = (TextView) findViewById(R.id.ExpedicionReferencia);
		this.clienteExpedicion = (TextView) findViewById(R.id.ClienteExpedicion);

		this.clienteExpedicion.setText(getIntent().getExtras().getString("CLIENTE"));
		this.referenciaExpedicion.setText(getIntent().getExtras().getString("REF"));
		this.codigoExpedicion.setText(this.expedicion);

		ConsultaExpedicionDetalle d = new ConsultaExpedicionDetalle(this, listaAlbaranesExpedicion);
		d.execute(expedicion);
		this.albaranes = d.getAlbaranes();

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
				startActivity(intent);
				finishAffinity();

			}
		});

		this.asignarbtn = (Button) findViewById(R.id.Asignarbtn);
		this.asignarbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				estadoBoton = !estadoBoton;
				if (estadoBoton) {

					asignarbtn.setBackgroundColor(Color.GREEN);
					new ConsultaAlbaranesCliente(InformacionExpedicionActivity.this, listaAlbaranesExpedicion,
							albaranes).execute(getIntent().getExtras().getString("CLIENTEID"));
				} else {
					ConsultaExpedicionDetalle d = new ConsultaExpedicionDetalle(InformacionExpedicionActivity.this,
							listaAlbaranesExpedicion);
					d.execute(expedicion);
					albaranes = d.getAlbaranes();
					asignarbtn.setBackgroundColor(Color.LTGRAY);
				}
			}
		});

		this.listaAlbaranesExpedicion.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

				if (estadoBoton) {
					InformacionComponenteOrdenProd selItem = (InformacionComponenteOrdenProd) parent.getAdapter().getItem(position);
					new ConsultaAsignarAlbaranExpedicion().execute(expedicion,
							selItem.getTitle());

					ArrayList<InformacionComponenteOrdenProd> albaranes = new ArrayList<InformacionComponenteOrdenProd>();
					for (int i = 0; i < parent.getAdapter().getCount(); i++) {
						if (i != position)
							albaranes.add((InformacionComponenteOrdenProd) parent.getAdapter().getItem(i));
					}
					AdaptadorComponentesOrdenProd adp = new AdaptadorComponentesOrdenProd(getApplicationContext(), albaranes);
					listaAlbaranesExpedicion.setAdapter(adp);
					adp.notifyDataSetChanged();

				} else {
					InformacionComponenteOrdenProd selItem = (InformacionComponenteOrdenProd) parent.getAdapter().getItem(position);
					Intent i = new Intent(InformacionExpedicionActivity.this, PickingActivity.class);
					i.putExtra("MODE", 1);
					i.putExtra("CODE", "" + selItem.getTitle());
//					i.putExtra("REF", selItem.getAlias());
					i.putExtra("PERSON", getIntent().getExtras().getString("CLIENTE"));
					i.putExtra("ALBARAN", true);
					startActivity(i);
				}

			}
		});

	}

}
