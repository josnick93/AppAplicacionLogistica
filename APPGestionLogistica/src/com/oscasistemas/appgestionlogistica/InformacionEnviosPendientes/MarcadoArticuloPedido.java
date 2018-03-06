package com.oscasistemas.appgestionlogistica.InformacionEnviosPendientes;

import com.oscasistemas.appgestionlogistica.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MarcadoArticuloPedido implements OnItemLongClickListener {

	Activity mycontext;

	public MarcadoArticuloPedido(Activity myContext) {
		this.mycontext = myContext;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

		final String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		AlertDialog.Builder myDialog = new AlertDialog.Builder(mycontext);
		myDialog.setTitle("My Dialog with ListView");

		myDialog.setItems(days, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String item = days[which];
				Toast.makeText(mycontext, item, Toast.LENGTH_LONG).show();

			}
		});

		myDialog.setNegativeButton("Cancel", null);

		myDialog.show();

		return true;
	}

}
