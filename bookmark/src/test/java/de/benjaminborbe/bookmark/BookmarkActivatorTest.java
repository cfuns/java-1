package de.benjaminborbe.bookmark;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkActivatorTest {

	@Test
	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkActivator o = injector.getInstance(BookmarkActivator.class);
		assertNotNull(o);
	}

}
