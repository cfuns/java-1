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
import de.benjaminborbe.task.api.TaskAttachment;
import de.benjaminborbe.task.api.TaskContext;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.TaskGuiConstants;
import de.benjaminborbe.task.gui.util.TaskComparator;
import de.benjaminborbe.task.gui.util.TaskContextComparator;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.task.gui.util.TaskGuiWidgetFactory;
import de.benjaminborbe.task.gui.widget.TaskCache;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ComparatorUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.website.link.LinkWidget;
import de.benjaminborbe.website.servlet.RedirectException;
import de.benjaminborbe.website.util.DivWidget;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H1Widget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.HtmlContentWidget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.util.PreWidget;
import de.benjaminborbe.website.util.RedirectWidget;
import de.benjaminborbe.website.util.SpanWidget;
import de.benjaminborbe.website.util.Target;
import de.benjaminborbe.website.util.UlWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Singleton
public class TaskGuiTaskViewServlet extends TaskGuiWebsiteHtmlServlet {

	private static final long serialVersionUID = 4130890931474576938L;

	private static final String TITLE = "Task - View";

	private final Logger logger;

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final HtmlUtil htmlUtil;

	private final CalendarUtil calendarUtil;

	private final TaskGuiWidgetFactory taskGuiWidgetFactory;

	private final ComparatorUtil comparatorUtil;

	private final TaskComparator taskComparator;

	private final Provider<TaskCache> taskCacheProvider;

	private final TaskGuiUtil taskGuiUtil;

	@Inject
	public TaskGuiTaskViewServlet(
		final Logger logger,
		final TaskGuiWidgetFactory taskGuiWidgetFactory,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final ParseUtil parseUtil,
		final NavigationWidget navigationWidget,
		final AuthenticationService authenticationService,
		final AuthorizationService authorizationService,
		final Provider<HttpContext> httpContextProvider,
		final UrlUtil urlUtil,
		final HtmlUtil htmlUtil,
		final TaskService taskService,
		final TaskGuiUtil taskGuiUtil,
		final TaskGuiLinkFactory taskGuiLinkFactory,
		final ComparatorUtil comparatorUtil,
		final TaskComparator taskComparator,
		final Provider<TaskCache> taskCacheProvider,
		final CacheService cacheService
	) {
		super(logger, calendarUtil, timeZoneUtil, parseUtil, navigationWidget, authenticationService, authorizationService, httpContextProvider, urlUtil, taskGuiUtil, cacheService);
		this.logger = logger;
		this.taskGuiWidgetFactory = taskGuiWidgetFactory;
		this.htmlUtil = htmlUtil;
		this.taskService = taskService;
		this.authenticationService = authenticationService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.calendarUtil = calendarUtil;
		this.comparatorUtil = comparatorUtil;
		this.taskComparator = taskComparator;
		this.taskGuiUtil = taskGuiUtil;
		this.taskCacheProvider = taskCacheProvider;
	}

	@Override
	protected Widget createTaskContentWidget(final HttpServletRequest request) throws IOException,
		PermissionDeniedException, RedirectException, LoginRequiredException {

		try {
			logger.trace("printContent");
			final ListWidget widgets = new ListWidget();
			widgets.add(new H1Widget(getTitle()));

			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			final TaskIdentifier taskIdentifier = taskService.createTaskIdentifier(request.getParameter(TaskGuiConstants.PARAMETER_TASK_ID));
			final TaskCache taskCache = taskCacheProvider.get();
			final Task task = taskCache.get(sessionIdentifier, taskIdentifier);
			if (task == null) {
				return new RedirectWidget(taskGuiLinkFactory.tasksNextUrl(request));
			}
			{
				final ListWidget tasksWidget = new ListWidget();
				addTask(tasksWidget, sessionIdentifier, task, taskCache, request);
				widgets.add(new DivWidget(tasksWidget));
			}
			{
				final ListWidget links = new ListWidget();
				links.add(taskGuiLinkFactory.tasksNext(request));
				links.add(" ");
				links.add(taskGuiLinkFactory.tasksUncompleted(request));
				links.add(" ");
				links.add(taskGuiLinkFactory.taskCreate(request));
				links.add(" ");
				links.add(taskGuiLinkFactory.taskContextList(request));
				widgets.add(new DivWidget(links));
			}

			return new DivWidget(widgets).addClass("taskView");
		} catch (final AuthenticationServiceException e) {
			return new ExceptionWidget(e);
		} catch (TaskServiceException e) {
			return new ExceptionWidget(e);
		}
	}

