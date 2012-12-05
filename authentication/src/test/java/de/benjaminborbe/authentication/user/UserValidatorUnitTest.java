package de.benjaminborbe.authentication.user;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import de.benjaminborbe.authentication.api.UserIdentifier;

public class UserValidatorUnitTest {

	@Test
	public void testValidUsername() throws Exception {
		final UserValidator v = new UserValidator();
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
