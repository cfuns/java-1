package de.benjaminborbe.vnc.core.util;

import com.glavsoft.viewer.ViewerGui;
import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.vnc.core.guice.VncModulesMock;

public class ViewerGuiRunner {

	public static void main(final String[] args) {
		final Injector injector = GuiceInjectorBuilder.getInjector(new VncModulesMock());
		final ViewerGui viewer = injector.getInstance(ViewerGui.class);
		viewer.run();
	}
}
