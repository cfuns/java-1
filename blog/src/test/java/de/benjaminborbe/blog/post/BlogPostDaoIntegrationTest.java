package de.benjaminborbe.blog.post;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.blog.dao.BlogPostDao;
import de.benjaminborbe.blog.dao.BlogPostDaoStorage;
import de.benjaminborbe.blog.guice.BlogModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class BlogPostDaoIntegrationTest {

	@Test
	public void testInject() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new BlogModulesMock());
		final BlogPostDao dao = injector.getInstance(BlogPostDao.class);
		assertNotNull(dao);
		assertEquals(BlogPostDaoStorage.class, dao.getClass());
	}
}
