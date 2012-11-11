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
import de.benjaminborbe.util.gui.servlet.UtilGuiTimeServlet;

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

	@Inject
	private UtilGuiTimeServlet utilGuiTimeServlet;

	public UtilGuiActivator() {
		super(UtilGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new UtilGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(utilServlet, UtilGuiConstants.URL_SLASH));
		result.add(new ServletInfo(utilGuiCalcServlet, UtilGuiConstants.URL_CALC));
		result.add(new ServletInfo(utilPasswordGeneratorServlet, UtilGuiConstants.PASSWORD_GENERATOR));
		result.add(new ServletInfo(utilGuiTimeConvertServlet, UtilGuiConstants.URL_TIME_CONVERT));
		result.add(new ServletInfo(utilGuiDayDiffServlet, UtilGuiConstants.URL_DAY_DIFF));
		result.add(new ServletInfo(utilGuiQUnitServlet, UtilGuiConstants.URL_QUNIT));
		result.add(new ServletInfo(utilGuiPentestServlet, UtilGuiConstants.URL_PENTEST));
		result.add(new ServletInfo(utilGuiPenMeServlet, UtilGuiConstants.URL_PENME));
		result.add(new ServletInfo(utilGuiTimeServlet, UtilGuiConstants.URL_TIME));
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
