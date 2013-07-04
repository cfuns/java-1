package de.benjaminborbe.util.gui.util;

import de.benjaminborbe.tools.password.PasswordCharacter;
import de.benjaminborbe.tools.password.PasswordGenerator;
import de.benjaminborbe.tools.password.PasswordGeneratorImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UtilGuiPasswordGeneratorUnitTest {

	@Test
	public void testgeneratePassword() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final PasswordGenerator utilPasswordGenerator = new PasswordGeneratorImpl(logger);
		assertNotNull(utilPasswordGenerator);
		final String password = utilPasswordGenerator.generatePassword(1, PasswordCharacter.NUMBER);
		assertNotNull(password);
		assertEquals(1, password.length());
	}

}
