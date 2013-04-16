package de.benjaminborbe.util.core.math.operation;

import de.benjaminborbe.util.core.math.HasValue;
import de.benjaminborbe.util.core.math.NumberValue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OperationsTest {

	@Test
	public void testMultiply() throws Exception {
		final Operations operations = new Operations();
		final HasValue operationValue = operations.get("*", new NumberValue("1337"), new NumberValue("42"));
		assertEquals(56154d, operationValue.getValue(), 0d);
		assertEquals("1337 * 42", operationValue.asString());
	}

	@Test
	public void testAddition() throws Exception {
		final Operations operations = new Operations();
		final HasValue operationValue = operations.get("+", new NumberValue("1337"), new NumberValue("42"));
		assertEquals(1379d, operationValue.getValue(), 0d);
		assertEquals("1337 + 42", operationValue.asString());
	}

	@Test
	public void testDivision() throws Exception {
		final Operations operations = new Operations();
		final HasValue operationValue = operations.get("/", new NumberValue("84"), new NumberValue("42"));
		assertEquals(2d, operationValue.getValue(), 0d);
		assertEquals("84 / 42", operationValue.asString());
	}

	@Test
	public void testSubtraction() throws Exception {
		final Operations operations = new Operations();
		final HasValue operationValue = operations.get("-", new NumberValue("1337"), new NumberValue("42"));
		assertEquals(1295, operationValue.getValue(), 0d);
		assertEquals("1337 - 42", operationValue.asString());
	}
}
