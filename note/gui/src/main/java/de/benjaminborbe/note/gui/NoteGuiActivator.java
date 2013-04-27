package de.benjaminborbe.note.gui;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.note.gui.guice.NoteGuiModules;
import de.benjaminborbe.note.gui.service.NoteGuiNavigationEntry;
import de.benjaminborbe.note.gui.servlet.NoteGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NoteGuiActivator extends HttpBundleActivator {

	@Inject
	private NoteGuiNavigationEntry noteGuiNavigationEntry;

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
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(noteGuiServlet, NoteGuiConstants.URL_HOME));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, noteGuiNavigationEntry));
		return result;
	}

}
