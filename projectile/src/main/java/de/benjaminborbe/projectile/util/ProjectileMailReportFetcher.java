package de.benjaminborbe.projectile.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;
import de.benjaminborbe.projectile.config.ProjectileConfig;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.synchronize.RunOnlyOnceATime;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.StreamUtil;

public class ProjectileMailReportFetcher {

	private final class FetchRunnable implements Runnable {

		@Override
		public void run() {
			Store store = null;
			try {

				final String host = projectileConfig.getPop3Hostname();
				final String user = projectileConfig.getPop3Login();
				final String password = projectileConfig.getPop3Password();

				final Session session = Session.getInstance(new Properties());
				store = session.getStore("pop3s");
				store.connect(host, user, password);

				final Folder fldr = store.getFolder("INBOX");

				fldr.open(Folder.READ_WRITE);

				final int count = fldr.getMessageCount();
				logger.debug(count + " total messages");

				// Message numbers start at 1
				for (int i = 1; i <= count; i++) {
					// Get a message by its sequence number
					final Message m = fldr.getMessage(i);

					final String subj = m.getSubject();
					final Object content = m.getContent();
					if (content instanceof Multipart) {
						final Multipart mp = (Multipart) content;
						final int count3 = mp.getCount();
						for (int j = 0; j < count3; j++) {
							final BodyPart b = mp.getBodyPart(j);
							final Object attachmentContent = b.getContent();
							if (attachmentContent instanceof InputStream) {

								logger.debug("ContentType: " + b.getContentType());

								final InputStream inputStream = (InputStream) attachmentContent;
								final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
								streamUtil.copy(inputStream, outputStream);

								final String csvContent = outputStream.toString("ISO-8859-1");
								final ProjectileSlacktimeReportInterval interval = buildInterval(subj);
								projectileCsvReportImporter.importCsvReport(csvContent, interval);
							}
						}
					}

					if (projectileConfig.getPop3Delete()) {
						// mark mail to delete
						m.setFlag(Flags.Flag.DELETED, true);
						logger.debug("mark mail to delete");
					}
					else {
						logger.debug("leave mail on server");
					}
				}

				// "true" actually deletes flagged messages from folder
				fldr.close(true);
			}
			catch (final NoSuchProviderException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final MessagingException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final IOException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final StorageException e) {
				logger.warn(e.getClass().getName(), e);
			}
			catch (final ParseException e) {
				logger.warn(e.getClass().getName(), e);
			}
			finally {
				if (store != null) {
					try {
						store.close();
					}
					catch (final MessagingException e) {
					}
				}
			}
		}

	}

	private final RunOnlyOnceATime runOnlyOnceATime;

	private final Logger logger;

	private final StreamUtil streamUtil;

	private final ProjectileCsvReportImporter projectileCsvReportImporter;

	private final ProjectileConfig projectileConfig;

	@Inject
	public ProjectileMailReportFetcher(
			final Logger logger,
			final ProjectileConfig projectileConfig,
			final RunOnlyOnceATime runOnlyOnceATime,
			final StreamUtil streamUtil,
			final ProjectileCsvReportImporter projectileCsvReportImporter) {
		this.logger = logger;
		this.projectileConfig = projectileConfig;
		this.runOnlyOnceATime = runOnlyOnceATime;
		this.streamUtil = streamUtil;
		this.projectileCsvReportImporter = projectileCsvReportImporter;
	}

	public boolean fetch() {
		return runOnlyOnceATime.run(new FetchRunnable());
	}

	private ProjectileSlacktimeReportInterval buildInterval(final String subject) {
		final String lowerSubject = subject.toLowerCase();
		if (lowerSubject.contains("year") || lowerSubject.contains("jahr")) {
			return ProjectileSlacktimeReportInterval.YEAR;
		}
		if (lowerSubject.contains("month") || lowerSubject.contains("monat")) {
			return ProjectileSlacktimeReportInterval.MONTH;
		}
		if (lowerSubject.contains("week") || lowerSubject.contains("woche")) {
			return ProjectileSlacktimeReportInterval.WEEK;
		}
		throw new IllegalArgumentException("illegal subject");
	}
}
