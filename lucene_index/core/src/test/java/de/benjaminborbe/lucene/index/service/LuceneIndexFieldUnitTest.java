package de.benjaminborbe.lucene.index.service;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LuceneIndexFieldUnitTest {

	@Test
	public void testNotEmpty() {
		for (final LuceneIndexField indexField : LuceneIndexField.values()) {
			assertNotNull(indexField.getFieldName());
			assertTrue(indexField.getFieldName().length() > 0);
		}
	}

}
