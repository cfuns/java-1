package de.benjaminborbe.authorization.permissionrole;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.authorization.guice.AuthorizationModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class PermissionRoleManyToManyRelationIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthorizationModulesMock());
		assertNotNull(injector.getInstance(PermissionRoleManyToManyRelation.class));
	}

	@Test
	public void testCrud() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new AuthorizationModulesMock());
		final PermissionRoleManyToManyRelation permissionRoleManyToManyRelation = injector.getInstance(PermissionRoleManyToManyRelation.class);
		final PermissionIdentifier pid = new PermissionIdentifier("pid");
		final RoleIdentifier rid = new RoleIdentifier("rid");
		permissionRoleManyToManyRelation.add(pid, rid);
		assertTrue(permissionRoleManyToManyRelation.exists(pid, rid));
		permissionRoleManyToManyRelation.remove(pid, rid);
		assertFalse(permissionRoleManyToManyRelation.exists(pid, rid));
	}
}
