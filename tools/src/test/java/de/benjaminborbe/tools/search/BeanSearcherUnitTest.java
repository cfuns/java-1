package de.benjaminborbe.tools.search;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.benjaminborbe.tools.map.MapChain;

public class BeanSearcherUnitTest {

	private static final String FIELD = "field";

	private final class BeanSearchImpl extends BeanSearcher<Bean> {

		@Override
		protected Map<String, String> getSearchValues(final Bean bean) {
			return new MapChain<String, String>().add(FIELD, bean.getName());
		}

		@Override
		protected Map<String, Integer> getSearchPrio() {
			return new MapChain<String, Integer>().add(FIELD, 1);
		}
	}

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
		final BeanSearcher<Bean> beanSearcher = new BeanSearchImpl();
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
