package com.oscasistemas.appgestionlogistica.BusquedaEnviosPendientes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import com.MapeoBD.Pedidos.MapeoPedido;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorEnviosPendientes extends ArrayAdapter<MapeoPedido> {
	private final Context context;
	private final ArrayList<MapeoPedido> modelsArrayList;

	public AdaptadorEnviosPendientes(Context context, ArrayList<MapeoPedido> modelsArrayList) {

		super(context, R.layout.send_info_item, modelsArrayList);

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
		rowView = inflater.inflate(R.layout.send_info_item, parent, false);

		TextView articleCode = (TextView) rowView.findViewById(R.id.numeroPedido);
		articleCode.setText(String.valueOf(modelsArrayList.get(position).getPedido()));
		TextView articleAlias = (TextView) rowView.findViewById(R.id.cliente_proveedor);
		articleAlias.setText(modelsArrayList.get(position).getNombreCliente());

		TextView provCost = (TextView) rowView.findViewById(R.id.referencia);
		provCost.setText(modelsArrayList.get(position).getReferencia());

		SimpleDateFormat postFormater = new SimpleDateFormat("dd MMMM, yyyy ", new Locale("es", "ES"));
		AutoResizeTextView sendDate = (AutoResizeTextView) rowView.findViewById(R.id.sendDate);
		if (modelsArrayList.get(position).getFechaEntrega() != null)
			sendDate.setText(postFormater.format(modelsArrayList.get(position).getFechaEntrega()));
		AutoResizeTextView recvDate = (AutoResizeTextView) rowView.findViewById(R.id.ReceptionDateText);
		if (modelsArrayList.get(position).getFecha() != null)
			recvDate.setText(postFormater.format(modelsArrayList.get(position).getFecha()));
		else
			recvDate.setVisibility(View.GONE);

		// 5. retrn rowView
		return rowView;
	}
}
