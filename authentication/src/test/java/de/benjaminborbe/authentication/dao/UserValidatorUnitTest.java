package de.benjaminborbe.authentication.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.ValidationConstraintValidatorMock;

public class UserValidatorUnitTest {

	@Test
	public void testValidUsername() throws Exception {
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
}
