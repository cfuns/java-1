package de.benjaminborbe.dhl.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.MailDto;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.api.MailServiceException;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class DhlStatusNotifierImpl implements DhlStatusNotifier {

	private final Logger logger;

	private final MailService mailService;

	private final CalendarUtil calendarUtil;

	private final DhlUrlBuilder dhlUrlBuilder;

	@Inject
	public DhlStatusNotifierImpl(final Logger logger, final MailService mailService, final CalendarUtil calendarUtil, final DhlUrlBuilder dhlUrlBuilder) {
		this.logger = logger;
		this.mailService = mailService;
		this.calendarUtil = calendarUtil;
		this.dhlUrlBuilder = dhlUrlBuilder;
	}

	@Override
	public void mailUpdate(final DhlStatus status) throws MailServiceException {
		if (status == null) {
			throw new NullPointerException("parameter DhlStatus is null");
		}
		logger.info("new status " + status);

		final MailDto mail = buildMail(status);
		mailService.send(mail);
	}

	protected MailDto buildMail(final DhlStatus status) {
		final StringBuffer mailContent = new StringBuffer();
		{
			mailContent.append("Date: ");
			mailContent.append(calendarUtil.toDateTimeString(status.getCalendar()));
			mailContent.append("\n");
		}
		{
			mailContent.append("Place: ");
			mailContent.append(status.getPlace());
			mailContent.append("\n");
		}
		{
			mailContent.append("Message: ");
			mailContent.append(status.getMessage());
			mailContent.append("\n");
		}
		{
			try {
				final URL url = dhlUrlBuilder.buildUrl(status.getDhl());
				mailContent.append("Link: ");
				mailContent.append(url);
				mailContent.append("\n");
			}
			catch (final MalformedURLException e) {
				logger.debug("build dhl-link failed!", e);
			}
		}
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "DHL-Status: " + status.getDhl().getTrackingNumber();
		return new MailDto(from, to, subject, mailContent.toString(), "text/plain");
	}
}
