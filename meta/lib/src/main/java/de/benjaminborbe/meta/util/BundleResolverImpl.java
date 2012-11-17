package de.benjaminborbe.meta.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.google.inject.Inject;

public class BundleResolverImpl implements BundleResolver {

	@Inject
	public BundleResolverImpl() {
		super();
	}

	@Override
	public List<String> getBundleSymbolicNames() {
		final ResourceBundle resourceBundle = ResourceBundle.getBundle("bundles");
		final List<String> result = new ArrayList<String>();
		final String value = resourceBundle.getString("bundles");

		final String[] parts = value.split("[\\s,]");

		for (final String b : parts) {
			result.add(b.trim());
		}
		return result;
	}
}
