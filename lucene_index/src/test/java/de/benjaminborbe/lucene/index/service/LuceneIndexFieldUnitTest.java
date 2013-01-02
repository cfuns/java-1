package de.benjaminborbe.lucene.index.service;

import static org.junit.Assert.*;
import org.junit.Test;

import de.benjaminborbe.lucene.index.service.LuceneIndexField;

public class LuceneIndexFieldUnitTest {

	@Test
	public void testNotEmpty() {
		for (final LuceneIndexField indexField : LuceneIndexField.values()) {
			assertNotNull(indexField.getFieldName());
			assertTrue(indexField.getFieldName().length() > 0);
		}
	}

}
