package com.oscasistemas.appgestionlogistica.utils;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class AuxiliarFunctions {

	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		View view = activity.getCurrentFocus();
		// If no view currently has focus, create a new one, just so we can grab
		// a window token from it
		if (view == null) {
			view = new View(activity);
		}
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional
												// '-' and decimal.
	}

	public static String format(String number, String format) {
		char[] arr = new char[format.length()];
		int i = 0;
		for (int j = 0; j < format.length(); j++) {
			if (format.charAt(j) == 'x')
				arr[j] = number.charAt(i++);
			else
				arr[j] = format.charAt(j);
		}

		return new String(arr);
	}

	public static String zeroFormat(String s) {
		return String.format("%0" + (9 - s.length()) + "d%s", 0, s);
	}

	public static String zeroFormatOrder(String s) {
		return String.format("%0" + (5 - s.length()) + "d%s", 0, s);
	}

	public static String zeroEndFormat(String s) {
		return String.format("%-9s", s).replace(' ', '0');
	}

	public static String articleFormatSearch(String s) {
		if (s.length() <= 2)
			return String.format("%-9s", s).replace(' ', '0');
		else
			return String.format("%-9s", s).replace(' ', '0');
	}

	public static String articlePointFormatSearch(String s) {
		if(!s.contains(".")) return zeroEndFormat(s);
		String aux = "000000000";
		int punto1 = -1;
		int punto2 = -1;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '.') {
				if (punto1 == -1)
					punto1 = i;
				else
					punto2 = i;
			}
		}
		if (punto1 != -1) {
			int indice = 1;
			int auxpunto = punto1 - 1;
			while (auxpunto != -1) {
				aux = aux.substring(0, indice) + s.charAt(auxpunto) + aux.substring(indice + 1, aux.length());
				auxpunto--;
				indice--;
			}
			// incluir resto
			if (punto1 != s.length()) {
				indice = 4;
				if (punto2 == -1)
					auxpunto = s.length() - 1;
				else
					auxpunto = punto2 - 1;
				while (s.charAt(auxpunto) != '.') {
					aux = aux.substring(0, indice) + s.charAt(auxpunto) + aux.substring(indice + 1, aux.length());
					auxpunto--;
					indice--;
				}

			}

		}
		if (punto2 != -1) {
			int indice = aux.length() - 1;
			int auxpunto = s.length() - 1;
			while (s.charAt(auxpunto) != '.') {
				aux = aux.substring(0, indice) + s.charAt(auxpunto) + aux.substring(indice + 1, aux.length());
				auxpunto--;
				indice--;
			}

		}
		return aux;
	}

	public static void imprimirEtiqueta(final String m) {
		
		Log.d("SCAN", m);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// Your code goes here

					/**
					 * Servicio que envia una etiqueta al servidor
					 */
					Socket MyClient = null;
					try {
						int SERVERPORT = 2400;
						InetAddress serverAddr = InetAddress.getByName("192.168.10.146");

						MyClient = new Socket(serverAddr, SERVERPORT);
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						Log.d("SCAN", e.getMessage());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("SCAN", e.getMessage());
					}

					PrintStream output = null;
					try {
						output = new PrintStream(MyClient.getOutputStream());
						output.println(m);
						output.close();
					} catch (IOException e) {
						System.out.println(e);
						Log.d("SCAN", e.getMessage());

					}

					try {
						MyClient.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (Exception e) {
					Log.e("SCAN", e.getMessage());
				}
			}
		});
		thread.start();

	}

}
