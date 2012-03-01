package de.benjaminborbe.dhl.util;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.StringUtil;

@Singleton
public class DhlStatusNotifierImpl implements DhlStatusNotifier {

	private static final int SUBJECT_MAX_LENGTH = 80;

	private final Logger logger;

	private final MailService mailService;

	private final CalendarUtil calendarUtil;

	private final StringUtil stringUtil;

	@Inject
	public DhlStatusNotifierImpl(final Logger logger, final MailService mailService, final CalendarUtil calendarUtil, final StringUtil stringUtil) {
		this.logger = logger;
		this.mailService = mailService;
		this.calendarUtil = calendarUtil;
		this.stringUtil = stringUtil;
	}

	@Override
	public void mailUpdate(final DhlStatus status) throws MailSendException {
		logger.info("new status " + status);

		final Mail mail = buildMail(status);
		mailService.send(mail);
	}

	protected Mail buildMail(final DhlStatus status) {
		final StringBuffer mailContent = new StringBuffer();
		mailContent.append("Date: ");
		mailContent.append(calendarUtil.toDateTimeString(status.getCalendar()));
		mailContent.append("\n");
		mailContent.append("Place: ");
		mailContent.append(status.getPlace());
		mailContent.append("\n");
		mailContent.append("Message: ");
		mailContent.append(status.getMessage());
		mailContent.append("\n");
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "DHL: " + stringUtil.shorten(calendarUtil.toDateTimeString(status.getCalendar()) + " - " + status.getMessage(), SUBJECT_MAX_LENGTH);
		return new Mail(from, to, subject, mailContent.toString(), "text/plain");
	}
}
