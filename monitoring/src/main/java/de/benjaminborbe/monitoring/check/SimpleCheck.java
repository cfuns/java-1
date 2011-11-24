package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SimpleCheck implements Check {

	@Inject
	public SimpleCheck() {
	}

	@Override
	public boolean check() {
		return true;
	}

}
