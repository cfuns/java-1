package de.benjaminborbe.util.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.FilterInfo;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.util.gui.guice.UtilGuiModules;
import de.benjaminborbe.util.gui.servlet.UtilGuiCalcServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiDayDiffServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiPasswordGeneratorServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiPenMeServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiPentestServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiQUnitServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiTimeConvertServlet;

public class UtilGuiActivator extends HttpBundleActivator {

	@Inject
	private UtilGuiCalcServlet utilGuiCalcServlet;

	@Inject
	private UtilGuiDayDiffServlet utilGuiDayDiffServlet;

	@Inject
	private UtilGuiServlet utilServlet;

	@Inject
	private UtilGuiPasswordGeneratorServlet utilPasswordGeneratorServlet;

	@Inject
	private UtilGuiTimeConvertServlet utilGuiTimeConvertServlet;

	@Inject
	private UtilGuiQUnitServlet utilGuiQUnitServlet;

	@Inject
	private UtilGuiPentestServlet utilGuiPentestServlet;

	@Inject
	private UtilGuiPenMeServlet utilGuiPenMeServlet;

	public UtilGuiActivator() {
		super("util");
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new UtilGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(utilServlet, "/"));
		result.add(new ServletInfo(utilGuiCalcServlet, "/calc"));
		result.add(new ServletInfo(utilPasswordGeneratorServlet, "/passwordGenerator"));
		result.add(new ServletInfo(utilGuiTimeConvertServlet, "/timeConvert"));
		result.add(new ServletInfo(utilGuiDayDiffServlet, "/daydiff"));
		result.add(new ServletInfo(utilGuiQUnitServlet, "/qunit"));
		result.add(new ServletInfo(utilGuiPentestServlet, "/pentest"));
		result.add(new ServletInfo(utilGuiPenMeServlet, "/penme"));
		return result;
	}

	@Override
	protected Collection<FilterInfo> getFilterInfos() {
		final Set<FilterInfo> result = new HashSet<FilterInfo>(super.getFilterInfos());
		// result.add(new FilterInfo(utilFilter, ".*", 1));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<ResourceInfo>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		result.add(new ResourceInfo("/js", "js"));
		result.add(new ResourceInfo("/html", "html"));
		return result;
	}
}
