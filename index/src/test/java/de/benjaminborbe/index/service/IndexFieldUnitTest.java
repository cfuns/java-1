package de.benjaminborbe.index.service;

import static org.junit.Assert.*;
import org.junit.Test;

public class IndexFieldUnitTest {

	@Test
	public void testNotEmpty() {
		for (final IndexField indexField : IndexField.values()) {
			assertNotNull(indexField.getFieldName());
			assertTrue(indexField.getFieldName().length() > 0);
		}
	}

}
