package de.benjaminborbe.tools.search;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class BeanSearcherUnitTest {

	private class Bean {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}
	}

	@Test
	public void testSearch() {
		final BeanSearcher<Bean> beanSearcher = new BeanSearcher<Bean>() {

			@Override
			protected Collection<String> getSearchValues(final Bean bean) {
				return Arrays.asList(bean.getName());
			}
		};

		final int limit = Integer.MAX_VALUE;

		final List<Bean> beans = new ArrayList<Bean>();
		assertEquals(0, beanSearcher.search(beans, limit, "hello", "world").size());
		beans.add(buildBean("bla"));
		assertEquals(0, beanSearcher.search(beans, limit, "hello", "world").size());
		beans.add(buildBean("hello"));
		assertEquals(1, beanSearcher.search(beans, limit, "hello", "world").size());
		beans.add(buildBean("world"));
		assertEquals(2, beanSearcher.search(beans, limit, "hello", "world").size());
		beans.add(buildBean("hello world"));
		assertEquals(3, beanSearcher.search(beans, limit, "hello", "world").size());
		assertEquals(1, beanSearcher.search(beans, 1, "hello", "world").size());
		assertEquals(0, beanSearcher.search(beans, 0, "hello", "world").size());
	}

	private Bean buildBean(final String name) {
		final Bean bean = new Bean();
		bean.setName(name);
		return bean;
	}
}
