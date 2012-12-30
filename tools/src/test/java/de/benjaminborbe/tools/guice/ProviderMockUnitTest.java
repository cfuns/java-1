package de.benjaminborbe.tools.guice;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.inject.Provider;

public class ProviderMockUnitTest {

	@Test
	public void testProvider() throws Exception {
		final Provider<TestBean> provider = new ProviderMock<TestBean>(TestBean.class);
		final TestBean bean = provider.get();
		assertNotNull(bean);
		assertEquals(TestBean.class, bean.getClass());
	}
}
