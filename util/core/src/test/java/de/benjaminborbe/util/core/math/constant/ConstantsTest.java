package de.benjaminborbe.util.core.math.constant;

import de.benjaminborbe.util.core.math.HasValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstantsTest {

	@Test
	public void testPi() throws Exception {
		final Constants constants = new Constants();
		final HasValue operationValue = constants.get("pi");
		assertEquals(3.14159d, operationValue.getValue(), 0d);
		assertEquals("3.14159", operationValue.asString());

	}

}
