package de.benjaminborbe.poker.test;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

public class HttpSessionMock implements HttpSession {

	public static final String SESSION_ID = "sessionId123";

	@Override
	public long getCreationTime() {
		return 0;
	}

	@Override
	public String getId() {
		return SESSION_ID;
	}

	@Override
	public long getLastAccessedTime() {
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}

	@Override
	public void setMaxInactiveInterval(final int interval) {
	}

	@Override
	public int getMaxInactiveInterval() {
		return 0;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return null;
	}

	@Override
	public Object getAttribute(final String name) {
		return null;
	}

	@Override
	public Object getValue(final String name) {
		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		return null;
	}

	@Override
	public String[] getValueNames() {
		return new String[0];
	}

	@Override
	public void setAttribute(final String name, final Object value) {
	}

	@Override
	public void putValue(final String name, final Object value) {
	}

	@Override
	public void removeAttribute(final String name) {
	}

	@Override
	public void removeValue(final String name) {
	}

	@Override
	public void invalidate() {
	}

	@Override
	public boolean isNew() {
		return false;
	}
}
