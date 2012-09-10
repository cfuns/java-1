package de.benjaminborbe.blog.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.blog.api.BlogPost;
import de.benjaminborbe.blog.api.BlogPostCreationException;
import de.benjaminborbe.blog.api.BlogPostDeleteException;
import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.blog.api.BlogPostNotFoundException;
import de.benjaminborbe.blog.api.BlogPostUpdateException;
import de.benjaminborbe.blog.api.BlogService;
import de.benjaminborbe.blog.api.BlogServiceException;
import de.benjaminborbe.blog.post.BlogPostBean;
import de.benjaminborbe.blog.post.BlogPostDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class BlogServiceImpl implements BlogService {

	private final Logger logger;

	private final BlogPostDao blogPostDao;

	private final AuthenticationService authenticationService;

	private final ValidationExecutor validationExecutor;

	@Inject
	public BlogServiceImpl(final Logger logger, final BlogPostDao blogPostDao, final AuthenticationService authenticationService, final ValidationExecutor validationExecutor) {
		this.logger = logger;
		this.blogPostDao = blogPostDao;
		this.authenticationService = authenticationService;
		this.validationExecutor = validationExecutor;
	}

	@Override
	public BlogPostIdentifier createBlogPost(final SessionIdentifier sessionIdentifier, final String title, final String content) throws BlogServiceException,
			BlogPostCreationException, LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			final BlogPostBean blogPost = blogPostDao.create();
			final BlogPostIdentifier id = createBlogPostIdentifier(sessionIdentifier, title);
			blogPost.setId(id);
			blogPost.setTitle(title);
			blogPost.setContent(content);
			blogPost.setCreator(userIdentifier);

			final ValidationResult errors = validationExecutor.validate(blogPost);
			if (errors.hasErrors()) {
				logger.warn("BlogPost " + errors.toString());
				throw new BlogPostCreationException(errors);
			}
			blogPostDao.save(blogPost);
			return id;
		}
		catch (final AuthenticationServiceException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final StorageException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public List<BlogPost> getLatestBlogPosts(final SessionIdentifier sessionIdentifier) throws BlogServiceException {
		try {
			logger.debug("getLatestBlogPosts");
			return new ArrayList<BlogPost>(blogPostDao.getAll());
		}
		catch (final StorageException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Collection<BlogPostIdentifier> getBlogPostIdentifiers(final SessionIdentifier sessionIdentifier) throws BlogServiceException {
		return null;
	}

	@Override
	public BlogPost getBlogPost(final SessionIdentifier sessionIdentifier, final BlogPostIdentifier blogPostIdentifier) throws BlogServiceException, BlogPostNotFoundException {
		return null;
	}

	@Override
	public void updateBlogPost(final SessionIdentifier sessionIdentifier, final BlogPostIdentifier blogPostIdentifier, final String title, final String content)
			throws BlogServiceException, BlogPostUpdateException, LoginRequiredException {
	}

	@Override
	public void deleteBlogPost(final SessionIdentifier sessionIdentifier, final BlogPostIdentifier blogPostIdentifier) throws BlogServiceException, BlogPostDeleteException,
			LoginRequiredException {
	}

	@Override
	public BlogPostIdentifier createBlogPostIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws BlogServiceException {
		return new BlogPostIdentifier(id);
	}

}
