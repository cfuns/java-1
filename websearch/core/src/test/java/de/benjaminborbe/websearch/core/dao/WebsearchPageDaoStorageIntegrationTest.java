package de.benjaminborbe.websearch.core.dao;

import com.google.inject.Injector;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.websearch.core.guice.WebsearchModulesMock;
import org.junit.Test;

import java.net.URL;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class WebsearchPageDaoStorageIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchModulesMock());
		assertThat(injector.getInstance(WebsearchPageDaoStorage.class), is(notNullValue()));
	}

	@Test
	public void testFindOrCreate() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new WebsearchModulesMock());
		final WebsearchPageDaoStorage websearchPageDaoStorage = injector.getInstance(WebsearchPageDaoStorage.class);
		final URL url = new URL("http://www.benjamin-borbe.de");
		long depth = 0;
		int timeout = 5000;
		final WebsearchPageBean websearchPageBean = websearchPageDaoStorage.findOrCreate(url, depth, timeout);
		assertThat(websearchPageBean, is(notNullValue()));
		assertThat(websearchPageBean.getUrl(), is(url));
		assertThat(websearchPageBean.getId(), is(notNullValue()));
		assertThat(websearchPageBean.getId().getId(), is(url.toExternalForm()));
	}

}
