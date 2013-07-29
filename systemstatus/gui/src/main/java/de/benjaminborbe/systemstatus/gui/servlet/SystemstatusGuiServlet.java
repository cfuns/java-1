package de.benjaminborbe.systemstatus.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.systemstatus.api.SystemstatusMemoryUsage;
import de.benjaminborbe.systemstatus.api.SystemstatusPartition;
import de.benjaminborbe.systemstatus.api.SystemstatusService;
import de.benjaminborbe.systemstatus.api.SystemstatusServiceException;
import de.benjaminborbe.systemstatus.gui.SystemstatusGuiConstants;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.NetUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringWriter;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Singleton
public class SystemstatusGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Systemstatus";

	private final Logger logger;

	private final StorageService storageService;

	private final SystemstatusService systemstatusService;

	private final NetUtil netUtil;

	private final ParseUtil parseUtil;

	@Inject
	public SystemstatusGuiServlet(
		final Logger logger,
		final NetUtil netUtil,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final StorageService storageService,
		final SystemstatusService systemstatusService,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.logger = logger;
		this.netUtil = netUtil;
		this.storageService = storageService;
		this.systemstatusService = systemstatusService;
		this.parseUtil = parseUtil;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final boolean withGc = parseUtil.parseBoolean(request.getParameter(SystemstatusGuiConstants.PARAMETER_WITH_GC), false);
			sessionData(request, widgets);
			hostnames(widgets);
			memoryState(widgets, withGc);
			diskUsage(widgets);
			storageState(widgets);

			return widgets;
		} catch (final SystemstatusServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private void hostnames(final ListWidget widgets) {
		widgets.add(new H2Widget("Hostnames"));
		try {
			final UlWidget ul = new UlWidget();
			final List<String> hostnames = new ArrayList<String>(netUtil.getHostnames());
			Collections.sort(hostnames);
			for (final String hostname : hostnames) {
				ul.add(hostname);
			}
			widgets.add(ul);
		} catch (final SocketException e) {
			widgets.add("getHostnames failed!");
			widgets.add(new ExceptionWidget(e));
		}
	}

	private void storageState(final ListWidget widgets) {
		widgets.add(new H2Widget("Storage"));
		final NumberFormat nf = NumberFormat.getNumberInstance();
		final UlWidget ul = new UlWidget();
		ul.add("Is available: " + storageService.isAvailable());
		ul.add("Open Connections: " + nf.format(storageService.getConnections()));
		ul.add("Free Connections: " + nf.format(storageService.getFreeConnections()));
		ul.add("Max Connections: " + nf.format(storageService.getMaxConnections()));
		ul.add("Encoding: " + storageService.getEncoding());
		widgets.add(ul);
	}

	private void diskUsage(final ListWidget widgets) throws SystemstatusServiceException {
		final NumberFormat nf = NumberFormat.getNumberInstance();
		final DecimalFormat dfPercent = new DecimalFormat("#####0.0%");
		widgets.add(new H2Widget("Disk-Space"));
		final UlWidget ul = new UlWidget();

		for (final SystemstatusPartition file : systemstatusService.getPartitions()) {
			final long totalSpace = file.getTotalSpace();
			final long usableSpace = file.getUsableSpace();
			final long freeSpace = file.getFreeSpace();
			final long usedSpace = file.getUsedSpace();
			ul.add(file.getAbsolutePath() + " used: " + dfPercent.format(1d * usedSpace / totalSpace) + " " + nf.format(usedSpace / 1024 / 1024) + " MB total: "
				+ nf.format(totalSpace / 1024 / 1024) + " MB usable: " + nf.format(usableSpace / 1024 / 1024) + " MB free: " + nf.format(freeSpace / 1024 / 1024) + " MB");
		}
		widgets.add(ul);
	}

	private void memoryState(final ListWidget widgets, final boolean withGc) throws SystemstatusServiceException {
		widgets.add(new H2Widget("Memory"));
		if (withGc) {
			widgets.add("Memory state before cleanup: ");
			widgets.add(new BrWidget());
			widgets.add(getMemoryState());
			widgets.add(new BrWidget());
			Runtime.getRuntime().gc();
			widgets.add("Memory state after cleanup: ");
			widgets.add(new BrWidget());
			widgets.add(getMemoryState());
			widgets.add(new BrWidget());
		} else {
			widgets.add(getMemoryState());
			widgets.add(new BrWidget());
		}
	}

	private void sessionData(final HttpServletRequest request, final ListWidget widgets) {
		{
			widgets.add(new H2Widget("Session-Data"));
			final HttpSession session = request.getSession();
			widgets.add("session-id: " + session.getId());
			widgets.add(new BrWidget());
			@SuppressWarnings("unchecked")
			final Enumeration<String> e = session.getAttributeNames();
			if (e.hasMoreElements()) {
				final UlWidget ul = new UlWidget();
				while (e.hasMoreElements()) {
					final String name = e.nextElement();
					ul.add(name + " = " + session.getAttribute(name));
				}
				widgets.add(ul);
			} else {
				widgets.add("no data in session");
			}
		}
	}

	private String getMemoryState() throws SystemstatusServiceException {

		final SystemstatusMemoryUsage memoryUsage = systemstatusService.getMemoryUsage();

		final StringWriter msg = new StringWriter();
		msg.append("");
		{
			msg.append("free=");
			msg.append(String.valueOf((memoryUsage.getHeapMax() - memoryUsage.getHeapUsed()) / (1024 * 1024)));
			msg.append(", ");
			msg.append("used=");
			msg.append(String.valueOf(memoryUsage.getHeapUsed() / (1024 * 1024)));
			// msg.append(", ");
			// msg.append("commited=");
			// msg.append(String.valueOf(m.getCommitted() / (1024 * 1024)));
			msg.append(", ");
			msg.append("max=");
			msg.append(String.valueOf(memoryUsage.getHeapMax() / (1024 * 1024)));
			// msg.append(", ");
			// msg.append("init=");
			// msg.append(String.valueOf(m.getInit() / (1024 * 1024)));
			msg.append(" ");
		}
		{
			msg.append("PermGen-free=");
			msg.append(String.valueOf((memoryUsage.getNonHeapMax() - memoryUsage.getNonHeapUsed()) / (1024 * 1024)));
			msg.append(", ");
			msg.append("PermGen-used=");
			msg.append(String.valueOf(memoryUsage.getNonHeapUsed() / (1024 * 1024)));
			// msg.append(", ");
			// msg.append("PermGen-commited=");
			// msg.append(String.valueOf(m.getCommitted() / (1024 * 1024)));
			msg.append(", ");
			msg.append("PermGen-max=");
			msg.append(String.valueOf(memoryUsage.getNonHeapMax() / (1024 * 1024)));
			// msg.append(", ");
			// msg.append("PermGen-init=");
			// msg.append(String.valueOf(m.getInit() / (1024 * 1024)));
			msg.append(" ");
		}
		return msg.toString();
	}
}
