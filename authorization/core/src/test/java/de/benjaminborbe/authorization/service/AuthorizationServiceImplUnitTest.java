package de.benjaminborbe.authorization.service;

import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.authorization.api.AuthorizationService;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.authorization.dao.RoleBean;
import de.benjaminborbe.authorization.dao.RoleDao;
import de.benjaminborbe.authorization.dao.UserRoleManyToManyRelation;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AuthorizationServiceImplUnitTest {

	@Test
	public void testHasRoleRoleNotFoundNoSuperadmin() throws Exception {
		final UserIdentifier userIdentifier = new UserIdentifier("testuser");
		final RoleIdentifier roleIdentifier = new RoleIdentifier("testrole");

		{
			final RoleDao roleDao = EasyMock.createMock(RoleDao.class);
			EasyMock.expect(roleDao.findByRolename(roleIdentifier)).andReturn(null);
			EasyMock.replay(roleDao);

			final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
			EasyMock.expect(authenticationService.isSuperAdmin(userIdentifier)).andReturn(false);
			EasyMock.replay(authenticationService);

			final AuthorizationServiceImpl service = new AuthorizationServiceImpl(null, authenticationService, roleDao, null, null, null);

			assertThat(service.hasRole(userIdentifier, roleIdentifier), is(false));
		}
	}

	@Test
	public void testHasRoleSuperAdmin() throws Exception {
		final UserIdentifier userIdentifier = new UserIdentifier("testuser");
		final RoleIdentifier roleIdentifier = new RoleIdentifier("testrole");

		{
			final RoleDao roleDao = EasyMock.createMock(RoleDao.class);
			EasyMock.expect(roleDao.findByRolename(roleIdentifier)).andReturn(null);
			EasyMock.replay(roleDao);

			final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
			EasyMock.expect(authenticationService.isSuperAdmin(userIdentifier)).andReturn(true);
			EasyMock.replay(authenticationService);

			final AuthorizationServiceImpl service = new AuthorizationServiceImpl(null, authenticationService, roleDao, null, null, null);

			assertThat(service.hasRole(userIdentifier, roleIdentifier), is(true));
		}
	}

	@Test
	public void testHasRoleRoleFoundButNoRelation() throws Exception {
		final UserIdentifier userIdentifier = new UserIdentifier("testuser");
		final RoleIdentifier roleIdentifier = new RoleIdentifier("testrole");

		{
			final UserRoleManyToManyRelation userRoleManyToManyRelation = EasyMock.createMock(UserRoleManyToManyRelation.class);
			EasyMock.expect(userRoleManyToManyRelation.exists(userIdentifier, roleIdentifier)).andReturn(false);
			EasyMock.replay(userRoleManyToManyRelation);

			final RoleBean roleBean = EasyMock.createMock(RoleBean.class);
			EasyMock.replay(roleBean);

			final RoleDao roleDao = EasyMock.createMock(RoleDao.class);
			EasyMock.expect(roleDao.findByRolename(roleIdentifier)).andReturn(roleBean);
			EasyMock.replay(roleDao);

			final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
			EasyMock.expect(authenticationService.isSuperAdmin(userIdentifier)).andReturn(false);
			EasyMock.replay(authenticationService);

			final AuthorizationServiceImpl service = new AuthorizationServiceImpl(null, authenticationService, roleDao, null, userRoleManyToManyRelation, null);

			assertThat(service.hasRole(userIdentifier, roleIdentifier), is(false));
		}
	}

	@Test
	public void testHasRoleRoleFoundAndRelation() throws Exception {
		final UserIdentifier userIdentifier = new UserIdentifier("testuser");
		final RoleIdentifier roleIdentifier = new RoleIdentifier("testrole");

		{
			final UserRoleManyToManyRelation userRoleManyToManyRelation = EasyMock.createMock(UserRoleManyToManyRelation.class);
			EasyMock.expect(userRoleManyToManyRelation.exists(userIdentifier, roleIdentifier)).andReturn(true);
			EasyMock.replay(userRoleManyToManyRelation);

			final RoleBean roleBean = EasyMock.createMock(RoleBean.class);
			EasyMock.replay(roleBean);

			final RoleDao roleDao = EasyMock.createMock(RoleDao.class);
			EasyMock.expect(roleDao.findByRolename(roleIdentifier)).andReturn(roleBean);
			EasyMock.replay(roleDao);

			final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
			EasyMock.expect(authenticationService.isSuperAdmin(userIdentifier)).andReturn(false);
			EasyMock.replay(authenticationService);

			final AuthorizationServiceImpl service = new AuthorizationServiceImpl(null, authenticationService, roleDao, null, userRoleManyToManyRelation, null);

			assertThat(service.hasRole(userIdentifier, roleIdentifier), is(true));
		}
	}

	@Test
	public void testHasLoggedInRole() throws Exception {
		final SessionIdentifier sessionIdentifier = new SessionIdentifier("sid");
		final RoleIdentifier roleIdentifier = new RoleIdentifier(AuthorizationService.ROLE_LOGGED_IN);

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		// logged out
		{
			final UserIdentifier userIdentifier = null;
			final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
			EasyMock.expect(authenticationService.isSuperAdmin(sessionIdentifier)).andReturn(false);
			EasyMock.expect(authenticationService.getCurrentUser(sessionIdentifier)).andReturn(userIdentifier);
			EasyMock.expect(authenticationService.isSuperAdmin(userIdentifier)).andReturn(false);
			EasyMock.replay(authenticationService);

			final RoleDao roleDao = EasyMock.createMock(RoleDao.class);
			EasyMock.expect(roleDao.findByRolename(roleIdentifier)).andReturn(null);
			EasyMock.replay(roleDao);

			final AuthorizationServiceImpl service = new AuthorizationServiceImpl(logger, authenticationService, roleDao, null, null, null);
			assertThat(service.hasRole(sessionIdentifier, roleIdentifier), is(false));
		}

		// logged in
		{
			final UserIdentifier userIdentifier = new UserIdentifier("testuser");
			final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
			EasyMock.expect(authenticationService.isSuperAdmin(sessionIdentifier)).andReturn(false);
			EasyMock.expect(authenticationService.getCurrentUser(sessionIdentifier)).andReturn(userIdentifier);
			EasyMock.expect(authenticationService.isLoggedIn(sessionIdentifier)).andReturn(true);
			EasyMock.replay(authenticationService);

			final AuthorizationServiceImpl service = new AuthorizationServiceImpl(logger, authenticationService, null, null, null, null);
			assertThat(service.hasRole(sessionIdentifier, roleIdentifier), is(true));
		}
	}

	@Test
	public void testHasLoggedOutRole() throws Exception {
		final SessionIdentifier sessionIdentifier = new SessionIdentifier("sid");
		final RoleIdentifier roleIdentifier = new RoleIdentifier(AuthorizationService.ROLE_LOGGED_OUT);
		final UserIdentifier userIdentifier = new UserIdentifier("testuser");

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		// logged out
		{
			final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
			EasyMock.expect(authenticationService.isSuperAdmin(sessionIdentifier)).andReturn(false);
			EasyMock.expect(authenticationService.getCurrentUser(sessionIdentifier)).andReturn(null);
			EasyMock.expect(authenticationService.isLoggedIn(sessionIdentifier)).andReturn(false);
			EasyMock.replay(authenticationService);

			final AuthorizationServiceImpl service = new AuthorizationServiceImpl(logger, authenticationService, null, null, null, null);
			assertThat(service.hasRole(sessionIdentifier, roleIdentifier), is(true));
		}

		// logged in
		{
			final AuthenticationService authenticationService = EasyMock.createMock(AuthenticationService.class);
			EasyMock.expect(authenticationService.isSuperAdmin(sessionIdentifier)).andReturn(false);
			EasyMock.expect(authenticationService.isSuperAdmin(userIdentifier)).andReturn(false);
			EasyMock.expect(authenticationService.getCurrentUser(sessionIdentifier)).andReturn(userIdentifier);
			EasyMock.expect(authenticationService.isLoggedIn(sessionIdentifier)).andReturn(true);
			EasyMock.replay(authenticationService);

			final RoleDao roleDao = EasyMock.createMock(RoleDao.class);
			EasyMock.expect(roleDao.findByRolename(roleIdentifier)).andReturn(null);
			EasyMock.replay(roleDao);

			final AuthorizationServiceImpl service = new AuthorizationServiceImpl(logger, authenticationService, roleDao, null, null, null);
			assertThat(service.hasRole(sessionIdentifier, roleIdentifier), is(false));
		}
	}
}
