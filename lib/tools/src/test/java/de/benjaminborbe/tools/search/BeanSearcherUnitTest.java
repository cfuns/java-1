package de.benjaminborbe.tools.search;

import de.benjaminborbe.tools.map.MapChain;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BeanSearcherUnitTest {

	private static final String FIELD_NAME = "name";

	private static final String FIELD_TITLE = "title";

	private final class BeanSearchImpl extends BeanSearcher<Bean> {

		@Override
		protected Map<String, String> getSearchValues(final Bean bean) {
			return new MapChain<String, String>().add(FIELD_NAME, bean.getName()).add(FIELD_TITLE, bean.getTitle());
		}

		@Override
		protected Map<String, Integer> getSearchPrio() {
			return new MapChain<String, Integer>().add(FIELD_NAME, 1).add(FIELD_TITLE, 2);
		}
	}

	private class Bean {

		private String name;

		private String title;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(final String title) {
			this.title = title;
		}
	}

	@Test
	public void testSearch() {
		final BeanSearcher<Bean> beanSearcher = new BeanSearchImpl();
		final int limit = Integer.MAX_VALUE;
		final List<Bean> beans = new ArrayList<>();
		assertEquals(0, beanSearcher.search(beans, limit, Arrays.asList("hello", "world")).size());
		beans.add(buildBean("test", "bla"));
		assertEquals(0, beanSearcher.search(beans, limit, Arrays.asList("hello", "world")).size());
		beans.add(buildBean("test", "hello"));
		assertEquals(1, beanSearcher.search(beans, limit, Arrays.asList("hello", "world")).size());
		beans.add(buildBean("test", "world"));
		assertEquals(2, beanSearcher.search(beans, limit, Arrays.asList("hello", "world")).size());
		beans.add(buildBean("test", "hello world"));
		assertEquals(3, beanSearcher.search(beans, limit, Arrays.asList("hello", "world")).size());
		assertEquals(1, beanSearcher.search(beans, 1, Arrays.asList("hello", "world")).size());
		assertEquals(0, beanSearcher.search(beans, 0, Arrays.asList("hello", "world")).size());
	}

	@Test
	public void testRating() {
		final BeanSearcher<Bean> beanSearcher = new BeanSearchImpl();
		final int limit = Integer.MAX_VALUE;
		{
			final List<Bean> beans = new ArrayList<>();
			beans.add(buildBean("hello", "world"));
			assertEquals(1, beanSearcher.search(beans, limit, Arrays.asList("hello")).size());
			assertEquals(2 * 1000 / 3, beanSearcher.search(beans, limit, Arrays.asList("hello")).get(0).getMatchCounter());
			assertEquals(1, beanSearcher.search(beans, limit, Arrays.asList("world")).size());
			assertEquals(1 * 1000 / 3, beanSearcher.search(beans, limit, Arrays.asList("world")).get(0).getMatchCounter());
		}
		{
			final List<Bean> beans = new ArrayList<>();
			beans.add(buildBean("hello", "world"));
			assertEquals(1, beanSearcher.search(beans, limit, Arrays.asList("hello", "world")).size());
			assertEquals(3 * 1000 / 3, beanSearcher.search(beans, limit, Arrays.asList("hello", "world")).get(0).getMatchCounter());
		}

		{
			final List<Bean> beans = new ArrayList<>();
			beans.add(buildBean("hello", "world world world world world"));
			assertEquals(1, beanSearcher.search(beans, limit, Arrays.asList("world")).size());
			assertEquals(5 * 1000 / 3, beanSearcher.search(beans, limit, Arrays.asList("world")).get(0).getMatchCounter());
		}

		{
			final List<Bean> beans = new ArrayList<>();
			beans.add(buildBean("hello", "world world world world world world"));
			assertEquals(1, beanSearcher.search(beans, limit, Arrays.asList("world")).size());
			assertEquals(5 * 1000 / 3, beanSearcher.search(beans, limit, Arrays.asList("world")).get(0).getMatchCounter());
		}
	}

	private Bean buildBean(final String title, final String name) {
		final Bean bean = new Bean();
		bean.setTitle(title);
		bean.setName(name);
		return bean;
	}
}
