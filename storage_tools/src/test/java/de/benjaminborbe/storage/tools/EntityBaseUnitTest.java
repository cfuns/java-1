package de.benjaminborbe.storage.tools;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import org.junit.Test;

import de.benjaminborbe.api.IdentifierBase;

public class EntityBaseUnitTest {

	private final class TestIdentifier extends IdentifierBase<String> {

		public TestIdentifier(final String id) {
			super(id);
		}

	}

	private final class TestBean extends EntityBase<TestIdentifier> {

		private static final long serialVersionUID = 5598176639226547485L;

	}

	@Test
	public void testGetSet() {
		final TestBean bean = new TestBean();
		assertThat(bean.getId(), is(nullValue()));
		{
			final TestIdentifier id = new TestIdentifier("bla");
			bean.setId(id);
			assertThat(bean.getId(), is(id));
		}
		{
			final TestIdentifier id = null;
			bean.setId(id);
			assertThat(bean.getId(), is(id));
		}
		{
			final TestIdentifier id = new TestIdentifier("foo");
			bean.setId(id);
			assertThat(bean.getId(), is(id));
		}
	}

	@Test
	public void testHashCode() {
		assertThat(buildBean("1").hashCode(), is(buildBean("1").hashCode()));
		assertThat(buildBean("a").hashCode(), is(buildBean("a").hashCode()));
		assertThat(buildBean("foo").hashCode(), is(buildBean("foo").hashCode()));
		assertThat(buildBean("bar").hashCode(), is(buildBean("bar").hashCode()));
	}

	private TestBean buildBean(final String id) {
		final TestBean bean = new TestBean();
		bean.setId(id != null ? new TestIdentifier(id) : null);
		return bean;
	}

	@Test
	public void testEquals() throws Exception {
		assertThat(buildBean("1").equals(buildBean("1")), is(true));
		assertThat(buildBean("1").equals(buildBean("2")), is(false));
		assertThat(buildBean(null).equals(buildBean(null)), is(false));
		assertThat(buildBean("1").equals(buildBean(null)), is(false));
		assertThat(buildBean(null).equals(buildBean("1")), is(false));
		final TestBean bean = new TestBean();
		assertThat(bean.equals(bean), is(true));
	}
}
