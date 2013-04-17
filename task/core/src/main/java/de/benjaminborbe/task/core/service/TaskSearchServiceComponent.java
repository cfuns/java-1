package de.benjaminborbe.task.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskMatch;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.api.TaskServiceException;
import de.benjaminborbe.tools.search.SearchUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Singleton
public class TaskSearchServiceComponent implements SearchServiceComponent {

	private static final String SEARCH_TYPE = "Task";

	private final Logger logger;

	private final TaskService taskService;

	private final SearchUtil searchUtil;

	@Inject
	public TaskSearchServiceComponent(final Logger logger, final TaskService taskService, final SearchUtil searchUtil) {
		this.logger = logger;
		this.taskService = taskService;
		this.searchUtil = searchUtil;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults) {
		final List<String> words = searchUtil.buildSearchParts(query);
		logger.trace("search: queryString: " + StringUtils.join(words, ",") + " maxResults: " + maxResults);
		final List<SearchResult> results = new ArrayList<>();
		try {
			final List<TaskMatch> tasks = taskService.searchTasks(sessionIdentifier, maxResults, words);
			final int max = Math.min(maxResults, tasks.size());
			for (int i = 0; i < max; ++i) {
				try {
					results.add(mapTask(tasks.get(i)));
				} catch (final MalformedURLException e) {
					logger.error(e.getClass().getName(), e);
				}
			}
			logger.trace("search found " + results.size() + " tasks");
		} catch (final TaskServiceException | PermissionDeniedException | LoginRequiredException e) {
			logger.trace(e.getClass().getName(), e);
		}
		return results;
	}

	protected SearchResult mapTask(final TaskMatch taskMatch) throws MalformedURLException {
		final Task task = taskMatch.getTask();
		return new SearchResultImpl(SEARCH_TYPE, taskMatch.getMatchCounter(), task.getName(), buildUrl(task), task.getDescription());
	}

	private String buildUrl(final Task task) {
		return "/task/task/view?task_id=" + task.getId();
	}

	@Override
	public String getName() {
		return SEARCH_TYPE;
	}

	@Override
	public Collection<String> getAliases() {
		return Arrays.asList("task");
	}
}
