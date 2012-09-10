package de.benjaminborbe.blog.post;

import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.blog.api.BlogPostIdentifier;
import de.benjaminborbe.tools.mapper.BaseMapper;
import de.benjaminborbe.tools.mapper.MapException;

@Singleton
public class BlogPostBeanMapper extends BaseMapper<BlogPostBean> {

	private static final String ID = "id";

	private static final String TITLE = "title";

	private static final String CONTENT = "content";

	@Inject
	public BlogPostBeanMapper(final Provider<BlogPostBean> provider) {
		super(provider);
	}

	@Override
	public void map(final BlogPostBean object, final Map<String, String> data) {
		data.put(ID, toString(object.getId()));
		data.put(TITLE, object.getTitle());
		data.put(CONTENT, object.getContent());
	}

	@Override
	public void map(final Map<String, String> data, final BlogPostBean object) throws MapException {
		object.setId(toBlogPostIdentifier(data.get(ID)));
		object.setTitle(data.get(TITLE));
		object.setContent(data.get(CONTENT));
	}

	private BlogPostIdentifier toBlogPostIdentifier(final String id) {
		return id != null ? new BlogPostIdentifier(id) : null;
	}

	private String toString(final BlogPostIdentifier id) {
		return id != null ? id.getId() : null;
	}
}
