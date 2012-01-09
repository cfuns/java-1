package de.benjaminborbe.tools.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseAdapter implements HttpServletResponse {

	private final HttpServletResponse response;

	public HttpServletResponseAdapter(final HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public String getCharacterEncoding() {
		return response.getCharacterEncoding();
	}

	@Override
	public String getContentType() {
		return response.getContentType();
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return response.getWriter();
	}

	@Override
	public void setCharacterEncoding(final String charset) {
		response.setCharacterEncoding(charset);
	}

	@Override
	public void setContentLength(final int len) {
		response.setContentLength(len);
	}

	@Override
	public void setContentType(final String type) {
		response.setContentType(type);
	}

	@Override
	public void setBufferSize(final int size) {
		response.setBufferSize(size);
	}

	@Override
	public int getBufferSize() {
		return response.getBufferSize();
	}

	@Override
	public void flushBuffer() throws IOException {
		response.flushBuffer();
	}

	@Override
	public void resetBuffer() {
		response.resetBuffer();
	}

	@Override
	public boolean isCommitted() {
		return response.isCommitted();
	}

	@Override
	public void reset() {
		response.reset();
	}

	@Override
	public void setLocale(final Locale loc) {
		response.setLocale(loc);
	}

	@Override
	public Locale getLocale() {
		return response.getLocale();
	}

	@Override
	public void addCookie(final Cookie cookie) {
		response.addCookie(cookie);
	}

	@Override
	public boolean containsHeader(final String name) {
		return response.containsHeader(name);
	}

	@Override
	public String encodeURL(final String url) {
		return response.encodeURL(url);
	}

	@Override
	public String encodeRedirectURL(final String url) {
		return response.encodeRedirectURL(url);
	}

	@Override
	public String encodeUrl(final String url) {
		return response.encodeUrl(url);
	}

	@Override
	public String encodeRedirectUrl(final String url) {
		return response.encodeRedirectUrl(url);
	}

	@Override
	public void sendError(final int sc, final String msg) throws IOException {
		response.sendError(sc, msg);
	}

	@Override
	public void sendError(final int sc) throws IOException {
		response.sendError(sc);
	}

	@Override
	public void sendRedirect(final String location) throws IOException {
		response.sendRedirect(location);
	}

	@Override
	public void setDateHeader(final String name, final long date) {
		response.setDateHeader(name, date);
	}

	@Override
	public void addDateHeader(final String name, final long date) {
		response.addDateHeader(name, date);
	}

	@Override
	public void setHeader(final String name, final String value) {
		response.setHeader(name, value);
	}

	@Override
	public void addHeader(final String name, final String value) {
		response.addHeader(name, value);
	}

	@Override
	public void setIntHeader(final String name, final int value) {
		response.setIntHeader(name, value);
	}

	@Override
	public void addIntHeader(final String name, final int value) {
		response.addIntHeader(name, value);
	}

	@Override
	public void setStatus(final int sc) {
		response.setStatus(sc);
	}

	@Override
	public void setStatus(final int sc, final String sm) {
		response.setStatus(sc, sm);
	}
}
