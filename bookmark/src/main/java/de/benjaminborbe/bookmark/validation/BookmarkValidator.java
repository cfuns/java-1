package de.benjaminborbe.bookmark.validation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.benjaminborbe.bookmark.dao.BookmarkBean;
import de.benjaminborbe.tools.validation.ValidationError;
import de.benjaminborbe.tools.validation.Validator;

public class BookmarkValidator implements Validator<BookmarkBean> {

	@Override
	public Class<BookmarkBean> getType() {
		return BookmarkBean.class;
	}

	@Override
	public Collection<ValidationError> validate(final Object object) {
		final BookmarkBean bookmark = (BookmarkBean) object;
		final Set<ValidationError> result = new HashSet<ValidationError>();

		// validate name
		final String name = bookmark.getName();
		{
			if (name == null || name.length() == 0) {
				result.add(new ValidationError());
			}
		}

		// validate url
		{
			try {
				new URL(bookmark.getUrl());
			}
			catch (final MalformedURLException e) {
				result.add(new ValidationError());
			}
		}

		return result;
	}

}
