package de.benjaminborbe.task.gui.util;

import com.google.inject.Inject;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.AuthorizationServiceException;
import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.task.api.TaskService;
import de.benjaminborbe.task.gui.TaskGuiConstants;

public class TaskGuiNavigationEntry implements NavigationEntry {

	private final AuthorizationService authorizationService;

	@Inject
	public TaskGuiNavigationEntry(final AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Override
	public String getTitle() {
		return "Task";
	}

	@Override
	public String getURL() {
		return "/" + TaskGuiConstants.NAME + TaskGuiConstants.URL_TASKS_NEXT;
	}

	@Override
	public boolean isVisible(final SessionIdentifier sessionIdentifier) {
		try {
			final PermissionIdentifier permissionIdentifier = authorizationService.createPermissionIdentifier(TaskService.PERMISSION);
			return authorizationService.hasPermission(sessionIdentifier, permissionIdentifier);
		} catch (final AuthorizationServiceException e) {
			return false;
		}
	}

}
