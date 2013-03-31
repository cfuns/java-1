package de.benjaminborbe.task.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

import java.util.Collection;

public interface TaskServiceTaskAttachment {

	TaskAttachmentIdentifier addAttachment(SessionIdentifier sessionIdentifier, TaskAttachment taskAttachment) throws LoginRequiredException, PermissionDeniedException, ValidationException, TaskServiceException;

	Collection<TaskAttachmentIdentifier> getAttachmentIdentifiers(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws LoginRequiredException, PermissionDeniedException, TaskServiceException;

	Collection<TaskAttachment> getAttachments(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier) throws LoginRequiredException, PermissionDeniedException, TaskServiceException;

	void deleteAttachment(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier, TaskAttachmentIdentifier taskAttachmentIdentifier) throws LoginRequiredException, PermissionDeniedException, TaskServiceException;

	TaskAttachment getAttachment(SessionIdentifier sessionIdentifier, TaskIdentifier taskIdentifier, TaskAttachmentIdentifier taskAttachmentIdentifier) throws LoginRequiredException, PermissionDeniedException, TaskServiceException;

}
