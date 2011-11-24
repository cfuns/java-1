package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CheckRegistryImpl implements CheckRegistry {

	private final Set<Check> data = new HashSet<Check>();

	@Inject
	public CheckRegistryImpl() {
	}

	@Override
	public Collection<Check> getAll() {
		return data;
	}

	@Override
	public void register(final Check check) {
		data.add(check);
	}
}
