package de.benjaminborbe.blog.mock;

import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.blog.api.BlogPost;
import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.blog.api.BlogPostNotFoundException;
import de.benjaminborbe.blog.api.BlogService;
import de.benjaminborbe.blog.api.BlogServiceException;

@Singleton
public class BlogServiceMock implements BlogService {

	@Inject
	public BlogServiceMock() {
	}

	@Override
	public BlogPostIdentifier createBlogPost(final SessionIdentifier sessionIdentifier, final String title, final String content) {
		return null;
	}

	@Override
	public List<BlogPost> getLatestBlogPosts(final SessionIdentifier sessionIdentifier) throws BlogServiceException {
		return null;
	}

	@Override
	public void updateBlogPost(final SessionIdentifier sessionIdentifier, final BlogPostIdentifier blogPostIdentifier, final String title, final String content)
			throws BlogServiceException, ValidationException, LoginRequiredException {
	}

	@Override
	public void deleteBlogPost(final SessionIdentifier sessionIdentifier, final BlogPostIdentifier blogPostIdentifier) throws BlogServiceException, ValidationException,
			LoginRequiredException {
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
	public BlogPostIdentifier createBlogPostIdentifier(final SessionIdentifier sessionIdentifier, final String id) throws BlogServiceException {
		return null;
	}

}
