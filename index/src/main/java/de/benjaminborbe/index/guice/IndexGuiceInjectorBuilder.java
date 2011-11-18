package de.benjaminborbe.index.guice;

import org.ops4j.peaberry.Peaberry;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

public class IndexGuiceInjectorBuilder {

	private IndexGuiceInjectorBuilder() {
	}

	public static Injector getInjector(final BundleContext context) {
		return Guice.createInjector(Peaberry.osgiModule(context), new ServletModule(), new IndexOsgiModule(),
				new IndexModule());
	}
}
