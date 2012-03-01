package de.benjaminborbe.dhl.util;

import de.benjaminborbe.mail.api.MailSendException;

public interface DhlStatusNotifier {

	void mailUpdate(DhlStatus status) throws MailSendException;
}
