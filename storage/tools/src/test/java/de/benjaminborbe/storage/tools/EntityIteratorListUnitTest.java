package de.benjaminborbe.storage.tools;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EntityIteratorListUnitTest {

	@Test
	public void testEmpty() throws Exception {
		EntityIteratorList<Object> entityIteratorList = new EntityIteratorList<Object>();
		assertThat(entityIteratorList.hasNext(), is(false));
		assertThat(entityIteratorList.hasNext(), is(false));
	}

	@Test
	public void testOneElement() throws Exception {
		Object o1 = new Object();
		EntityIteratorList<Object> entityIteratorList = new EntityIteratorList<Object>(o1);
		assertThat(entityIteratorList.hasNext(), is(true));
		assertThat(entityIteratorList.hasNext(), is(true));
		assertThat(entityIteratorList.next(), is(o1));
		assertThat(entityIteratorList.hasNext(), is(false));
		assertThat(entityIteratorList.hasNext(), is(false));
	}

	@Test
	public void testTwoElement() throws Exception {
		Object o1 = new Object();
		Object o2 = new Object();
		EntityIteratorList<Object> entityIteratorList = new EntityIteratorList<Object>(o1, o2);
		assertThat(entityIteratorList.hasNext(), is(true));
		assertThat(entityIteratorList.hasNext(), is(true));
		assertThat(entityIteratorList.next(), is(o1));
		assertThat(entityIteratorList.hasNext(), is(true));
		assertThat(entityIteratorList.hasNext(), is(true));
		assertThat(entityIteratorList.next(), is(o2));
		assertThat(entityIteratorList.hasNext(), is(false));
		assertThat(entityIteratorList.hasNext(), is(false));
	}
}
