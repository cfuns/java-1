package de.benjaminborbe.bookmark.dao;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkBeanIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkBean bookmarkBean = injector.getInstance(BookmarkBean.class);
		assertNotNull(bookmarkBean);
	}

}
