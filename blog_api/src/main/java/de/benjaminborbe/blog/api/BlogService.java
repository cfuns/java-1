package de.benjaminborbe.blog.api;

import java.util.Collection;
import java.util.List;

import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface BlogService {

	Collection<BlogPostIdentifier> getBlogPostIdentifiers(SessionIdentifier sessionIdentifier) throws BlogServiceException;

	BlogPost getBlogPost(SessionIdentifier sessionIdentifier, BlogPostIdentifier blogPostIdentifier) throws BlogServiceException, BlogPostNotFoundException;

	List<BlogPost> getLatestBlogPosts(SessionIdentifier sessionIdentifier) throws BlogServiceException;

	BlogPostIdentifier createBlogPost(SessionIdentifier sessionIdentifier, String title, String content) throws BlogServiceException, BlogPostCreationException,
			LoginRequiredException;

	void updateBlogPost(SessionIdentifier sessionIdentifier, BlogPostIdentifier blogPostIdentifier, String title, String content) throws BlogServiceException,
			BlogPostUpdateException, LoginRequiredException;

	void deleteBlogPost(SessionIdentifier sessionIdentifier, BlogPostIdentifier blogPostIdentifier) throws BlogServiceException, BlogPostDeleteException, LoginRequiredException;

	BlogPostIdentifier createBlogPostIdentifier(SessionIdentifier sessionIdentifier, String id) throws BlogServiceException;

}
