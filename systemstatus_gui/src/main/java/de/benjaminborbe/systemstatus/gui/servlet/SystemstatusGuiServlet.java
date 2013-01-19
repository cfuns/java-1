package de.benjaminborbe.systemstatus.gui.servlet;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectUtil;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class SystemstatusGuiServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Systemstatus";

	private final Logger logger;

	private final StorageService storageService;

	@Inject
	public SystemstatusGuiServlet(
			final Logger logger,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final AuthenticationService authenticationService,
			final NavigationWidget navigationWidget,
			final Provider<HttpContext> httpContextProvider,
			final RedirectUtil redirectUtil,
			final UrlUtil urlUtil,
			final AuthorizationService authorizationService,
			final StorageService storageService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.logger = logger;
		this.storageService = storageService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException {
		logger.trace("printContent");
		final ListWidget widgets = new ListWidget();
		widgets.add(new H1Widget(getTitle()));

		sessionData(request, widgets);
		memoryState(widgets);
		diskUsage(widgets);
		storageState(widgets);

		return widgets;
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

	private void diskUsage(final ListWidget widgets) {
		final NumberFormat nf = NumberFormat.getNumberInstance();
		final DecimalFormat dfPercent = new DecimalFormat("#####0.0%");
		widgets.add(new H2Widget("Disk-Space"));
		final UlWidget ul = new UlWidget();
		for (final File file : File.listRoots()) {
			final long totalSpace = file.getTotalSpace();
			final long usableSpace = file.getUsableSpace();
			final long freeSpace = file.getFreeSpace();
			final long usedSpace = totalSpace - freeSpace;

			ul.add(file.getAbsolutePath() + " used: " + dfPercent.format(1d * usedSpace / totalSpace) + " " + nf.format(usedSpace / 1024 / 1024) + " MB total: "
					+ nf.format(totalSpace / 1024 / 1024) + " MB usable: " + nf.format(usableSpace / 1024 / 1024) + " MB free: " + nf.format(freeSpace / 1024 / 1024) + " MB");
		}
		widgets.add(ul);
	}

	private void memoryState(final ListWidget widgets) {
		{
			widgets.add(new H2Widget("Memory"));
			widgets.add("Memory state before cleanup: ");
			widgets.add(new BrWidget());
			widgets.add(getMemoryStateMX());
			widgets.add(new BrWidget());
			Runtime.getRuntime().gc();
			widgets.add("Memory state after cleanup: ");
			widgets.add(new BrWidget());
			widgets.add(getMemoryStateMX());
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
			}
			else {
				widgets.add("no data in session");
			}
		}
	}

	protected String getMemoryStateMX() {
		final StringWriter msg = new StringWriter();
		msg.append("");
		final MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
		{
			final MemoryUsage m = mem.getHeapMemoryUsage();
			msg.append("free=");
			msg.append(String.valueOf((m.getMax() - m.getUsed()) / (1024 * 1024)));
			msg.append(", ");
			msg.append("used=");
			msg.append(String.valueOf(m.getUsed() / (1024 * 1024)));
			msg.append(", ");
			msg.append("commited=");
			msg.append(String.valueOf(m.getCommitted() / (1024 * 1024)));
			msg.append(", ");
			msg.append("max=");
			msg.append(String.valueOf(m.getMax() / (1024 * 1024)));
			msg.append(", ");
			msg.append("init=");
			msg.append(String.valueOf(m.getInit() / (1024 * 1024)));
			msg.append(" ");
		}
		{
			final MemoryUsage m = mem.getNonHeapMemoryUsage();
			msg.append("PermGen-free=");
			msg.append(String.valueOf((m.getMax() - m.getUsed()) / (1024 * 1024)));
			msg.append(", ");
			msg.append("PermGen-used=");
			msg.append(String.valueOf(m.getUsed() / (1024 * 1024)));
			msg.append(", ");
			msg.append("PermGen-commited=");
			msg.append(String.valueOf(m.getCommitted() / (1024 * 1024)));
			msg.append(", ");
			msg.append("PermGen-max=");
			msg.append(String.valueOf(m.getMax() / (1024 * 1024)));
			msg.append(", ");
			msg.append("PermGen-init=");
			msg.append(String.valueOf(m.getInit() / (1024 * 1024)));
		}
		return msg.toString();
	}

	protected String getMemoryState() {
		final StringWriter msg = new StringWriter();
		msg.append("used=");
		msg.append(String.valueOf((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)));
		msg.append(", ");
		msg.append("free=");
		msg.append(String.valueOf(Runtime.getRuntime().freeMemory() / (1024 * 1024)));
		msg.append(", ");
		msg.append("total=");
		msg.append(String.valueOf(Runtime.getRuntime().totalMemory() / (1024 * 1024)));
		msg.append(", ");
		msg.append("max=");
		msg.append(String.valueOf(Runtime.getRuntime().maxMemory() / (1024 * 1024)));

		return msg.toString();
	}
}