	private void addTask(
		final ListWidget widgets, final SessionIdentifier sessionIdentifier, final TaskIdentifier taskIdentifier, final TaskCache taskCache,
		final HttpServletRequest request
	) throws TaskServiceException, LoginRequiredException, PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {
		final Task task = taskCache.get(sessionIdentifier, taskIdentifier);
		addTask(widgets, sessionIdentifier, task, taskCache, request);
	}

	private void addTask(
		final ListWidget widgets,
		final SessionIdentifier sessionIdentifier,
		final Task task,
		final TaskCache taskCache,
		final HttpServletRequest request
	)
		throws TaskServiceException, LoginRequiredException, PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {
		if (task.getParentId() != null) {
			addTask(widgets, sessionIdentifier, task.getParentId(), taskCache, request);
		} else {
			addChilds(widgets, sessionIdentifier, task, request, null, null, taskCache);
		}
	}

	private void addChilds(
		final ListWidget widgets, final SessionIdentifier sessionIdentifier, final Task task, final HttpServletRequest request, final Task previousTask,
		final Task nextTask, final TaskCache taskCache
	) throws TaskServiceException, LoginRequiredException, PermissionDeniedException, MalformedURLException,
		UnsupportedEncodingException {
		final List<Task> childTasks = comparatorUtil.sort(taskService.getTaskChilds(sessionIdentifier, task.getId()), taskComparator);
		addTaskEntry(widgets, sessionIdentifier, task, request, hasNotCompleted(childTasks), nextTask, previousTask, taskCache);

		if (childTasks.size() > 0) {
			for (int i = 0; i < childTasks.size(); ++i) {
				final Task childPreviousTask = getTask(childTasks, i + 1);
				final Task childNextTask = getTask(childTasks, i - 1);
				final Task childTask = childTasks.get(i);
				addChilds(widgets, sessionIdentifier, childTask, request, childPreviousTask, childNextTask, taskCache);
			}
		}
	}

	private Task getTask(final List<Task> tasks, final int pos) {
		if (pos < 0 || pos >= tasks.size()) {
			return null;
		}
		return tasks.get(pos);
	}

	private boolean hasNotCompleted(final Collection<Task> tasks) {
		for (final Task task : tasks) {
			if (Boolean.FALSE.equals(task.getCompleted()) || task.getCompleted() == null) {
				return false;
			}
		}
		return true;
	}

