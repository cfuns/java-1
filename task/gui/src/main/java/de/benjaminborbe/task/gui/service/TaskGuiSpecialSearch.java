package de.benjaminborbe.task.gui.service;

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
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.api.TaskMatch;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.task.gui.util.TaskGuiLinkFactory;
import de.benjaminborbe.task.gui.util.TaskGuiUtil;
import de.benjaminborbe.tools.search.SearchUtil;
import de.benjaminborbe.website.util.ExceptionWidget;
import de.benjaminborbe.website.util.H2Widget;
import de.benjaminborbe.website.util.ListWidget;
import de.benjaminborbe.website.widget.BrWidget;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Singleton
public class TaskGuiSpecialSearch implements SearchSpecial {

	private static final String ADD = "add";

	private static final List<String> NAMES = Arrays.asList("t");

	private final static String PARAMETER_SEARCH = "q";

	private static final String NEXT = "next";

	private final TaskService taskService;

	private final AuthenticationService authenticationService;

	private final TaskGuiLinkFactory taskGuiLinkFactory;

	private final SearchUtil searchUtil;

	private final Logger logger;

	private final TaskGuiUtil taskGuiUtil;

	@Inject
	public TaskGuiSpecialSearch(
		final Logger logger,
		final TaskGuiUtil taskGuiUtil,
		final AuthenticationService authenticationService,
		final TaskService taskService,
		final TaskGuiLinkFactory taskGuiLinkFactory,
		final SearchUtil searchUtil) {
		this.logger = logger;
		this.taskGuiUtil = taskGuiUtil;
		this.authenticationService = authenticationService;
		this.taskService = taskService;
		this.taskGuiLinkFactory = taskGuiLinkFactory;
		this.searchUtil = searchUtil;
	}

	@Override
	public Collection<String> getAliases() {
		return NAMES;
	}

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response, final HttpContext context) throws IOException {
		try {
			final String searchQuery = request.getParameter(PARAMETER_SEARCH);
			final String term = searchQuery.substring(searchQuery.indexOf(":") + 1).trim();
			final SessionIdentifier sessionIdentifier = authenticationService.createSessionIdentifier(request);
			if (term.indexOf(ADD) == 0) {
				addTask(request, response, sessionIdentifier, term.substring(ADD.length()).trim());
				return;
			}
			if (term.indexOf(NEXT) == 0) {
				response.sendRedirect(taskGuiLinkFactory.tasksNextUrl(request));
				return;
			}
			final List<String> words = searchUtil.buildSearchParts(term);
			final List<TaskMatch> tasks = taskService.searchTasks(sessionIdentifier, 1, words);
			if (tasks.size() > 0) {
				response.sendRedirect(taskGuiLinkFactory.taskViewUrl(request, tasks.get(0).getTask().getId()));
				return;
			} else {
				final ListWidget widgets = new ListWidget();
				widgets.add(new H2Widget("TaskSearch"));
				widgets.add("no match");
				widgets.add(new BrWidget());
				widgets.add(taskGuiLinkFactory.tasksNext(request));
				widgets.render(request, response, context);
			}
		} catch (final TaskServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		} catch (final LoginRequiredException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		} catch (final PermissionDeniedException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		} catch (final ValidationException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		} catch (final AuthenticationServiceException e) {
			logger.debug(e.getClass().getName(), e);
			final ExceptionWidget widget = new ExceptionWidget(e);
			widget.render(request, response, context);
		}
	}

	private void addTask(final HttpServletRequest request, final HttpServletResponse response, final SessionIdentifier sessionIdentifier, final String input)
		throws TaskServiceException, LoginRequiredException, PermissionDeniedException, ValidationException, IOException, UnsupportedEncodingException {
		final TaskIdentifier taskIdentifier = taskService.createTask(sessionIdentifier, taskGuiUtil.quickStringToTask(sessionIdentifier, input));
		response.sendRedirect(taskGuiLinkFactory.taskViewUrl(request, taskIdentifier));
	}
}
