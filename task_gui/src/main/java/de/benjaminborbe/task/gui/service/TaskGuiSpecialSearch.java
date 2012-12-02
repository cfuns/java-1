package de.benjaminborbe.task.gui.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.html.api.HttpContext;
import de.benjaminborbe.search.api.SearchSpecial;
import de.benjaminborbe.task.api.TaskMatch;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.tools.search.SearchUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;

@Singleton
public class TaskGuiSpecialSearch implements SearchSpecial {

	private static final String ADD = "add";

	private static final List<String> NAMES = Arrays.asList("t", "task");

	private final static String PARAMETER_SEARCH = "q";

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final SearchUtil searchUtil;

	private final Logger logger;

	@Inject
	public TaskGuiSpecialSearch(
			final Logger logger,
			final AuthenticationService authenticationService,
			final TaskService taskService,
			final TaskGuiLinkFactory taskGuiLinkFactory,
			final SearchUtil searchUtil) {
		this.logger = logger;
		this.authenticationService = authenticationService;
		this.taskService = taskService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.searchUtil = searchUtil;
	}

	@Override
	public Collection<String> getNames() {
		return NAMES;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final String searchQuery = request.getParameter(PARAMETER_SEARCH);
			final String term = searchQuery.substring(searchQuery.indexOf(":") + 1).trim();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			if (term.indexOf(ADD) == 0) {
				final String taskName = term.substring(ADD.length());
				taskService.createTask(sessionIdentifier, taskName, null, null, null, null, null, null, null, null);
				response.sendRedirect(taskGuiLinkFactory.tasksNextUrl(request));
				return;
			}
			final String[] words = searchUtil.buildSearchParts(term);
			final List<TaskMatch> tasks = taskService.searchTasks(sessionIdentifier, 1, words);
			if (tasks.size() > 0) {
				response.sendRedirect(taskGuiLinkFactory.taskViewUrl(request, tasks.get(0).getTask()));
				return;
			}
			else {
				final ListWidget widgets = new ListWidget();
				widgets.add(new H2Widget("TaskSearch"));
				widgets.add("no match");
				widgets.add(new BrWidget());
				widgets.add(taskGuiLinkFactory.tasksNext(request));
				widgets.render(request, response, context);
			}
		}
		catch (final TaskServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
		catch (final LoginRequiredException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
		catch (final PermissionDeniedException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
		catch (final ValidationException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
		catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}
}
