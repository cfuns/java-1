package de.benjaminborbe.authentication.core.util;

import de.benjaminborbe.tools.password.PasswordCharacter;
import de.benjaminborbe.tools.password.PasswordGenerator;
import de.benjaminborbe.tools.password.PasswordGeneratorImpl;
import de.benjaminborbe.tools.password.PasswordValidator;
import de.benjaminborbe.tools.password.PasswordValidatorImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class AuthenticationPasswordUtilUnitTest {

	@Test
	public void testValidatePassword() throws Exception {
		final AuthenticationPasswordUtil authenticationPasswordUtil = getUtil();
		assertThat(authenticationPasswordUtil.validatePassword("test"), is(not(nullValue())));
		assertThat(authenticationPasswordUtil.validatePassword("test").isEmpty(), is(false));
		assertThat(authenticationPasswordUtil.validatePassword("Test123!").isEmpty(), is(true));
	}

	@Test
	public void testGeneratePassword() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final PasswordValidator passwordValidator = new PasswordValidatorImpl();

		final PasswordGenerator passwordGenerator = EasyMock.createMock(PasswordGenerator.class);
		EasyMock.expect(passwordGenerator.generatePassword(16, PasswordCharacter.values())).andReturn("wrongPw");
		EasyMock.expect(passwordGenerator.generatePassword(16, PasswordCharacter.values())).andReturn("Test123!");
		EasyMock.replay(passwordGenerator);

		final AuthenticationPasswordUtil authenticationPasswordUtil = new AuthenticationPasswordUtil(passwordValidator, passwordGenerator);
		assertThat(authenticationPasswordUtil.generatePassword(), is("Test123!"));
	}

	private AuthenticationPasswordUtil getUtil() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final PasswordValidator passwordValidator = new PasswordValidatorImpl();
		final PasswordGenerator passwordGenerator = new PasswordGeneratorImpl(logger);
		final AuthenticationPasswordUtil authenticationPasswordUtil = new AuthenticationPasswordUtil(passwordValidator, passwordGenerator);
		return authenticationPasswordUtil;
	}
}
