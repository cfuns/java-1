package de.benjaminborbe.blog.dao;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class BlogPostDaoStorage extends DaoStorage<BlogPostBean, BlogPostIdentifier> implements BlogPostDao {

	private static final String COLUMN_FAMILY = "blog_post";

	@Inject
	public BlogPostDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<BlogPostBean> beanProvider,
			final BlogPostBeanMapper mapper,
			final BlogPostIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
