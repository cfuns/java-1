package de.benjaminborbe.authorization.permission;

import com.google.inject.Injector;
import de.benjaminborbe.authorization.dao.PermissionBeanMapper;
import de.benjaminborbe.authorization.guice.AuthorizationModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class PermissionBeanMapperIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthorizationModulesMock());
		assertNotNull(injector.getInstance(PermissionBeanMapper.class));
	}
}
