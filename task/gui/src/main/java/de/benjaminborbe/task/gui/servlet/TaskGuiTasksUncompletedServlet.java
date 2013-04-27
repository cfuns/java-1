package de.benjaminborbe.task.gui.servlet;

import com.google.inject.Provider;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.cache.api.CacheService;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.html.api.Widget;
import de.benjaminborbe.navigation.api.NavigationWidget;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.util.TaskComparator;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.task.gui.util.TaskGuiWidgetFactory;
import de.benjaminborbe.task.gui.widget.TaskCache;
import de.benjaminborbe.task.gui.widget.TaskGuiSwitchWidget;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.ListWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

@Singleton
public class TaskGuiTasksUncompletedServlet extends TaskGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 1328676176772634649L;

	private static final String TITLE = "Tasks";

	private final Logger logger;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final TaskGuiWidgetFactory taskGuiWidgetFactory;

	private final TaskGuiUtil taskGuiUtil;

	private final TaskGuiSwitchWidget taskGuiSwitchWidget;

	private final ComparatorUtil comparatorUtil;

	private final TaskComparator taskComparator;

	private final Provider<TaskCache> taskCacheProvider;

	@Inject
	public TaskGuiTasksUncompletedServlet(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final AuthenticationService authenticationService,
		final NavigationWidget navigationWidget,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final AuthorizationService authorizationService,
		final TaskGuiLinkFactory taskGuiLinkFactory,
		final TaskGuiWidgetFactory taskGuiWidgetFactory,
		final TaskGuiUtil taskGuiUtil,
		final TaskGuiSwitchWidget taskGuiSwitchWidget,
		final ComparatorUtil comparatorUtil,
		final TaskComparator taskComparator,
		final Provider<TaskCache> taskCacheProvider,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, taskGuiUtil, cacheService);
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.taskGuiWidgetFactory = taskGuiWidgetFactory;
		this.taskGuiUtil = taskGuiUtil;
		this.taskGuiSwitchWidget = taskGuiSwitchWidget;
		this.comparatorUtil = comparatorUtil;
		this.taskComparator = taskComparator;
		this.taskCacheProvider = taskCacheProvider;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected Widget createTaskContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {
		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			widgets.add(taskGuiSwitchWidget);

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final List<String> taskContextIds = taskGuiUtil.getSelectedTaskContextIds(request);

			final Collection<Task> allTasks = taskGuiUtil.getTasks(sessionIdentifier, false, taskContextIds);
			final TaskCache taskCache = taskCacheProvider.get();
			taskCache.addAll(allTasks);

			final List<Task> tasks = comparatorUtil.sort(allTasks, taskComparator);
			final TimeZone timeZone = authenticationService.getTimeZone(sessionIdentifier);
			logger.trace("found " + tasks.size() + " tasks");
			widgets.add(taskGuiWidgetFactory.taskListWithChilds(sessionIdentifier, taskCache, null, request, timeZone));

			final ListWidget links = new ListWidget();
			links.add(taskGuiLinkFactory.tasksNext(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.taskCreate(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.tasksCompleted(request));
			links.add(" ");
			links.add(taskGuiLinkFactory.taskContextList(request));
			widgets.add(links);
			return widgets;
		} catch (final AuthenticationServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		} catch (final TaskServiceException e) {
			logger.trace(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			return widget;
		}
	}

}
