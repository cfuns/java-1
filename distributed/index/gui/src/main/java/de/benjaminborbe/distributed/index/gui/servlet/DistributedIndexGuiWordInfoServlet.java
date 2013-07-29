package de.benjaminborbe.distributed.index.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;
import de.benjaminborbe.distributed.index.gui.DistributedIndexGuiConstants;
import de.benjaminborbe.distributed.index.gui.util.DistributedIndexGuiLinkFactory;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.JavascriptResource;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.form.FormInputSubmitWidget;
import de.benjaminborbe.website.form.FormInputTextWidget;
import de.benjaminborbe.website.form.FormWidget;
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
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@Singleton
public class DistributedIndexGuiWordInfoServlet extends WebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "DistributedIndex - Word-Info";

	private final DistributedIndexService distributedIndexService;

	private final Logger logger;

	private final DistributedIndexGuiLinkFactory distributedIndexGuiLinkFactory;

	@Inject
	public DistributedIndexGuiWordInfoServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final DistributedIndexService distributedIndexService,
		final DistributedIndexGuiLinkFactory distributedIndexGuiLinkFactory,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, cacheService);
		this.distributedIndexService = distributedIndexService;
		this.logger = logger;
		this.distributedIndexGuiLinkFactory = distributedIndexGuiLinkFactory;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createContentWidget(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final String index = request.getParameter(DistributedIndexGuiConstants.PARAMETER_INDEX);
			final String word = request.getParameter(DistributedIndexGuiConstants.PARAMETER_WORD);
			if (index != null && word != null) {

				widgets.add("Index: " + index);
				widgets.add(new BrWidget());
				widgets.add("Word: " + word);
				widgets.add(new BrWidget());

				final Map<String, Integer> data = distributedIndexService.getEntryRatingForWord(index, word.toLowerCase());

				final TableWidget table = new TableWidget();
				table.addClass("sortable");
				{
					final TableRowWidget row = new TableRowWidget();
					row.addCell(new TableCellHeadWidget("Url"));
					row.addCell(new TableCellHeadWidget("Rating"));
					table.addRow(row);
				}
				for (final Entry<String, Integer> e : data.entrySet()) {
					final TableRowWidget row = new TableRowWidget();
					final String url = e.getKey();
					final String rating = toString(e.getValue());
					{
						final TableCellWidget cell = new TableCellWidget(distributedIndexGuiLinkFactory.entryInfo(request, url, index, url));
						cell.addAttribute("sorttable_customkey", url);
						row.addCell(cell);
					}
					{
						final TableCellWidget cell = new TableCellWidget(distributedIndexGuiLinkFactory.entryInfo(request, rating, index, url));
						cell.addAttribute("sorttable_customkey", rating);
						row.addCell(cell);
					}
					table.addRow(row);
				}
				widgets.add(table);

			}

			final FormWidget formWidget = new FormWidget();
			formWidget.addFormInputWidget(new FormInputTextWidget(DistributedIndexGuiConstants.PARAMETER_INDEX).addLabel("Index"));
			formWidget.addFormInputWidget(new FormInputTextWidget(DistributedIndexGuiConstants.PARAMETER_WORD).addLabel("Word"));
			formWidget.addFormInputWidget(new FormInputSubmitWidget("show"));
			widgets.add(formWidget);

			return widgets;
		} catch (final DistributedIndexServiceException e) {
			logger.debug(e.getClass().getName(), e);
			return new ExceptionWidget(e);
		}
	}

	private String toString(final Integer value) {
		return value != null ? String.valueOf(value) : "~";
	}

	@Override
	protected List<JavascriptResource> getJavascriptResources(final HttpServletRequest request, final HttpServletResponse response) {
		final String contextPath = request.getContextPath();
		final List<JavascriptResource> result = new ArrayList<JavascriptResource>();
		result.add(new JavascriptResourceImpl(contextPath + "/js/sorttable.js"));
		return result;
	}
}
