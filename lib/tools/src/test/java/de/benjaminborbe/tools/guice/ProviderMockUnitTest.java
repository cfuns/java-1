package de.benjaminborbe.tools.guice;

import com.google.inject.Provider;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProviderMockUnitTest {

	@Test
	public void testProvider() throws Exception {
		final Provider<TestBean> provider = new ProviderMock<>(TestBean.class);
		final TestBean bean = provider.get();
		assertNotNull(bean);
		assertEquals(TestBean.class, bean.getClass());
	}
}
