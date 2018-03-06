package com.oscasistemas.appgestionlogistica.BusquedaOrdenesProduccion;

import java.sql.SQLException;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import com.MapeoBD.Cliente.MapeoCliente;
import com.MapeoBD.Ordenes.MapeoOrden;
import com.MapeoBD.Usuario.MapeoUsuario;
import com.oscasistemas.appgestionlogistica.R;
import com.oscasistemas.appgestionlogistica.utils.AutoResizeTextView;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdaptadorListaOrdenesProduccion extends BaseAdapter {

	private Context context;
	private List<MapeoOrden> ordenes;
	LayoutInflater layoutInflater;

	public AdaptadorListaOrdenesProduccion(Context context, List<MapeoOrden> items) {
		this.context = context;
		this.ordenes = items;
		LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.ordenes.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.ordenes.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		if (convertView == null) {
			// Create a new view into the list.
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.prod_info_item, parent, false);

		}

		// Set data into the view.
		final AutoResizeTextView numeroOrdenInfo = (AutoResizeTextView) rowView.findViewById(R.id.numeroOrdenInfo);
		final AutoResizeTextView referenciaOrdenInfo = (AutoResizeTextView) rowView
				.findViewById(R.id.referenciaOrdenInfo);
		final AutoResizeTextView clienteOrdenInfo = (AutoResizeTextView) rowView.findViewById(R.id.clienteOrdenInfo);
		final AutoResizeTextView usuarioOrdenInfo = (AutoResizeTextView) rowView.findViewById(R.id.usuarioOrdenInfo);
		final AutoResizeTextView envioOrdenInfo = (AutoResizeTextView) rowView.findViewById(R.id.envioOrdenInfo);
		final AutoResizeTextView recepcionOrdenInfo = (AutoResizeTextView) rowView
				.findViewById(R.id.recepcionOrdenInfo);
		/**
		 * 
		 */
		final TextView referenciaOrdenText = (TextView) rowView.findViewById(R.id.referenciaOrdenText);
		final TextView envioOrdenText = (TextView) rowView.findViewById(R.id.envioOrdenText);
		final TextView recepcionOrdenText = (TextView) rowView.findViewById(R.id.recepcionOrdenText);

		final MapeoOrden ordenProduccion = this.ordenes.get(position);
		// rellenar datos
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
		
		
		numeroOrdenInfo.setText(String.valueOf(ordenProduccion.getOrden()));
		
		if(ordenProduccion.getReferencia()!=null)referenciaOrdenInfo.setText(ordenProduccion.getReferencia());
		else referenciaOrdenText.setVisibility(View.INVISIBLE);
		
		clienteOrdenInfo.setText(obtenerCliente(ordenProduccion.getCliente()).getNombre());
		
		
		usuarioOrdenInfo.setText(obtenerUsuario(ordenProduccion.getiUser()).getUser());
		
		if(ordenProduccion.getFecha()!=null)envioOrdenInfo.setText(sdf.format(ordenProduccion.getFecha()));
		else envioOrdenText.setVisibility(View.INVISIBLE);
		
		if(ordenProduccion.getFechaEntrega()!=null)recepcionOrdenInfo.setText(sdf.format(ordenProduccion.getFechaEntrega()));
		else recepcionOrdenText.setVisibility(View.INVISIBLE);

		return rowView;
	}

	private MapeoCliente obtenerCliente(final int idCliente) {
		AsyncTask<MapeoCliente, MapeoCliente, MapeoCliente> tarea = new AsyncTask<MapeoCliente, MapeoCliente, MapeoCliente>() {
			@Override
			protected MapeoCliente doInBackground(MapeoCliente... params) {
				MapeoCliente cliente = null;
				try {
					cliente = new MapeoCliente(idCliente);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return cliente;

			}
		};
		try {
			return tarea.execute().get();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	private MapeoUsuario obtenerUsuario(final int idUsuario) {
		AsyncTask<MapeoUsuario, MapeoUsuario, MapeoUsuario> tarea = new AsyncTask<MapeoUsuario, MapeoUsuario, MapeoUsuario>() {
			@Override
			protected MapeoUsuario doInBackground(MapeoUsuario... params) {
				MapeoUsuario usuario = null;
				try {
					usuario = new MapeoUsuario(idUsuario);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return usuario;

			}
		};
		try {
			return tarea.execute().get();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			return null;
		}
	}

}
