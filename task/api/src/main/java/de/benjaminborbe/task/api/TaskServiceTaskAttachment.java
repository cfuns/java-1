package de.benjaminborbe.task.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.util.Collection;

public interface TaskServiceTaskAttachment {

	TaskAttachmentIdentifier createTaskAttachmentIdentifier(String id) throws TaskServiceException;

	TaskAttachmentIdentifier addAttachment(TaskAttachment taskAttachment) throws LoginRequiredException, PermissionDeniedException, ValidationException, TaskServiceException;

	Collection<TaskAttachmentIdentifier> getAttachmentIdentifiers(TaskIdentifier taskIdentifier) throws LoginRequiredException, PermissionDeniedException, TaskServiceException;

	Collection<TaskAttachment> getAttachments(TaskIdentifier taskIdentifier) throws LoginRequiredException, PermissionDeniedException, TaskServiceException;

	void deleteAttachment(SessionIdentifier sessionIdentifier, TaskAttachmentIdentifier taskAttachmentIdentifier) throws LoginRequiredException, PermissionDeniedException, TaskServiceException;

	TaskAttachment getAttachment(SessionIdentifier sessionIdentifier, TaskAttachmentIdentifier taskAttachmentIdentifier) throws LoginRequiredException, PermissionDeniedException, TaskServiceException;

}
