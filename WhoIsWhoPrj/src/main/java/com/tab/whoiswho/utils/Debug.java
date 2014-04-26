/**
 * Copyright (c) 2013 Sykehusapotekene i Midt Norge HF. All rights reserved. 
 */

package com.tab.whoiswho.utils;

import android.util.Log;

public class Debug {

	private static final boolean DEBUG_MODE = true;
	private static final int DEBUG_LEVEL = Log.VERBOSE;

	/**
	 * Shows an info message
	 * 
	 * @param log
	 *            Message to show as info
	 **/
	public static void logInfo(String log) {
		if (DEBUG_MODE && DEBUG_LEVEL <= Log.INFO) {
			log(Log.INFO, log);
		}
	}

	/**
	 * Shows a warning message
	 * 
	 * @param log
	 *            Message to show as warning
	 * **/
	public static void logWarning(String log) {
		if (DEBUG_MODE && DEBUG_LEVEL <= Log.WARN) {
			log(Log.WARN, log);
		}
	}

	/**
	 * Shows a debug message
	 * 
	 * @param log
	 *            Message to show as debug
	 **/
	public static void logDebug(String log) {
		if (DEBUG_MODE && DEBUG_LEVEL <= Log.DEBUG) {
			log(Log.DEBUG, log);
		}
	}

	/**
	 * Shows a error message
	 * 
	 * @param log
	 *            Message to show as error
	 **/
	public static void logError(String log) {
		if (DEBUG_MODE && DEBUG_LEVEL <= Log.ERROR) {
			log(Log.ERROR, log);
		}
	}

	/**
	 * Shows a log message
	 * 
	 * @param priority
	 *            The priority/type of this log message
	 * @param message
	 *            Message to show
	 **/
	private static void log(int priority, String message) {
		if (message != null) {

			StringBuilder builder = new StringBuilder();

			StackTraceElement stackTrace = Thread.currentThread()
					.getStackTrace()[4];

			int line = stackTrace.getLineNumber();
			String method = stackTrace.getMethodName();
			String file = stackTrace.getFileName();

			builder.append("[");
			builder.append(file);
			builder.append("] ");
			builder.append(method);
			builder.append("(");
			builder.append(line);
			builder.append(")");

			String tag = builder.toString();

			Log.println(priority, tag, message);
		}
	}

}
