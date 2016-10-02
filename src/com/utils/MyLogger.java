package com.utils;

import android.nfc.Tag;
import android.util.Log;

public class MyLogger {
	public static String TAG = "WagonWheel";

	public static void log(String msg) {
		Log.d(TAG, msg);
	}
}
