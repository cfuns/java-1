package de.benjaminborbe.microblog.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.dashboard.api.DashboardContentWidget;
import de.benjaminborbe.microblog.gui.guice.MicroblogGuiModules;
import de.benjaminborbe.microblog.gui.service.MicroblogGuiDashboardWidget;
import de.benjaminborbe.microblog.gui.servlet.MicroblogGuiRefreshServlet;
import de.benjaminborbe.microblog.gui.servlet.MicroblogGuiSendConversationServlet;
import de.benjaminborbe.microblog.gui.servlet.MicroblogGuiSendPostServlet;
import de.benjaminborbe.microblog.gui.servlet.MicroblogGuiServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class MicroblogGuiActivator extends HttpBundleActivator {

	@Inject
	private MicroblogGuiServlet microblogServlet;

	@Inject
	private MicroblogGuiDashboardWidget microblogDashboardWidget;

	@Inject
	private MicroblogGuiSendPostServlet microblogGuiSendPostServlet;

	@Inject
	private MicroblogGuiSendConversationServlet microblogGuiSendConversationServlet;

	@Inject
	private MicroblogGuiRefreshServlet microblogGuiRefreshServlet;

	public MicroblogGuiActivator() {
		super(MicroblogGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MicroblogGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(microblogGuiRefreshServlet, MicroblogGuiConstants.URL_REFRESH));
		result.add(new ServletInfo(microblogServlet, MicroblogGuiConstants.URL_SLASH));
		result.add(new ServletInfo(microblogGuiSendPostServlet, MicroblogGuiConstants.URL_POST_SEND));
		result.add(new ServletInfo(microblogGuiSendConversationServlet, MicroblogGuiConstants.URL_CONVERSATION_SEND));
		return result;
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<ServiceInfo>(super.getServiceInfos());
		result.add(new ServiceInfo(DashboardContentWidget.class, microblogDashboardWidget, microblogDashboardWidget.getClass().getName()));
		return result;
	}

}
