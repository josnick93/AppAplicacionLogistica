package com.oscasistemas.appgestionlogistica.InformacionOrdenProduccion;

import java.util.ArrayList;
import com.MapeoBD.Ordenes.MapeoOrdenDetalle;
import com.MapeoBD.Picking.MapeoPickingDetalle;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;
import com.oscasistemas.appgestionlogistica.utils.AuxiliarFunctions;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdaptadorComponentesOrdenProd extends ArrayAdapter<InformacionComponenteOrdenProd> {
	private final Context context;
	private final ArrayList<InformacionComponenteOrdenProd> modelsArrayList;

	public AdaptadorComponentesOrdenProd(Context context, ArrayList<InformacionComponenteOrdenProd> modelsArrayList) {

		super(context, R.layout.prod_info_item, modelsArrayList);

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

			rowView = inflater.inflate(R.layout.prod_compt_item, parent, false);
			AutoResizeTextView articleCodeInfo = (AutoResizeTextView) rowView.findViewById(R.id.article_code);
			AutoResizeTextView articleAliasInfo = (AutoResizeTextView) rowView.findViewById(R.id.article_alias);
			AutoResizeTextView articleQualityInfo = (AutoResizeTextView) rowView.findViewById(R.id.article_counter);

			InformacionComponenteOrdenProd objeto = modelsArrayList.get(position);

			switch (objeto.getTipoComponente()) {
			case COMPONENTE:
				MapeoOrdenDetalle ordenDetalle = (MapeoOrdenDetalle) objeto.getObjeto();
				// articulo marcado
				if (ordenDetalle.getSwAsignada() == -1) {
					articleCodeInfo.setTextColor(Color.GREEN);
					articleAliasInfo.setTextColor(Color.GREEN);
					articleQualityInfo.setTextColor(Color.GREEN);
				}
				articleCodeInfo.setText(AuxiliarFunctions.format(ordenDetalle.getArticulo(), "xx.xxx.xxxx"));
				articleAliasInfo.setText(ordenDetalle.getAlias());
				articleQualityInfo.setText(String.valueOf(ordenDetalle.getCantidad()));
				break;
			case PICKING:
				MapeoPickingDetalle pickingDetalle = (MapeoPickingDetalle) objeto.getObjeto();
				if (pickingDetalle.getSwAsignada() == -1) {
					articleCodeInfo.setTextColor(Color.GREEN);
					articleAliasInfo.setTextColor(Color.GREEN);
					articleQualityInfo.setTextColor(Color.GREEN);
				} else if (pickingDetalle.getStock() - pickingDetalle.getCantidad() < 0.0) {
					articleCodeInfo.setTextColor(Color.RED);
					articleAliasInfo.setTextColor(Color.RED);
					articleQualityInfo.setTextColor(Color.RED);
				}
				articleCodeInfo.setText(AuxiliarFunctions.format(pickingDetalle.getArticulo(), "xx.xxx.xxxx"));
				articleAliasInfo.setText(pickingDetalle.getAlias());
				articleQualityInfo.setText(String.valueOf(pickingDetalle.getCantidad()));
				break;
			case TITULO:
				break;
			default:
				break;
			}

		} else {
			String titulo = modelsArrayList.get(position).getTitle();
			rowView = inflater.inflate(R.layout.article_header_item, parent, false);
			TextView titleView = (TextView) rowView.findViewById(R.id.header);
			titleView.setText(titulo);

		}

		// 5. retrn rowView
		return rowView;
	}

}
