package de.benjaminborbe.blog.post;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;

@Singleton
public class BlogPostDaoStorage extends DaoStorage<BlogPostBean, BlogPostIdentifier> implements BlogPostDao {

	private static final String COLUMN_FAMILY = "blog_post";

	@Inject
	public BlogPostDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<BlogPostBean> beanProvider,
			final BlogPostBeanMapper mapper,
			final BlogPostIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
