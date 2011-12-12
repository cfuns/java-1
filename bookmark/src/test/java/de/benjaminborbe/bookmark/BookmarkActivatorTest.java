package de.benjaminborbe.bookmark;

import junit.framework.TestCase;

import com.google.inject.Injector;

import de.benjaminborbe.bookmark.BookmarkActivator;
import de.benjaminborbe.bookmark.guice.BookmarkModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BookmarkActivatorTest extends TestCase {

	public void testInject() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BookmarkModulesMock());
		final BookmarkActivator o = injector.getInstance(BookmarkActivator.class);
		assertNotNull(o);
	}
}
