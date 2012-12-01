package de.benjaminborbe.bookmark.guice;

import com.google.inject.Inject;

import de.benjaminborbe.bookmark.dao.BookmarkValidator;
import de.benjaminborbe.tools.validation.ValidatorRegistry;

public class BookmarkValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final BookmarkValidator bookmarkValidator) {
		validatorRegistry.register(bookmarkValidator);
	}
}
