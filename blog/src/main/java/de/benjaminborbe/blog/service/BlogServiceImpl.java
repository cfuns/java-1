package de.benjaminborbe.blog.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.authentication.api.AuthenticationService;
import de.benjaminborbe.authentication.api.AuthenticationServiceException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.blog.api.BlogPost;
import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.blog.api.BlogPostNotFoundException;
import de.benjaminborbe.blog.api.BlogService;
import de.benjaminborbe.blog.api.BlogServiceException;
import de.benjaminborbe.blog.dao.BlogPostBean;
import de.benjaminborbe.blog.dao.BlogPostDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.validation.ValidationExecutor;

@Singleton
public class BlogServiceImpl implements BlogService {

	private final Logger logger;

	private final BlogPostDao blogPostDao;

	private final AuthenticationService authenticationService;

	private final ValidationExecutor validationExecutor;

	private final CalendarUtil calendarUtil;

	@Inject
	public BlogServiceImpl(
			final Logger logger,
			final BlogPostDao blogPostDao,
			final AuthenticationService authenticationService,
			final ValidationExecutor validationExecutor,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.blogPostDao = blogPostDao;
		this.authenticationService = authenticationService;
		this.validationExecutor = validationExecutor;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public BlogPostIdentifier createBlogPost(final SessionIdentifier sessionIdentifier, final String title, final String content) throws BlogServiceException, ValidationException,
			LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);

			final BlogPostBean blogPost = blogPostDao.create();
			final BlogPostIdentifier id = createBlogPostIdentifier(sessionIdentifier, title);
			blogPost.setId(id);
			blogPost.setTitle(title);
			blogPost.setContent(content);
			blogPost.setCreator(userIdentifier);
			blogPost.setCreated(calendarUtil.now());
			blogPost.setModified(calendarUtil.now());

			final ValidationResult errors = validationExecutor.validate(blogPost);
			if (errors.hasErrors()) {
				logger.warn("BlogPost " + errors.toString());
				throw new ValidationException(errors);
			}
			else {
				blogPostDao.save(blogPost);
				logger.debug("blogPost created");
				return id;
			}
		}
		catch (final AuthenticationServiceException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final StorageException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void updateBlogPost(final SessionIdentifier sessionIdentifier, final BlogPostIdentifier blogPostIdentifier, final String title, final String content)
			throws BlogServiceException, ValidationException, LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			final BlogPostBean blogPost = blogPostDao.load(blogPostIdentifier);
			blogPost.setTitle(title);
			blogPost.setContent(content);
			blogPost.setModified(calendarUtil.now());
			if (blogPost.getCreated() == null) {
				blogPost.setCreated(calendarUtil.now());
			}
			if (blogPost.getCreator() == null) {
				final UserIdentifier userIdentifier = authenticationService.getCurrentUser(sessionIdentifier);
				blogPost.setCreator(userIdentifier);
			}
			final ValidationResult errors = validationExecutor.validate(blogPost);
			if (errors.hasErrors()) {
				logger.warn("BlogPost " + errors.toString());
				throw new ValidationException(errors);
			}
			else {
				logger.debug("blogPost updated");
				blogPostDao.save(blogPost);
			}
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
			final List<BlogPost> result = new ArrayList<BlogPost>();
			final EntityIterator<BlogPostBean> i = blogPostDao.getEntityIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final EntityIteratorException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public Collection<BlogPostIdentifier> getBlogPostIdentifiers(final SessionIdentifier sessionIdentifier) throws BlogServiceException, LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			final List<BlogPostIdentifier> result = new ArrayList<BlogPostIdentifier>();
			final IdentifierIterator<BlogPostIdentifier> i = blogPostDao.getIdentifierIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final AuthenticationServiceException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final StorageException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final IdentifierIteratorException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public BlogPost getBlogPost(final SessionIdentifier sessionIdentifier, final BlogPostIdentifier blogPostIdentifier) throws BlogServiceException, BlogPostNotFoundException,
			LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);

			final BlogPostBean result = blogPostDao.load(blogPostIdentifier);
			if (result == null) {
				throw new BlogPostNotFoundException("no blogpost with id " + blogPostIdentifier + " found");
			}
			else {
				return result;
			}
		}
		catch (final AuthenticationServiceException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final StorageException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void deleteBlogPost(final SessionIdentifier sessionIdentifier, final BlogPostIdentifier blogPostIdentifier) throws BlogServiceException, ValidationException,
			LoginRequiredException {
		try {
			authenticationService.expectLoggedIn(sessionIdentifier);
			blogPostDao.delete(blogPostIdentifier);
		}
		catch (final AuthenticationServiceException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
		catch (final StorageException e) {
			throw new BlogServiceException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public BlogPostIdentifier createBlogPostIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws BlogServiceException {
		return new BlogPostIdentifier(id);
	}

}
