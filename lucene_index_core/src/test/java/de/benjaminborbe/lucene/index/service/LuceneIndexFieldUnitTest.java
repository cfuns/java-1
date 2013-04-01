package de.benjaminborbe.lucene.index.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LuceneIndexFieldUnitTest {

	@Test
	public void testNotEmpty() {
		for (final LuceneIndexField indexField : LuceneIndexField.values()) {
			assertNotNull(indexField.getFieldName());
			assertTrue(indexField.getFieldName().length() > 0);
		}
	}

}
