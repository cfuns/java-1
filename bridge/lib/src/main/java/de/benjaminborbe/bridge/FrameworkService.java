package de.benjaminborbe.bridge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.felix.framework.Felix;
import org.apache.felix.framework.util.FelixConstants;

public final class FrameworkService {

	private final ServletContext context;

	private Felix felix;

	public FrameworkService(final ServletContext context) {
		this.context = context;
	}

	public void start() {
		try {
			doStart();
		}
		catch (final Exception e) {
			log("Failed to start framework", e);
		}
	}

	public void stop() {
		try {
			doStop();
		}
		catch (final Exception e) {
			log("Error stopping framework", e);
		}
	}

	private void doStart() throws Exception {
		try {
			felix = new Felix(createConfig());
			felix.start();
			log("OSGi framework started", null);
		}
		catch (final RuntimeException e) {
			log("OSGi framework started failed!", e);
		}
	}

	private void doStop() throws Exception {
		if (this.felix != null) {
			this.felix.stop();
		}

		log("OSGi framework stopped", null);
	}

	private Map<String, Object> createConfig() throws Exception {
		final Properties props = new Properties();
		props.load(context.getResourceAsStream("/WEB-INF/framework.properties"));

		final Map<String, Object> map = new HashMap<String, Object>();
		for (final Entry<Object, Object> entry : props.entrySet()) {
			map.put(entry.getKey().toString(), entry.getValue());
		}

		map.put(FelixConstants.SYSTEMBUNDLE_ACTIVATORS_PROP, Arrays.asList(new ProvisionActivator(context)));
		return map;
	}

	private void log(final String message, final Throwable cause) {
		context.log(message, cause);
	}
}
