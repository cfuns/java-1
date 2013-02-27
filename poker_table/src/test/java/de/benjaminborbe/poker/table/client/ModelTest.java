package de.benjaminborbe.poker.table.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ModelTest {

	@Test
	public void testSingleton() {
		final Model model = new Model();
		assertNotNull(model);
		final Tables tables = model.getTables();
		assertNotNull(tables);
		assertEquals(0, tables.size());
	}
}
