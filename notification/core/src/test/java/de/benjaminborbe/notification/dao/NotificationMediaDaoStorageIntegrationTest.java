package de.benjaminborbe.notification.dao;

import com.google.inject.Injector;
import de.benjaminborbe.notification.guice.NotificationModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class NotificationMediaDaoStorageIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new NotificationModulesMock());
		assertNotNull(injector.getInstance(NotificationMediaDaoStorage.class));
	}
}
