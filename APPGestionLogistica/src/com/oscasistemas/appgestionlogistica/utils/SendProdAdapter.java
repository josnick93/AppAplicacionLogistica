package com.oscasistemas.appgestionlogistica.utils;

import java.util.ArrayList;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.articulo.ProdInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class SendProdAdapter extends ArrayAdapter<ProdInfo> {
	private final Context context;
	private final ArrayList<ProdInfo> modelsArrayList;

	public SendProdAdapter(Context context, ArrayList<ProdInfo> modelsArrayList) {

		super(context, R.layout.prod_info_item, modelsArrayList);

		this.context = context;
		this.modelsArrayList = modelsArrayList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater

		View rowView = null;
		rowView = inflater.inflate(R.layout.prod_info_item, parent, false);
		
		
//		TextView articleCode=(TextView) rowView.findViewById(R.id.numero_orden);
//		articleCode.setText(modelsArrayList.get(position).getOrdenProduccion());
//		
//		TextView ArticleAlias=(TextView) rowView.findViewById(R.id.cliente);
//		ArticleAlias.setText(modelsArrayList.get(position).getCliente());
//		
		
		AutoResizeTextView prodRef=(AutoResizeTextView) rowView.findViewById(R.id.referencia);
		prodRef.setText(modelsArrayList.get(position).getReferencia());
		
		
//		TextView prodClient = (TextView) rowView.findViewById(R.id.usuario);
//		prodClient.setText(modelsArrayList.get(position).getUsuario());		
//		
//		SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMM, yyyy ",new Locale("es", "ES")); 
//		AutoResizeTextView sendDate = (AutoResizeTextView) rowView.findViewById(R.id.envio);
//		sendDate.setText(postFormater.format(modelsArrayList.get(position).getEnvio()));
//		AutoResizeTextView recvDate = (AutoResizeTextView) rowView.findViewById(R.id.recepcion);
//		if(modelsArrayList.get(position).getRecepcion()!=null)
//			recvDate.setText(postFormater.format(modelsArrayList.get(position).getRecepcion()));
//		else recvDate.setVisibility(View.GONE);

		// 5. retrn rowView
		return rowView;
	}
}
