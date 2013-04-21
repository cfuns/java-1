package de.benjaminborbe.proxy.core.dao;

import de.benjaminborbe.proxy.api.ProxyContent;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class ProxyContentImpl implements ProxyContent {

	private final ByteArrayOutputStream outputStream;

	public ProxyContentImpl() {
		outputStream = new ByteArrayOutputStream();
	}

	@Override
	public byte[] getContent() {
		return outputStream.toByteArray();
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

}