	private void addTaskEntry(
		final ListWidget widgets, final SessionIdentifier sessionIdentifier, final Task task, final HttpServletRequest request,
		final boolean hasNotCompletedChilds, final Task previousTask, final Task nextTask, final TaskCache taskCache
	) throws TaskServiceException, LoginRequiredException,
		PermissionDeniedException, MalformedURLException, UnsupportedEncodingException {

		final String taskName = taskGuiUtil.buildCompleteName(sessionIdentifier, taskCache, task, TaskGuiConstants.PARENT_NAME_LENGTH);
		final H2Widget title = new H2Widget(taskName);
		if (Boolean.TRUE.equals(task.getCompleted())) {
			title.addAttribute("class", "completed");
		}
		widgets.add(title);

		// options
		{
			final ListWidget options = new ListWidget();

			if (previousTask != null) {
				options.add(taskGuiLinkFactory.taskPrioFirst(request, taskGuiWidgetFactory.buildImage(request, "first"), task.getId()));
				options.add(" ");
				options.add(taskGuiLinkFactory.taskPrioSwap(request, taskGuiWidgetFactory.buildImage(request, "up"), task.getId(), previousTask.getId()));
				options.add(" ");
			}
			if (nextTask != null) {
				options.add(taskGuiLinkFactory.taskPrioSwap(request, taskGuiWidgetFactory.buildImage(request, "down"), task.getId(), nextTask.getId()));
				options.add(" ");
				options.add(taskGuiLinkFactory.taskPrioLast(request, taskGuiWidgetFactory.buildImage(request, "last"), task.getId()));
				options.add(" ");
			}

			if (Boolean.TRUE.equals(task.getCompleted())) {
				options.add(taskGuiLinkFactory.taskUncomplete(request, task));
				options.add(" ");
			} else {
				if (hasNotCompletedChilds) {
					options.add(taskGuiLinkFactory.taskComplete(request, taskGuiWidgetFactory.buildImage(request, "complete"), task));
					options.add(" ");
					options.add(taskGuiLinkFactory.taskDelete(request, taskGuiWidgetFactory.buildImage(request, "delete"), task));
					options.add(" ");
				}
			}
			options.add(taskGuiLinkFactory.taskUpdate(request, taskGuiWidgetFactory.buildImage(request, "update"), task));
			options.add(" ");
			options.add(taskGuiLinkFactory.taskCreateSubTask(request, taskGuiWidgetFactory.buildImage(request, "subtask"), task.getId()));
			options.add(" ");
			options.add(taskGuiLinkFactory.taskAttachmentCreate(request, task.getId(), taskGuiWidgetFactory.buildImage(request, "upload")));

			widgets.add(new DivWidget(options).addClass("options"));
		}

		// focus
		{
			final ListWidget focusList = new ListWidget();
			focusList.add("Focus: ");
			for (final TaskFocus taskFocus : TaskFocus.values()) {
				focusList.add(taskGuiLinkFactory.taskUpdateFocus(request, task, taskFocus, taskFocus.name().toLowerCase()));
				focusList.add(" ");
			}
			widgets.add(new DivWidget(focusList).addClass("focusList"));
		}

		// taskContext
		{
			final List<TaskContext> taskContexts = new ArrayList<TaskContext>(taskService.getTaskContexts(sessionIdentifier));
			Collections.sort(taskContexts, new TaskContextComparator());
			final ListWidget contextList = new ListWidget();
			contextList.add("Context: ");
			{
				final Widget name;
				if (task.getContext() == null) {
					name = new SpanWidget("none").addClass("taskContextSelected");
				} else {
					name = new SpanWidget("none").addClass("taskContextNotSelected");
				}
				contextList.add(taskGuiLinkFactory.taskSelectTaskContext(request, task.getId(), null, name));
				contextList.add(" ");
			}
			for (final TaskContext taskContext : taskContexts) {
				final Widget name;
				if (taskContext.getId().equals(task.getContext())) {
					name = new SpanWidget(taskContext.getName()).addClass("taskContextSelected");
				} else {
					name = new SpanWidget(taskContext.getName()).addClass("taskContextNotSelected");
				}
				contextList.add(taskGuiLinkFactory.taskSelectTaskContext(request, task.getId(), taskContext.getId(), name));
				contextList.add(" ");
			}

			widgets.add(new DivWidget(contextList).addClass("contextList"));
		}

		// url
		{
			if (task.getUrl() != null && task.getUrl().length() > 0) {
				final ListWidget urlWidget = new ListWidget();
				urlWidget.add("Url: ");
				urlWidget.add(new LinkWidget(task.getUrl(), task.getUrl()).addTarget(Target.BLANK));
				widgets.add(new DivWidget(urlWidget));
			}
		}

		// start
		{
			if (task.getStart() != null) {
				widgets.add(new DivWidget("Start: " + calendarUtil.toDateTimeString(task.getStart()) + " " + calendarUtil.getWeekday(task.getStart())
					+ (task.getRepeatStart() != null ? " (repeat in + " + task.getRepeatStart() + " days)" : "")));
			}
		}

		// due
		{
			if (task.getDue() != null) {
				widgets.add(new DivWidget("Due: " + calendarUtil.toDateTimeString(task.getDue()) + " " + calendarUtil.getWeekday(task.getDue())
					+ (task.getRepeatDue() != null ? " (repeat in + " + task.getRepeatDue() + " days)" : "")));
			}
		}

		// description
		{
			if (task.getDescription() != null) {
				widgets.add(new PreWidget(buildDescription(task.getDescription())));
			}
		}

		// attachments
		{
			final Collection<TaskAttachment> attachments = taskService.getAttachments(sessionIdentifier, task.getId());
			if (!attachments.isEmpty()) {
				widgets.add("Attachments:");
				widgets.add(new BrWidget());
				final UlWidget ul = new UlWidget();
				for (final TaskAttachment attachment : attachments) {
					ul.add(taskGuiLinkFactory.taskAttachmentDownload(request, attachment));
				}
				widgets.add(ul);
			}
		}
	}

	private Widget buildDescription(final String description) {
		return new HtmlContentWidget(htmlUtil.addLinks(htmlUtil.escapeHtml(description)));
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

}
