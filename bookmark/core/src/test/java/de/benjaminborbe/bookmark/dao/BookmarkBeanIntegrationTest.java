package de.benjaminborbe.bookmark.dao;

import com.google.inject.Injector;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class BookmarkBeanIntegrationTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkBean bookmarkBean = injector.getInstance(BookmarkBean.class);
		assertNotNull(bookmarkBean);
	}

}
