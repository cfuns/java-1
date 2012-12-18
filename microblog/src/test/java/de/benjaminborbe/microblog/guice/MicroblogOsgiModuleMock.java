package de.benjaminborbe.microblog.guice;

import org.apache.felix.http.api.ExtHttpService;
import org.osgi.service.log.LogService;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.benjaminborbe.mail.api.MailService;
import de.benjaminborbe.mail.mock.MailServiceMock;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.mock.StorageServiceMock;
import de.benjaminborbe.tools.osgi.mock.ExtHttpServiceMock;
import de.benjaminborbe.tools.osgi.mock.LogServiceMock;
import de.benjaminborbe.xmpp.api.XmppService;
import de.benjaminborbe.xmpp.mock.XmppServiceMock;

public class MicroblogOsgiModuleMock extends AbstractModule {

	@Override
	protected void configure() {
		bind(XmppService.class).to(XmppServiceMock.class).in(Singleton.class);
		bind(StorageService.class).to(StorageServiceMock.class).in(Singleton.class);
		bind(MailService.class).to(MailServiceMock.class).in(Singleton.class);
		bind(LogService.class).to(LogServiceMock.class).in(Singleton.class);
		bind(ExtHttpService.class).to(ExtHttpServiceMock.class).in(Singleton.class);
	}
}
