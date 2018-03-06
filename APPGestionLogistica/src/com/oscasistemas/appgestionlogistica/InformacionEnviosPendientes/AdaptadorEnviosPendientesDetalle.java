package com.oscasistemas.appgestionlogistica.InformacionEnviosPendientes;

import java.util.List;


import com.MapeoBD.Pedidos.MapeoPedidoDetalle;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorEnviosPendientesDetalle extends BaseAdapter {

	private Context context;
	private final List<InfoPedidoDetalle> modelsArrayList;
	LayoutInflater layoutInflater;

	public AdaptadorEnviosPendientesDetalle(Context context, List<InfoPedidoDetalle> modelsArrayList) {
		this.context = context;
		this.modelsArrayList = modelsArrayList;
		LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.modelsArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.modelsArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater

		View rowView = null;
		if (!modelsArrayList.get(position).isCabecera()) {

			rowView = inflater.inflate(R.layout.article_picking_item, parent, false);
			TextView code = (TextView) rowView.findViewById(R.id.codigo_articulo);
			TextView alias = (TextView) rowView.findViewById(R.id.referencia_cliente);
			AutoResizeTextView cantidad = (AutoResizeTextView) rowView.findViewById(R.id.articulo_cantidad);

			MapeoPedidoDetalle a = this.modelsArrayList.get(position).getPedidoDetalle();
			if (a != null) {

				if (a.getSwAsignada() == -1) {
					code.setTextColor(Color.GREEN);
					alias.setTextColor(Color.GREEN);
					cantidad.setTextColor(Color.GREEN);
				}
				code.setText(AuxiliarFunctions.format(a.getArticulo(), "xx.xxx.xxxx"));
				alias.setText(a.getAlias());
				cantidad.setText(String.valueOf(a.getCantidad()));
			}else{
				code.setText(this.modelsArrayList.get(position).getC1());
				alias.setText(this.modelsArrayList.get(position).getC2());
				cantidad.setText(this.modelsArrayList.get(position).getC3());
			}

		} else {
			String titulo = modelsArrayList.get(position).getTitulo();
			rowView = inflater.inflate(R.layout.article_header_item, parent, false);
			TextView titleView = (TextView) rowView.findViewById(R.id.header);
			titleView.setText(titulo);

		}

		// 5. retrn rowView
		return rowView;
	}

}
