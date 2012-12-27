package de.benjaminborbe.dhl.util;

import de.benjaminborbe.mail.api.MailServiceException;

public interface DhlStatusNotifier {

	void mailUpdate(DhlStatus status) throws MailServiceException;
}
