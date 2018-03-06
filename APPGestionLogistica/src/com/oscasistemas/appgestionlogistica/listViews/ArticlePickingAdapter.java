package com.oscasistemas.appgestionlogistica.listViews;

import java.util.List;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.InformacionEnviosPendientes.InfoPedidoDetalle;
import com.oscasistemas.appgestionlogistica.articulo.ArticlePickingInfo;
import com.oscasistemas.appgestionlogistica.informacionArticulo.InformacionArticulo;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ArticlePickingAdapter extends BaseAdapter {

	private Context context;
	private List<InfoPedidoDetalle> articles;
	LayoutInflater layoutInflater;

	public ArticlePickingAdapter(Context context, List<InfoPedidoDetalle> items) {
		this.context = context;
		this.articles = items;
		LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.articles.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.articles.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		// 1. Create inflater
//		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//		// 2. Get rowView from inflater
//
//		View rowView = null;
//		if (!modelsArrayList.get(position).isGroupHeader()) {
//			rowView = inflater.inflate(R.layout.article_info_item, parent, false);
//
//			// 3. Get icon,title & counter views from the rowView
//			// ImageView imgView = (ImageView)
//			// rowView.findViewById(R.id.item_icon);
//			AutoResizeTextView campo1 = (AutoResizeTextView) rowView.findViewById(R.id.campo1);
//			AutoResizeTextView campo2 = (AutoResizeTextView) rowView.findViewById(R.id.campo2);
//			AutoResizeTextView campo3 = (AutoResizeTextView) rowView.findViewById(R.id.campo3);
//
//			InformacionArticulo aux = modelsArrayList.get(position);
//			switch (aux.getTipoInformacionArticulo()) {
//			case LOCALIZACION:
//				campo1.setText(aux.getCampo1());
//				campo2.setText(aux.getCampo2().substring(0, aux.getCampo2().indexOf("(")));
//				campo3.setText(" " + aux.getCampo3());
//				break;
//			case PROVEEDOR:
//				campo1.setText(aux.getCampo1());
//				campo3.setText(" " + aux.getCampo3());
//				break;
//			case CLIENTE:
//				campo1.setText(aux.getCampo1());
//				campo3.setText(" " + aux.getCampo3());
//				break;
//			case DESPIECE:
//				campo1.setText(aux.getCampo1());
//				campo3.setText(" " + aux.getCampo3());
//				break;
//			case ORDEN:
//				campo1.setText(aux.getCampo1());
//				campo3.setText(" " + aux.getCampo3());
//				break;
//			case PEDIDOSRECIBIDOS:
//				campo1.setText(aux.getCampo1());
//				campo2.setText(aux.getCampo2());
//				campo3.setText(" " + aux.getCampo3());
//				break;
//			case TITULO:
//				break;
//			default:
//				break;
//
//			}
//		} else {
//			rowView = inflater.inflate(R.layout.article_header_item, parent, false);
//			TextView titleView = (TextView) rowView.findViewById(R.id.header);
//			titleView.setText(modelsArrayList.get(position).getTitle());
//
//		}
//
//		// 5. retrn rowView
//		return rowView;
		return null;
	}

}
