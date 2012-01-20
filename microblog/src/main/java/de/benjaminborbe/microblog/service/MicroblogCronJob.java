package de.benjaminborbe.microblog.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailSendException;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.microblog.util.MicroblogConnector;
import de.benjaminborbe.microblog.util.MicroblogConnectorException;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorage;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorageException;

@Singleton
public class MicroblogCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "0 * * * * ?";

	private final Logger logger;

	private final MicroblogConnector microblogConnector;

	private final MicroblogRevisionStorage microblogRevisionStorage;

	private final MailService mailService;

	@Inject
	public MicroblogCronJob(final Logger logger, final MicroblogConnector microblogConnector, final MicroblogRevisionStorage microblogRevisionStorage, final MailService mailService) {
		this.logger = logger;
		this.microblogConnector = microblogConnector;
		this.microblogRevisionStorage = microblogRevisionStorage;
		this.mailService = mailService;
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public void execute() {
		logger.trace("MonitoringCronJob.execute()");

		try {
			final long latestRevision = microblogConnector.getLatestRevision();
			logger.debug("latestRevision in microblog: " + latestRevision);
			final Long lastestRevisionSend = microblogRevisionStorage.getLastRevision();
			if (lastestRevisionSend == null) {
				// no revision found in storage
				microblogRevisionStorage.setLastRevision(latestRevision);
			}
			else {
				logger.debug("latestRevision send: " + latestRevision);
				for (long rev = lastestRevisionSend + 1; rev <= latestRevision; ++rev) {
					microblogRevisionStorage.setLastRevision(rev);
					send(rev);
				}
			}
			logger.debug("done");
		}
		catch (final MicroblogConnectorException e) {
			logger.debug("MicroblogConnectorException", e);
		}
		catch (final MicroblogRevisionStorageException e) {
			logger.debug("MicroblogRevisionStorageException", e);
		}

		logger.trace("MonitoringCronJob.execute() - finished");
	}

	protected void send(final long rev) {
		logger.trace("send rev = " + rev);
		final Mail mail = buildMail(rev);
		try {
			mailService.send(mail);
		}
		catch (final MailSendException e) {
			logger.error("MailSendException", e);
		}
	}

	protected Mail buildMail(final long rev) {
		final StringBuffer content = new StringBuffer();
		content.append("https://micro.rp.seibert-media.net/notice/" + rev);
		final String from = "bborbe@seibert-media.net";
		final String to = "bborbe@seibert-media.net";
		final String subject = "BB - Microblog - " + rev;
		return new Mail(from, to, subject, content.toString());
	}
}
