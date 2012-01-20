package de.benjaminborbe.microblog.service;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.mail.api.Mail;
import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.microblog.guice.MicroblogModulesMock;
import de.benjaminborbe.microblog.util.MicroblogConnector;
import de.benjaminborbe.microblog.util.MicroblogRevisionStorage;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class MicroblogCronJobTest {

	@Test
	public void singleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new MicroblogModulesMock());
		final MicroblogCronJob a = injector.getInstance(MicroblogCronJob.class);
		final MicroblogCronJob b = injector.getInstance(MicroblogCronJob.class);
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
		assertEquals(a, b);
	}

	@Test
	public void testExecute() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogConnector microblogConnector = EasyMock.createMock(MicroblogConnector.class);
		EasyMock.expect(microblogConnector.getLatestRevision()).andReturn(1337l);
		EasyMock.replay(microblogConnector);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.expect(microblogRevisionStorage.getLastRevision()).andReturn(1335l);
		microblogRevisionStorage.setLastRevision(1336l);
		microblogRevisionStorage.setLastRevision(1337l);
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		mailService.send(EasyMock.anyObject(Mail.class)); // 1336
		mailService.send(EasyMock.anyObject(Mail.class)); // 1337
		EasyMock.replay(mailService);

		final MicroblogCronJob microblogCronJob = new MicroblogCronJob(logger, microblogConnector, microblogRevisionStorage, mailService);

		microblogCronJob.execute();
	}

	@Test
	public void testExecuteNoMail() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final MicroblogConnector microblogConnector = EasyMock.createMock(MicroblogConnector.class);
		EasyMock.expect(microblogConnector.getLatestRevision()).andReturn(1337l);
		EasyMock.replay(microblogConnector);

		final MicroblogRevisionStorage microblogRevisionStorage = EasyMock.createMock(MicroblogRevisionStorage.class);
		EasyMock.expect(microblogRevisionStorage.getLastRevision()).andReturn(1337l);
		EasyMock.replay(microblogRevisionStorage);

		final MailService mailService = EasyMock.createMock(MailService.class);
		EasyMock.replay(mailService);

		final MicroblogCronJob microblogCronJob = new MicroblogCronJob(logger, microblogConnector, microblogRevisionStorage, mailService);

		microblogCronJob.execute();
	}
}
