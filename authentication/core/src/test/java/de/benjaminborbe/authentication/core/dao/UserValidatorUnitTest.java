package de.benjaminborbe.authentication.core.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.ValidationConstraintValidatorMock;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserValidatorUnitTest {

	@Test
	public void testValidateUsername() throws Exception {
		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.expect(userDao.exists(EasyMock.anyObject(UserIdentifier.class))).andReturn(false).anyTimes();
		EasyMock.replay(userDao);

		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidatorMock();

		final UserValidator v = new UserValidator(validationConstraintValidator, userDao);
		final UserBean user = new UserBean();

		user.setId(new UserIdentifier("a"));
		assertThat(v.validate(user).size(), is(0));

		user.setId(null);
		assertThat(v.validate(user).size(), is(1));

		user.setId(new UserIdentifier(null));
		assertThat(v.validate(user).size(), is(1));

		user.setId(new UserIdentifier("A"));
		assertThat(v.validate(user).size(), is(1));

		user.setId(new UserIdentifier("Aa"));
		assertThat(v.validate(user).size(), is(1));
	}

	@Test
	public void testValidateEmail() throws Exception {
		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.expect(userDao.exists(EasyMock.anyObject(UserIdentifier.class))).andReturn(false).anyTimes();
		EasyMock.replay(userDao);
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		final UserValidator v = new UserValidator(validationConstraintValidator, userDao);
		final UserBean user = new UserBean();
		user.setId(new UserIdentifier("a"));

		{
			user.setEmail("foo@example.com");
			user.setEmailNew(null);
			assertThat(v.validate(user).toString(), v.validate(user).size(), is(0));
			assertThat(v.validate(user).toString(), v.validate(user, Arrays.asList("email")).size(), is(0));
		}
		{
			user.setEmail(null);
			user.setEmailNew("foo@example.com");
			assertThat(v.validate(user).toString(), v.validate(user).size(), is(0));
			assertThat(v.validate(user).toString(), v.validate(user, Arrays.asList("email")).size(), is(0));
		}
		{
			user.setEmail(null);
			user.setEmailNew(null);
			assertThat(v.validate(user).toString(), v.validate(user).size(), is(1));
			assertThat(v.validate(user).toString(), v.validate(user, Arrays.asList("email")).size(), is(1));
		}
	}

	@Test
	public void testValidateEmailNew() throws Exception {
		final UserDao userDao = EasyMock.createMock(UserDao.class);
		EasyMock.expect(userDao.exists(EasyMock.anyObject(UserIdentifier.class))).andReturn(false).anyTimes();
		EasyMock.replay(userDao);
		final ValidationConstraintValidator validationConstraintValidator = new ValidationConstraintValidator();
		final UserValidator v = new UserValidator(validationConstraintValidator, userDao);
		final UserBean user = new UserBean();
		user.setId(new UserIdentifier("a"));
		user.setEmail("foo@example.com");
		{
			user.setEmailNew(null);
			assertThat(v.validate(user).toString(), v.validate(user).size(), is(0));
			assertThat(v.validate(user).toString(), v.validate(user, Arrays.asList("emailNew")).size(), is(0));
		}
		{
			user.setEmailNew("foo@example.com");
			assertThat(v.validate(user).toString(), v.validate(user).size(), is(0));
			assertThat(v.validate(user).toString(), v.validate(user, Arrays.asList("emailNew")).size(), is(0));
		}
		{
			user.setEmailNew("foobar");
			assertThat(v.validate(user).toString(), v.validate(user).size(), is(1));
			assertThat(v.validate(user).toString(), v.validate(user, Arrays.asList("emailNew")).size(), is(1));
		}
	}
}
