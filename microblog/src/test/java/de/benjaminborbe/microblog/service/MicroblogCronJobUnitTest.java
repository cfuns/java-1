package de.benjaminborbe.microblog.service;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.connector.MicroblogConnector;
import de.benjaminborbe.microblog.conversation.MicroblogConversationFinder;
import de.benjaminborbe.microblog.post.MicroblogPostMailer;
import de.benjaminborbe.microblog.revision.MicroblogRevisionStorage;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;

public class MicroblogCronJobUnitTest {

	@Test
	public void testExecute() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogConnector microblogConnector = EasyMock.createMock(MicroblogConnector.class);
		EasyMock.expect(microblogConnector.getLatestRevision()).andReturn(new MicroblogPostIdentifier(1337l));
		EasyMock.replay(microblogConnector);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.expect(microblogRevisionStorage.getLastRevision()).andReturn(new MicroblogPostIdentifier(1335l));
		microblogRevisionStorage.setLastRevision(new MicroblogPostIdentifier(1336l));
		microblogRevisionStorage.setLastRevision(new MicroblogPostIdentifier(1337l));
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		mailService.send(EasyMock.anyObject(Mail.class)); // 1336
		mailService.send(EasyMock.anyObject(Mail.class)); // 1337
		EasyMock.replay(mailService);

		final HttpDownloader httpDownloader = EasyMock.createNiceMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createNiceMock(HttpDownloadUtil.class);
		EasyMock.replay(httpDownloadUtil);

		final MicroblogPostMailer microblogPostMailer = EasyMock.createNiceMock(MicroblogPostMailer.class);
		EasyMock.replay(microblogPostMailer);

		final MicroblogConversationFinder microblogConversationFinder = EasyMock.createNiceMock(MicroblogConversationFinder.class);
		EasyMock.replay(microblogConversationFinder);

		final MicroblogCronJob microblogCronJob = new MicroblogCronJob(logger, microblogConnector, microblogRevisionStorage, microblogConversationFinder, microblogPostMailer, null);

		microblogCronJob.execute();
	}

	@Test
	public void testExecuteNoMail() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogConnector microblogConnector = EasyMock.createMock(MicroblogConnector.class);
		EasyMock.expect(microblogConnector.getLatestRevision()).andReturn(new MicroblogPostIdentifier(1337l));
		EasyMock.replay(microblogConnector);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.expect(microblogRevisionStorage.getLastRevision()).andReturn(new MicroblogPostIdentifier(1337l));
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		EasyMock.replay(mailService);

		final HttpDownloader httpDownloader = EasyMock.createNiceMock(HttpDownloader.class);
		EasyMock.replay(httpDownloader);

		final HttpDownloadUtil httpDownloadUtil = EasyMock.createNiceMock(HttpDownloadUtil.class);
		EasyMock.replay(httpDownloadUtil);
		final MicroblogPostMailer microblogPostMailer = EasyMock.createNiceMock(MicroblogPostMailer.class);
		EasyMock.replay(microblogPostMailer);

		final MicroblogConversationFinder microblogConversationFinder = EasyMock.createNiceMock(MicroblogConversationFinder.class);
		EasyMock.replay(microblogConversationFinder);

		final MicroblogCronJob microblogCronJob = new MicroblogCronJob(logger, microblogConnector, microblogRevisionStorage, microblogConversationFinder, microblogPostMailer, null);

		microblogCronJob.execute();
	}

}
