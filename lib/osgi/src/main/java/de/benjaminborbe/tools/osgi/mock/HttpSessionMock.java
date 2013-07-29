package de.benjaminborbe.tools.osgi.mock;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

@SuppressWarnings("deprecation")
public class HttpSessionMock implements HttpSession {

	private final HashMap<String, Object> attributes = new HashMap<String, Object>();

	public Object getAttribute(final String arg0) {
		return attributes.get(arg0);
	}

	public void setAttribute(final String arg0, final Object arg1) {
		attributes.put(arg0, arg1);
	}

	public void removeAttribute(final String arg0) {
		attributes.remove(arg0);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public Enumeration getAttributeNames() {
		final Vector temp = new Vector();
		temp.addAll(attributes.keySet());
		return temp.elements();
	}

	public void putValue(final String arg0, final Object arg1) {

	}

	public void removeValue(final String arg0) {

	}

	public long getCreationTime() {

		return 0;
	}

	public String getId() {

		return null;
	}

	public long getLastAccessedTime() {

		return 0;
	}

	public int getMaxInactiveInterval() {

		return 0;
	}

	public ServletContext getServletContext() {

		return null;
	}

	public HttpSessionContext getSessionContext() {

		return null;
	}

	public Object getValue(final String arg0) {

		return null;
	}

	public String[] getValueNames() {

		return null;
	}

	public void invalidate() {

	}

	public boolean isNew() {

		return false;
	}

	public void setMaxInactiveInterval(final int arg0) {

	}

}
