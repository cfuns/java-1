package de.benjaminborbe.util.gui;

import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ResourceInfo;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import de.benjaminborbe.util.gui.guice.UtilGuiModules;
import de.benjaminborbe.util.gui.service.UtilGuiNavigationEntry;
import de.benjaminborbe.util.gui.servlet.UtilGuiCalcServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiDayDiffServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiLogServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiPasswordGeneratorServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiPenMeServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiPentestServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiQUnitServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiRequestDumpServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiTimeConvertServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiTimeServlet;
import de.benjaminborbe.util.gui.servlet.UtilGuiUUIDGeneratorServlet;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UtilGuiActivator extends HttpBundleActivator {

	@Inject
	private UtilGuiRequestDumpServlet utilGuiRequestDumpServlet;

	@Inject
	private UtilGuiUUIDGeneratorServlet utilGuiUUIDGeneratorServlet;

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

	@Inject
	private UtilGuiLogServlet utilGuiLogServlet;

	@Inject
	private UtilGuiNavigationEntry utilGuiNavigationEntry;

	public UtilGuiActivator() {
		super(UtilGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new UtilGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(utilServlet, UtilGuiConstants.URL_SLASH));
		result.add(new ServletInfo(utilGuiCalcServlet, UtilGuiConstants.URL_CALC));
		result.add(new ServletInfo(utilPasswordGeneratorServlet, UtilGuiConstants.URL_PASSWORD_GENERATOR));
		result.add(new ServletInfo(utilGuiTimeConvertServlet, UtilGuiConstants.URL_TIME_CONVERT));
		result.add(new ServletInfo(utilGuiDayDiffServlet, UtilGuiConstants.URL_DAY_DIFF));
		result.add(new ServletInfo(utilGuiQUnitServlet, UtilGuiConstants.URL_QUNIT));
		result.add(new ServletInfo(utilGuiPentestServlet, UtilGuiConstants.URL_PENTEST));
		result.add(new ServletInfo(utilGuiPenMeServlet, UtilGuiConstants.URL_PENME));
		result.add(new ServletInfo(utilGuiTimeServlet, UtilGuiConstants.URL_TIME));
		result.add(new ServletInfo(utilGuiLogServlet, UtilGuiConstants.URL_LOG));
		result.add(new ServletInfo(utilGuiUUIDGeneratorServlet, UtilGuiConstants.URL_UUID_GENERATOR));
		result.add(new ServletInfo(utilGuiRequestDumpServlet, UtilGuiConstants.URL_DUMP_REQUEST));
		return result;
	}

	@Override
	protected Collection<ResourceInfo> getResouceInfos() {
		final Set<ResourceInfo> result = new HashSet<>(super.getResouceInfos());
		result.add(new ResourceInfo("/css", "css"));
		result.add(new ResourceInfo("/js", "js"));
		result.add(new ResourceInfo("/html", "html"));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, utilGuiNavigationEntry));
		return result;
	}
}
