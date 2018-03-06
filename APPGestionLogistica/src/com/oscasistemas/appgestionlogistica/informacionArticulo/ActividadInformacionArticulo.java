package com.oscasistemas.appgestionlogistica.informacionArticulo;

import java.sql.SQLException;
import java.util.ArrayList;
import com.MapeoBD.Articulo.MapeoArticulo;
import com.MapeoBD.Articulo.MapeoDespiece;
import com.MapeoBD.Cliente.MapeoArticuloCliente;
import com.MapeoBD.Localizacion.MapeoLocalizacion;
import com.MapeoBD.Ordenes.MapeoOrden;
import com.MapeoBD.Ordenes.MapeoOrdenDetalle;
import com.MapeoBD.Pedidos.MapeoPedidoDetalle;
import com.MapeoBD.Pedidos.MapeoPedidoProveedorDetalle;
import com.MapeoBD.Proveedor.MapeoArticuloProveedor;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion.ActividadInformacionOrdenProd;
import com.oscasistemas.appgestionlogistica.Menu.MenuActivity;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ActividadInformacionArticulo extends Activity {

	private MapeoArticulo articulo;
	private TextView articleCodeText, articleAliasText, articleDescText, totalArticleText, avaiableArticlesText;
	private ImageButton backButton, homeBtn;
	private Button pendSendButton, pendRecButton;
	private ListView articleInfo;
	private AdaptadorInformacionArticulo loc;
	private boolean pendientesPulsado; // indica si se ha pulsado el boton de
										// pendientes

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pendientesPulsado = false;
		setContentView(R.layout.article_activity);
		articulo = (MapeoArticulo) getIntent().getExtras().getSerializable("Articulo");
		AuxiliarFunctions.hideKeyboard(ActividadInformacionArticulo.this);

		inicializarBotonHome();
		inicializarBotonAtras();
		inicializarPedidosPendientes();
		inicializarEnviosPendientes();
		inicializarInformacionArticulo();
		obtenerInformacionArticulo();
	}

	private void obtenerInformacionArticulo() {
		/**
		 * Informacion general
		 */
		articleCodeText.setText(AuxiliarFunctions.format(articulo.getArticulo(), "xx.xxx.xxxx"));
		articleAliasText.setText(articulo.getAlias());
		articleDescText.setText(articulo.getDescripcion());
		totalArticleText.setText(String.valueOf(articulo.getStock()));
		
		avaiableArticlesText.setText(String.valueOf(articulo.getStock() - articulo.getPendienteServir()));
		new AsyncTask<Object, Object, Object>() {
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadInformacionArticulo.this);
			ArrayList<InformacionArticulo> articleInfoData = new ArrayList<InformacionArticulo>();

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando Informacion Articulo.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					/* Localizacion Articulo */
					ArrayList<MapeoLocalizacion> aux = new MapeoLocalizacion()
							.getLocalizacionArticulo(articulo.getArticulo());
					if (!aux.isEmpty()) {
						articleInfoData.add(new InformacionArticulo("Localizacion"));
						for (MapeoLocalizacion mloc : aux)
							articleInfoData.add(new InformacionArticulo(mloc));
					}

					/* Clientes */
					ArrayList<MapeoArticuloCliente> aux1 = new MapeoArticuloCliente()
							.getClientesArticulo(articulo.getArticulo());
					if (!aux1.isEmpty()) {
						articleInfoData.add(new InformacionArticulo("Clientes"));
						for (MapeoArticuloCliente mloc : aux1)
							articleInfoData.add(new InformacionArticulo(mloc));
					}

					/* Proveedores */
					ArrayList<MapeoArticuloProveedor> aux2 = new MapeoArticuloProveedor()
							.getProveedoresArticulo(articulo.getArticulo());
					if (!aux2.isEmpty()) {
						articleInfoData.add(new InformacionArticulo("Proveedores"));
						for (MapeoArticuloProveedor mloc : aux2)
							articleInfoData.add(new InformacionArticulo(mloc));
					}

					/* Despiece */
					ArrayList<MapeoDespiece> aux3 = new MapeoDespiece().getDespieceArticulo(articulo.getArticulo());
					if (!aux3.isEmpty()) {
						articleInfoData.add(new InformacionArticulo("Despiece"));
						for (MapeoDespiece mloc : aux3)
							articleInfoData.add(new InformacionArticulo(mloc));
					}

					/* Ordenes */
					ArrayList<MapeoOrdenDetalle> aux4 = new MapeoOrdenDetalle()
							.getOrdenesArticulo(articulo.getArticulo());
					if (!aux4.isEmpty()) {
						articleInfoData.add(new InformacionArticulo("Orden"));
						for (MapeoOrdenDetalle mloc : aux4)
							articleInfoData.add(new InformacionArticulo(mloc));
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				loc = new AdaptadorInformacionArticulo(ActividadInformacionArticulo.this, articleInfoData);
				articleInfo.setAdapter(loc);
				loc.notifyDataSetChanged();
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}

	private void inicializarBotonHome() {
		this.homeBtn = (ImageButton) findViewById(R.id.homeBtn);
		this.homeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				finishAffinity();
				startActivity(intent);

			}
		});

	}

	private void inicializarBotonAtras() {

		this.backButton = (ImageButton) findViewById(R.id.backButton);
		this.backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pendientesPulsado) {
					pendRecButton.setVisibility(View.VISIBLE);
					pendSendButton.setVisibility(View.VISIBLE);
					obtenerInformacionArticulo();
					pendientesPulsado = false;
				} else
					finish();
			}
		});
	}

	private void inicializarPedidosPendientes() {
		/**
		 * Ver en pedidos pendientes
		 */
		this.pendRecButton = (Button) findViewById(R.id.pendRecv);
		this.pendRecButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pendRecButton.setVisibility(View.INVISIBLE);
				pendSendButton.setVisibility(View.INVISIBLE);
				pendientesPulsado = true;
				mostrarPedidosClientes();
			}
		});
	}

	private void mostrarPedidosClientes() {
		new AsyncTask<Object, Object, Object>() {
			ArrayList<InformacionArticulo> articleInfoData = new ArrayList<InformacionArticulo>();
			private ProgressDialog dialogoEspera = new ProgressDialog(ActividadInformacionArticulo.this);

			protected void onPreExecute() {
				dialogoEspera.setMessage("Obteniendo pedientes Servir.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					/**
					 * Mostrar pedidos proveedor
					 */
					ArrayList<MapeoPedidoProveedorDetalle> aux = new MapeoPedidoProveedorDetalle()
							.getPedidosArticulo(articulo.getArticulo());
					if (!aux.isEmpty()) {
						articleInfoData.add(new InformacionArticulo("Pendientes Recibir"));
						for (MapeoPedidoProveedorDetalle mloc : aux)
							articleInfoData.add(new InformacionArticulo(mloc));
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				loc = new AdaptadorInformacionArticulo(ActividadInformacionArticulo.this, articleInfoData);
				articleInfo.setAdapter(loc);
				loc.notifyDataSetChanged();
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();
	}

	private void inicializarEnviosPendientes() {
		/**
		 * Ver en pedidos recibidos
		 */
		this.pendSendButton = (Button) findViewById(R.id.pendSend);
		this.pendSendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pendRecButton.setVisibility(View.INVISIBLE);
				pendSendButton.setVisibility(View.INVISIBLE);
				pendientesPulsado = true;
				mostrarPedidosProveedor();
			}
		});
	}

	private void mostrarPedidosProveedor() {
		new AsyncTask<Object, Object, Object>() {
			ProgressDialog dialogoEspera = new ProgressDialog(ActividadInformacionArticulo.this);
			ArrayList<InformacionArticulo> articleInfoData = new ArrayList<InformacionArticulo>();

			protected void onPreExecute() {
				dialogoEspera.setMessage("Cargando Informacion Articulo.");
				dialogoEspera.setCancelable(false);
				dialogoEspera.show();
			};

			@Override
			protected Object doInBackground(Object... params) {
				try {
					/**
					 * Mostrar pedidos proveedor
					 */
					ArrayList<MapeoPedidoDetalle> aux = new MapeoPedidoDetalle()
							.getPedidosArticulo(articulo.getArticulo());
					if (!aux.isEmpty()) {
						articleInfoData.add(new InformacionArticulo("Pendientes Servir"));
						for (MapeoPedidoDetalle mloc : aux)
							articleInfoData.add(new InformacionArticulo(mloc));
					}

					/**
					 * Mostrar pedidos proveedor
					 */
					ArrayList<MapeoOrdenDetalle> aux1 = new MapeoOrdenDetalle()
							.getOrdenesArticulo(articulo.getArticulo());
					if (!aux1.isEmpty()) {
						articleInfoData.add(new InformacionArticulo("Ordenes produccion"));
						for (MapeoOrdenDetalle mloc : aux1)
							articleInfoData.add(new InformacionArticulo(mloc));
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Object result) {
				loc = new AdaptadorInformacionArticulo(ActividadInformacionArticulo.this, articleInfoData);
				articleInfo.setAdapter(loc);
				loc.notifyDataSetChanged();
				if (dialogoEspera.isShowing()) {
					dialogoEspera.dismiss();
				}
			};

		}.execute();

	}

	private void inicializarInformacionArticulo() {
		this.articleCodeText = (TextView) findViewById(R.id.ArticleCode);
		this.articleAliasText = (TextView) findViewById(R.id.ArticleAlias);
		this.articleDescText = (TextView) findViewById(R.id.ArticleName);
		this.totalArticleText = (TextView) findViewById(R.id.TotalArticle);
		this.avaiableArticlesText = (TextView) findViewById(R.id.avaiableArticles);

		this.articleInfo = (ListView) findViewById(R.id.infoArticleList);
		this.articleInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				InformacionArticulo aux = (InformacionArticulo) parent.getAdapter().getItem(position);
				switch (aux.getTipoInformacionArticulo()) {
				case CLIENTE:
					break;
				case DESPIECE:
					iniciarActividadDespiece((MapeoDespiece) aux.getObjeto());
					break;
				case LOCALIZACION:
					break;
				case ORDEN:
					iniciarActividadOrden((MapeoOrdenDetalle) aux.getObjeto());
					break;
				case PEDIDOSRECIBIDOS:
					break;
				case PROVEEDOR:
					break;
				case TITULO:
					break;
				default:
					break;
				}
			}
		});
	}

	private void iniciarActividadDespiece(final MapeoDespiece despiece) {
		new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... params) {
				try {
					Intent i = new Intent(getApplicationContext(), ActividadInformacionArticulo.class);
					i.putExtra("Articulo", new MapeoArticulo(despiece.getIngrediente()));
					startActivity(i);

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

		}.execute();

	}
	
	private void iniciarActividadOrden(final MapeoOrdenDetalle orden) {
		new AsyncTask<Object, Object, Object>() {
			@Override
			protected Object doInBackground(Object... params) {
				try {
					Intent i = new Intent(getApplicationContext(), ActividadInformacionOrdenProd.class);
					i.putExtra("OrdenProduccion", new MapeoOrden(orden.getOrden()));
					startActivity(i);

				} catch (SQLException e) {
					e.printStackTrace();
				}
				return null;
			}

		}.execute();

	}


}
