package de.benjaminborbe.wiki.guice;

import com.google.inject.Inject;

import de.benjaminborbe.tools.validation.ValidatorRegistry;
import de.benjaminborbe.wiki.dao.WikiPageValidator;
import de.benjaminborbe.wiki.dao.WikiSpaceValidator;

public class WikiValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final WikiSpaceValidator wikiSpaceValidator, final WikiPageValidator wikiPageValidator) {
		validatorRegistry.register(wikiSpaceValidator);
		validatorRegistry.register(wikiPageValidator);
	}
}
