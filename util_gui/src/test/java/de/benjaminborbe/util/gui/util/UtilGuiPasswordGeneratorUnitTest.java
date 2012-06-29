package de.benjaminborbe.util.gui.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class UtilGuiPasswordGeneratorUnitTest {

	@Test
	public void testgeneratePassword() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final UtilGuiPasswordGenerator utilPasswordGenerator = new UtilGuiPasswordGeneratorImpl(logger);
		assertNotNull(utilPasswordGenerator);
		final String password = utilPasswordGenerator.generatePassword(1, UtilGuiPasswordCharacter.NUMBER);
		assertNotNull(password);
		assertEquals(1, password.length());
	}

}
