package de.benjaminborbe.blog.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;

import java.util.Collection;
import java.util.List;

public interface BlogService {

	String PERMISSION = "blog";

	Collection<BlogPostIdentifier> getBlogPostIdentifiers(SessionIdentifier sessionIdentifier) throws BlogServiceException, LoginRequiredException;

	BlogPost getBlogPost(SessionIdentifier sessionIdentifier, BlogPostIdentifier blogPostIdentifier) throws BlogServiceException, BlogPostNotFoundException, LoginRequiredException;

	List<BlogPost> getLatestBlogPosts() throws BlogServiceException;

	BlogPostIdentifier createBlogPost(SessionIdentifier sessionIdentifier, String title, String content) throws BlogServiceException, ValidationException, LoginRequiredException;

	void updateBlogPost(SessionIdentifier sessionIdentifier, BlogPostIdentifier blogPostIdentifier, String title, String content) throws BlogServiceException, ValidationException,
		LoginRequiredException;

	void deleteBlogPost(SessionIdentifier sessionIdentifier, BlogPostIdentifier blogPostIdentifier) throws BlogServiceException, ValidationException, LoginRequiredException;

	BlogPostIdentifier createBlogPostIdentifier(String id) throws BlogServiceException;

}
