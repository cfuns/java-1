package de.benjaminborbe.bookmark.guice;

import de.benjaminborbe.bookmark.dao.BookmarkValidator;
import de.benjaminborbe.lib.validation.ValidatorRegistry;

import javax.inject.Inject;

public class BookmarkValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final BookmarkValidator bookmarkValidator) {
		validatorRegistry.register(bookmarkValidator);
	}
}
