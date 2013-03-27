package de.benjaminborbe.tools.io;

import java.io.PrintWriter;

public class FlushPrintWriter {

	private final PrintWriter out;

	public FlushPrintWriter(final PrintWriter out) {
		this.out = out;
	}

	public void println(final String text) {
		out.println(text);
		out.flush();
	}

	public void printStackTrace(final Exception e) {
		e.printStackTrace(out);
		out.flush();
	}

	public void print(final String text) {
		out.print(text);
		out.flush();
	}

	public void print(final Object object) {
		if (object != null) {
			print(object.toString());
		}
	}

	public void println(final Object object) {
		if (object != null) {
			println(object.toString());
		}
	}

}
