package de.benjaminborbe.bridge;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class StartupListener implements ServletContextListener {

	private FrameworkService service;

	public void contextInitialized(final ServletContextEvent event) {
		this.service = new FrameworkService(event.getServletContext());
		this.service.start();
	}

	public void contextDestroyed(final ServletContextEvent event) {
		this.service.stop();
	}
}
