package de.benjaminborbe.proxy.gui.util;

import com.google.inject.Inject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Proxy {

	@Inject
	public Proxy() {
	}

	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
	}
}
