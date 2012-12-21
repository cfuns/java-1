package de.benjaminborbe.projectile.gui.servlet;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.projectile.api.ProjectileService;
import de.benjaminborbe.projectile.api.ProjectileServiceException;
import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.projectile.gui.util.ProjectileSlacktimeReportComparator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.servlet.WebsiteHtmlServlet;
import de.benjaminborbe.website.table.TableCellHeadWidget;
import de.benjaminborbe.website.table.TableCellWidget;
import de.benjaminborbe.website.table.TableRowWidget;
import de.benjaminborbe.website.table.TableWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.JavascriptResourceImpl;
import de.benjaminborbe.website.util.ListWidget;

@Singleton
public class ProjectileGuiReportAllServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 8865908885832843737L;

	private static final String TITLE = "Projectile - Report Extern/Intern";

	private final ProjectileService projectileService;

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final ComparatorUtil comparatorUtil;

	@Inject
	public ProjectileGuiReportAllServlet(
			final Logger logger,
			final ComparatorUtil comparatorUtil,
			final CalendarUtil calendarUtil,
			final TimeZoneUtil timeZoneUtil,
			final ParseUtil parseUtil,
			final NavigationWidget navigationWidget,
			final AuthenticationService authenticationService,
			final AuthorizationService authorizationService,
			final Provider<HttpContext> httpContextProvider,
			final UrlUtil urlUtil,
			final ProjectileService projectileService) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil);
		this.comparatorUtil = comparatorUtil;
		this.projectileService = projectileService;
		this.logger = logger;
		this.authenticationService = authenticationService;
	}

	@Override
	public boolean isAdminRequired() {
		return false;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
			PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final Collection<ProjectileSlacktimeReport> reports = projectileService.getSlacktimeReportAll(sessionIdentifier);
			final List<ProjectileSlacktimeReport> reportList = comparatorUtil.sort(reports, new ProjectileSlacktimeReportComparator());

			final TableWidget table = new TableWidget();
			table.addClass("sortable");
			{
				final TableRowWidget row = new TableRowWidget();
				row.addCell(new TableCellHeadWidget("Name"));
				row.addCell(new TableCellHeadWidget("Week %"));
				row.addCell(new TableCellHeadWidget("Week Extern"));
				row.addCell(new TableCellHeadWidget("Week Intern"));
				row.addCell(new TableCellHeadWidget("Month %"));
				row.addCell(new TableCellHeadWidget("Month Extern"));
				row.addCell(new TableCellHeadWidget("Month Intern"));
				row.addCell(new TableCellHeadWidget("Year %"));
				row.addCell(new TableCellHeadWidget("Year Extern"));
				row.addCell(new TableCellHeadWidget("Year Intern"));
				table.addRow(row);
			}
			for (final ProjectileSlacktimeReport report : reportList) {
				if (hasData(report)) {
					final TableRowWidget row = new TableRowWidget();
					row.addCell(report.getUsername());
					addCells(row, report.getWeekIntern(), report.getWeekExtern());
					addCells(row, report.getMonthIntern(), report.getMonthExtern());
					addCells(row, report.getYearIntern(), report.getYearExtern());
					table.addRow(row);
				}
			}
			widgets.add(table);

			return widgets;
		}
		catch (final ProjectileServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

	private boolean hasData(final ProjectileSlacktimeReport report) {
		return report.getWeekIntern() != null && report.getWeekIntern() > 0 || report.getWeekExtern() != null && report.getWeekExtern() > 0 || report.getMonthIntern() != null
				&& report.getMonthIntern() > 0 || report.getMonthExtern() != null && report.getMonthExtern() > 0 || report.getYearIntern() != null && report.getYearIntern() > 0
				|| report.getYearExtern() != null && report.getYearExtern() > 0;
	}

	private void addCells(final TableRowWidget row, final Double intern, final Double extern) {
		if (extern != null && intern != null && (extern > 0 || intern > 0)) {
			final double total = extern + intern;
			final double procent = (extern / total);
			final DecimalFormat df = new DecimalFormat("#####0.0");
			final DecimalFormat dfHour = new DecimalFormat("#####0.0h");
			final DecimalFormat dfPercent = new DecimalFormat("#####0.0%");

			{
				final TableCellWidget cell = new TableCellWidget(dfPercent.format(procent));
				cell.addAttribute("sorttable_customkey", df.format(procent));
				row.addCell(cell);
			}
			{
				final TableCellWidget cell = new TableCellWidget(dfHour.format(extern));
				cell.addAttribute("sorttable_customkey", df.format(extern));
				row.addCell(cell);
			}
			{
				final TableCellWidget cell = new TableCellWidget(dfHour.format(intern));
				cell.addAttribute("sorttable_customkey", df.format(intern));
				row.addCell(cell);
			}
		}
		else {
			row.addCell("-");
			row.addCell("-");
			row.addCell("-");
		}
	}

	@Override
	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}
}
