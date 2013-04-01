package de.benjaminborbe.task.core.util;

import com.google.common.base.Predicate;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.core.dao.task.TaskBean;

public class TaskOwnerPredicate implements Predicate<TaskBean> {

	private final UserIdentifier userIdentifier;

	public TaskOwnerPredicate(final UserIdentifier userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	@Override
	public boolean apply(final TaskBean task) {
		return userIdentifier != null && task != null && userIdentifier.equals(task.getOwner());
	}
}
