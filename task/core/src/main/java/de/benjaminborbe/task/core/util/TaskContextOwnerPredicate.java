package de.benjaminborbe.task.core.util;

import com.google.common.base.Predicate;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.core.dao.context.TaskContextBean;

public class TaskContextOwnerPredicate implements Predicate<TaskContextBean> {

	private final UserIdentifier userIdentifier;

	public TaskContextOwnerPredicate(final UserIdentifier userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	@Override
	public boolean apply(final TaskContextBean task) {
		return userIdentifier != null && task != null && userIdentifier.equals(task.getOwner());
	}
}
