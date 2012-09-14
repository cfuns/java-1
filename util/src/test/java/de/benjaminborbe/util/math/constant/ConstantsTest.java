package de.benjaminborbe.util.math.constant;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.benjaminborbe.util.math.HasValue;

public class ConstantsTest {

	@Test
	public void testPi() throws Exception {
		final Constants constants = new Constants();
		final HasValue operationValue = constants.get("pi");
		assertEquals(3.14159d, operationValue.getValue(), 0d);
		assertEquals("3.14159", operationValue.asString());

	}

}
