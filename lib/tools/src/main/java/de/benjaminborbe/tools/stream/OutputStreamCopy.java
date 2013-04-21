package de.benjaminborbe.tools.stream;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamCopy extends OutputStream {

	private final OutputStream outputStreamOrg;

	private final OutputStream outputStreamCopy;

	public OutputStreamCopy(final OutputStream outputStreamOrg, final OutputStream outputStreamCopy) {
		this.outputStreamOrg = outputStreamOrg;
		this.outputStreamCopy = outputStreamCopy;
	}

	@Override
	public void write(final int b) throws IOException {
		outputStreamOrg.write(b);
		outputStreamCopy.write(b);
	}
}
