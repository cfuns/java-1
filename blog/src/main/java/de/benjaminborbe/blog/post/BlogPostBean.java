package de.benjaminborbe.blog.post;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.blog.api.BlogPost;
import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class BlogPostBean implements Entity<BlogPostIdentifier>, BlogPost, HasCreated, HasModified {

	private static final long serialVersionUID = -1631188424667532085L;

	private BlogPostIdentifier id;

	private String content;

	private String title;

	private UserIdentifier creator;

	private Calendar created;

	private Calendar modified;

	@Override
	public BlogPostIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final BlogPostIdentifier id) {
		this.id = id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setCreator(final UserIdentifier creator) {
		this.creator = creator;
	}

	@Override
	public UserIdentifier getCreator() {
		return creator;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

}
