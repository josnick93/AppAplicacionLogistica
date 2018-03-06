package com.oscasistemas.appgestionlogistica.BusquedaAlbaranesSalida;

import java.util.ArrayList;


import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.articulo.ArticlePickingInfo;
import com.oscasistemas.appgestionlogistica.informacionArticulo.ActividadInformacionArticulo;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Mode 0 = RECV Mode 1 = SEND
 * 
 * @author Portátil1
 *
 */
public class ActividadInformacionAlbaranSalida extends Activity {

	private TextView codigoPedido, referenciaPedido;
	private ImageButton backButton, homeBtn;
	private ListView articleInfo;
	private EditText descripcion;
	private Button filtrado;
	private boolean total, albaran;
	private int mode;
	private ArrayList<ArticlePickingInfo> articleInfoData;
	private int posicion;

	private ArticlePickingInfo selItem;

	public int getMode() {
		return mode;
	}

	public boolean getalbaran() {
		// TODO Auto-generated method stub
		return albaran;
	}

	public String code() {
		return codigoPedido.getText().toString();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picking_activity);

		AuxiliarFunctions.hideKeyboard(ActividadInformacionAlbaranSalida.this);

		this.mode = getIntent().getExtras().getInt("MODE");
		this.albaran = getIntent().getExtras().getBoolean("ALBARAN", false);
		this.total = true;
		this.articleInfoData = new ArrayList<ArticlePickingInfo>();

		this.codigoPedido = (TextView) findViewById(R.id.codigoPedido);
		this.codigoPedido.setText(getIntent().getExtras().getString("CODE"));

		this.referenciaPedido = (TextView) findViewById(R.id.referenciaPedido);
		this.referenciaPedido.setText(" " + getIntent().getExtras().getString("PERSON"));

		this.descripcion = (EditText) findViewById(R.id.Cliente);
		this.descripcion.setText(getIntent().getExtras().getString("REF"));

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

		this.articleInfo = (ListView) findViewById(R.id.listaArticulosOrden);
		this.articleInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(), ActividadInformacionArticulo.class);
				// send article
				ArticlePickingInfo selItem = (ArticlePickingInfo) parent.getAdapter().getItem(position); //
				if (String.valueOf(selItem.getCodigoArticulo()).length() < 9)
					intent.putExtra("CODE", AuxiliarFunctions.zeroFormat(String.valueOf(selItem.getCodigoArticulo())));
				else
					intent.putExtra("CODE", String.valueOf(selItem.getCodigoArticulo()));

				startActivity(intent);
			}
		});

//		this.articleInfo.setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PickingActivity.this,
//						android.R.layout.select_dialog_item);
//				arrayAdapter.add("	Marcar/Desmarcar");
//				arrayAdapter.add("	Imprimir");
//
//				selItem = (ArticlePickingInfo) parent.getAdapter().getItem(position);
//				AlertDialog.Builder alert = new AlertDialog.Builder(ActividadInformacionAlbaranSalida.this);
//				posicion = position;
//				alert.setTitle("Articulos a enviar:");
//				final EditText input = new EditText(ActividadInformacionAlbaranSalida.this);
//				input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//				input.setRawInputType(Configuration.KEYBOARD_12KEY);
//				input.setText(selItem.getCantidad());
//				alert.setView(input);
//
//				alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//				alert.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// TODO Auto-generated method stub
//						switch (which) {
//						case 0:
//							// if (selItem.getEstado() == (byte) 0 ||
//							// selItem.getEstado() == (byte) 1)
//							// new
//							// ConexionMarcarArticuloSacado(PickingActivity.this,
//							// articleInfo, articleInfoData,
//							// mode, posicion,
//							// albaran).execute(codigoPedido.getText().toString(),
//							// selItem.getCodigoArticulo(),
//							// selItem.getCantidad(),
//							// input.getText().toString());
//							new ConexionMarcarArticuloEnviado(PickingActivity.this, articleInfo, articleInfoData, mode,
//									selItem.getPosicion(), selItem.getRegistro(), albaran).execute(
//											codigoPedido.getText().toString(), selItem.getCodigoArticulo(),
//											selItem.getCantidad(), input.getText().toString());
//							break;
//						case 1:
//							dialog.dismiss();
//							final NumberPicker picker = new NumberPicker(PickingActivity.this);
//							picker.setValue(1);
//							picker.setMinValue(1);
//							picker.setMaxValue(1000);
//
//							final FrameLayout layout = new FrameLayout(PickingActivity.this);
//							layout.addView(picker, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
//									FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
//
//							new AlertDialog.Builder(PickingActivity.this).setTitle("Numero etiquetas a imprimir")
//									.setView(layout)
//									.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//										@Override
//										public void onClick(DialogInterface dialogInterface, int i) {
//											// do something with
//											// picker.getValue()
//											new ConsultaImprimirPedido().execute(String.valueOf(mode),
//													String.valueOf(albaran), codigoPedido.getText().toString(),
//													selItem.getCodigoArticulo(), input.getText().toString(),
//													String.valueOf(picker.getValue()));
//										}
//									}).setNegativeButton(android.R.string.cancel, null).show();
//
//							break;
//						case 2:
//							// p//ROSROCA//S21600-B28T-0020//474742//FLEXIBLE
//							// ROSROCA ET16 HGM36 - HG90M36 L-2100 S/PLANO
//							// 1042295_002//50
//							// dialog.dismiss();
//							// final NumberPicker picker = new
//							// NumberPicker(PickingActivity.this);
//							// picker.setMinValue(1);
//							//
//							// final FrameLayout layout = new
//							// FrameLayout(PickingActivity.this);
//							// layout.addView(picker, new
//							// FrameLayout.LayoutParams(
//							// FrameLayout.LayoutParams.WRAP_CONTENT,
//							// FrameLayout.LayoutParams.WRAP_CONTENT,
//							// Gravity.CENTER));
//							//
//							// new AlertDialog.Builder(PickingActivity.this)
//							// .setView(layout)
//							// .setPositiveButton(android.R.string.ok, new
//							// DialogInterface.OnClickListener() {
//							// @Override
//							// public void onClick(DialogInterface
//							// dialogInterface, int i) {
//							// // do something with picker.getValue()
//							// new
//							// ConsultaImprimirPedido().execute(String.valueOf(mode),
//							// String.valueOf(albaran),
//							// codigoPedido.getText().toString(),
//							// selItem.getCodigoArticulo(),
//							// input.getText().toString(),String.valueOf(i));
//							// }
//							// })
//							// .setNegativeButton(android.R.string.cancel, null)
//							// .show();
//							//
//							break;
//						default:
//							break;
//						}
//					}
//				});
//				alert.show();
//				return true;
//
//			}
//		});

		this.filtrado = (Button) findViewById(R.id.BotonFiltrado);
		this.filtrado.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				total = !total;
				if (total) {
					filtrado.setText("Pendientes");
					// pedidos a enviar
//					new ConsultaPedidos(PickingActivity.this, articleInfoData, articleInfo)
//							.execute(String.valueOf(mode), String.valueOf(albaran), codigoPedido.getText().toString());
				} else {
					filtrado.setText("Total");
//					new ConsultaPedidosPendientes(PickingActivity.this, articleInfoData, articleInfo)
//							.execute(String.valueOf(mode), String.valueOf(albaran), codigoPedido.getText().toString());

				}
			}
		});
	}
}
