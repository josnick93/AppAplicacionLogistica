package com.oscasistemas.appgestionlogistica.Expediciones;

import java.util.ArrayList;


import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class InformacionExpedicionAdaptador extends ArrayAdapter<InformacionExpedicion> {
	private final Context context;
	private final ArrayList<InformacionExpedicion> modelsArrayList;

	public InformacionExpedicionAdaptador(Context context, ArrayList<InformacionExpedicion> modelsArrayList) {

		super(context, R.layout.informacion_expedicion, modelsArrayList);

		this.context = context;
		this.modelsArrayList = modelsArrayList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// 1. Create inflater
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// 2. Get rowView from inflater

		View rowView = null;
		rowView = inflater.inflate(R.layout.informacion_expedicion, parent, false);

		AutoResizeTextView expedicion, cliente, referencia, usuario;

		expedicion = (AutoResizeTextView) rowView.findViewById(R.id.ExpedicionInfoText);
		cliente = (AutoResizeTextView) rowView.findViewById(R.id.ClienteInforText);
		referencia = (AutoResizeTextView) rowView.findViewById(R.id.ReferenciaInfoText);
		usuario = (AutoResizeTextView) rowView.findViewById(R.id.UsuarioInfoText);

		InformacionExpedicion ex = modelsArrayList.get(position);

		expedicion.setText(ex.getExpedicion());
		cliente.setText(ex.getCliente());
		referencia.setText(ex.getReferencia());
		usuario.setText(ex.getUsuario());

		// 5. retrn rowView
		return rowView;
	}

}
