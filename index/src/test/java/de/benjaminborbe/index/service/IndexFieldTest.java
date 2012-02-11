package de.benjaminborbe.index.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IndexFieldTest {

	@Test
	public void testNotEmpty() {
		for (final IndexField indexField : IndexField.values()) {
			assertNotNull(indexField.getFieldName());
			assertTrue(indexField.getFieldName().length() > 0);
		}
	}
}
