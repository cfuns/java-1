package de.benjaminborbe.tools.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceInjectorBuilder {

	private GuiceInjectorBuilder() {
	}

	public static Injector getInjector(final Modules modules) {
		return Guice.createInjector(modules.getModules());
	}
}
