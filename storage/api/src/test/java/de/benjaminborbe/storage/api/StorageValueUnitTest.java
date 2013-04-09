package de.benjaminborbe.storage.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StorageValueUnitTest {

	private final String encoding = "UTF8";

	@Test
	public void testIsEmpty() {
		assertTrue(new StorageValue().isEmpty());
		assertFalse(new StorageValue("", encoding).isEmpty());
		assertFalse(new StorageValue("a", encoding).isEmpty());
		assertFalse(new StorageValue(new byte[0], encoding).isEmpty());
		assertFalse(new StorageValue(new byte[] { 1 }, encoding).isEmpty());
	}

	@Test
	public void testHashCode() {
		assertEquals(new StorageValue().hashCode(), new StorageValue().hashCode());
		assertEquals(new StorageValue("", encoding).hashCode(), new StorageValue("", encoding).hashCode());
		assertEquals(new StorageValue("a", encoding).hashCode(), new StorageValue("a", encoding).hashCode());
		assertEquals(new StorageValue(new byte[0], encoding).hashCode(), new StorageValue(new byte[0], encoding).hashCode());
		assertEquals(new StorageValue(new byte[] { 1 }, encoding).hashCode(), new StorageValue(new byte[] { 1 }, encoding).hashCode());
	}

	@Test
	public void testToString() {
		assertEquals(null, new StorageValue().toString());
		assertEquals("", new StorageValue("", encoding).toString());
		assertEquals("a", new StorageValue("a", encoding).toString());
	}

	@Test
	public void testEquals() {
		assertTrue(new StorageValue("", encoding).equals(new StorageValue("", encoding)));
		assertTrue(new StorageValue("a", encoding).equals(new StorageValue("a", encoding)));
		assertTrue(new StorageValue("Bäm", encoding).equals(new StorageValue("Bäm", encoding)));
		assertTrue(new StorageValue(new byte[0], encoding).equals(new StorageValue(new byte[0], encoding)));
		assertTrue(new StorageValue(new byte[] { 1 }, encoding).equals(new StorageValue(new byte[] { 1 }, encoding)));
		assertEquals(new StorageValue("Bäm", encoding), new StorageValue("Bäm", encoding));
	}
}
