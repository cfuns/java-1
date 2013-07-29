package de.benjaminborbe.forum.gui;

import de.benjaminborbe.forum.gui.guice.ForumGuiModules;
import de.benjaminborbe.forum.gui.servlet.ForumGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ForumGuiActivator extends HttpBundleActivator {

	@Inject
	private ForumGuiServlet forumGuiServlet;

	public ForumGuiActivator() {
		super(ForumGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new ForumGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(forumGuiServlet, ForumGuiConstants.URL_HOME));
		return result;
	}

}
