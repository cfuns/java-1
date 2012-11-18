package de.benjaminborbe.meta.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class BundleResolverImpl implements BundleResolver {

	private final Logger logger;

	@Inject
	public BundleResolverImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public List<String> getBundleSymbolicNames() {
		logger.info("getBundleSymbolicNames");
		final ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles");
		final String value = resourceBundle.getString("bundles");
		return parseValue(value);
	}

	protected List<String> parseValue(final String value) {
		final List<String> result = new ArrayList<String>();
		if (value == null) {
			return result;
		}
		final String[] parts = value.split("[\\s,]");

		for (final String part : parts) {
			if (part.length() > 0) {
				result.add(part);
			}
		}
		return result;
	}
}
