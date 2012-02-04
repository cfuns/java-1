package de.benjaminborbe.vaadin.gui.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;

import de.benjaminborbe.vaadin.gui.MyApplication;

@Singleton
public class MyApplicationServlet extends AbstractApplicationServlet {

	private static final long serialVersionUID = 3196753149929364590L;

	private final Logger logger;

	@Inject
	public MyApplicationServlet(final Logger logger) {
		this.logger = logger;
	}

	@Override
	protected Application getNewApplication(final HttpServletRequest request) throws ServletException {
		logger.debug("getNewApplication");
		// Creates a new application instance
		try {
			final Application application = getApplicationClass().newInstance();

			return application;
		}
		catch (final IllegalAccessException e) {
			throw new ServletException("getNewApplication failed", e);
		}
		catch (final InstantiationException e) {
			throw new ServletException("getNewApplication failed", e);
		}
		catch (final ClassNotFoundException e) {
			throw new ServletException("getNewApplication failed", e);
		}
	}

	@Override
	protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
		logger.debug("getApplicationClass");
		return MyApplication.class;
	}

}
