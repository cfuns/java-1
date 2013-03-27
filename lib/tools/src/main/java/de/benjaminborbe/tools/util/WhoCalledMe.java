package de.benjaminborbe.tools.util;

import java.io.StringWriter;

public class WhoCalledMe {

	public static String whoCalledMe() {
		final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		final StackTraceElement caller = stackTraceElements[4];
		final String classname = caller.getClassName();
		final String methodName = caller.getMethodName();
		final int lineNumber = caller.getLineNumber();
		return classname + "." + methodName + ":" + lineNumber;
	}

	public static String getCallStack() {
		final StringWriter sw = new StringWriter();
		final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		for (int i = 2; i < stackTraceElements.length; i++) {
			final StackTraceElement ste = stackTraceElements[i];
			final String classname = ste.getClassName();
			final String methodName = ste.getMethodName();
			final int lineNumber = ste.getLineNumber();
			sw.append(classname + "." + methodName + ":" + lineNumber + "\n");
		}
		return sw.toString();
	}
}
