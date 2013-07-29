package de.benjaminborbe.cms.gui;

import de.benjaminborbe.cms.gui.guice.CmsGuiModules;
import de.benjaminborbe.cms.gui.servlet.CmsGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CmsGuiActivator extends HttpBundleActivator {

	@Inject
	private CmsGuiServlet cmsGuiServlet;

	public CmsGuiActivator() {
		super(CmsGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new CmsGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(cmsGuiServlet, CmsGuiConstants.URL_HOME));
		return result;
	}

}
