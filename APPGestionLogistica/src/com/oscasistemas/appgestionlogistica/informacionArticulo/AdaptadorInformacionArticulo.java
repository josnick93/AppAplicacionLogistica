package com.oscasistemas.appgestionlogistica.informacionArticulo;

import java.util.ArrayList;

import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorInformacionArticulo extends ArrayAdapter<InformacionArticulo> {
	private final Context context;
	private final ArrayList<InformacionArticulo> modelsArrayList;

	public AdaptadorInformacionArticulo(Context context, ArrayList<InformacionArticulo> modelsArrayList) {

		super(context, R.layout.article_info_item, modelsArrayList);

		this.context = context;
		this.modelsArrayList = modelsArrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater

		View rowView = null;
		if (!modelsArrayList.get(position).isGroupHeader()) {
			rowView = inflater.inflate(R.layout.article_info_item, parent, false);

			// 3. Get icon,title & counter views from the rowView
			// ImageView imgView = (ImageView)
			// rowView.findViewById(R.id.item_icon);
			AutoResizeTextView campo1 = (AutoResizeTextView) rowView.findViewById(R.id.campo1);
			AutoResizeTextView campo2 = (AutoResizeTextView) rowView.findViewById(R.id.campo2);
			AutoResizeTextView campo3 = (AutoResizeTextView) rowView.findViewById(R.id.campo3);

			InformacionArticulo aux = modelsArrayList.get(position);
			switch (aux.getTipoInformacionArticulo()) {
			case LOCALIZACION:
				campo1.setText(aux.getCampo1());
				campo2.setText(aux.getCampo2().substring(0, aux.getCampo2().indexOf("(")));
				campo3.setText(" " + aux.getCampo3());
				break;
			case PROVEEDOR:
				campo1.setText(aux.getCampo1());
				campo3.setText(" " + aux.getCampo3());
				break;
			case CLIENTE:
				campo1.setText(aux.getCampo1());
				campo3.setText(" " + aux.getCampo3());
				break;
			case DESPIECE:
				campo1.setText(aux.getCampo1());
				campo3.setText(" " + aux.getCampo3());
				break;
			case ORDEN:
				campo1.setText(aux.getCampo1());
				campo3.setText(" " + aux.getCampo3());
				break;
			case PEDIDOSRECIBIDOS:
				campo1.setText(aux.getCampo1());
				campo2.setText(aux.getCampo2());
				campo3.setText(" " + aux.getCampo3());
				break;
			case TITULO:
				break;
			default:
				break;

			}
		} else {
			rowView = inflater.inflate(R.layout.article_header_item, parent, false);
			TextView titleView = (TextView) rowView.findViewById(R.id.header);
			titleView.setText(modelsArrayList.get(position).getTitle());

		}

		// 5. retrn rowView
		return rowView;
	}

}
