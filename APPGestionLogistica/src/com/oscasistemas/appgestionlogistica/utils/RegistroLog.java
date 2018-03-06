package com.oscasistemas.appgestionlogistica.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class RegistroLog {

	public static void appendLog(String text) {
		File logFile = new File(Environment.getExternalStorageDirectory() + "/AplicacionGestionLogisticaLog.txt");
		BufferedWriter buf = null;
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			buf = new BufferedWriter(new FileWriter(logFile, true));
			text = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK).format(new Date())
					+ " ----------------------------------------- " + text + "\n";
			Log.d("LOG", text);
			buf.append(text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			try {
				buf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
