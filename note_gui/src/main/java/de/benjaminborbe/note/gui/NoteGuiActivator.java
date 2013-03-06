package de.benjaminborbe.note.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.note.gui.guice.NoteGuiModules;
import de.benjaminborbe.note.gui.servlet.NoteGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class NoteGuiActivator extends HttpBundleActivator {

	@Inject
	private NoteGuiServlet noteGuiServlet;

	public NoteGuiActivator() {
		super(NoteGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new NoteGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(noteGuiServlet, NoteGuiConstants.URL_HOME));
		return result;
	}

}
