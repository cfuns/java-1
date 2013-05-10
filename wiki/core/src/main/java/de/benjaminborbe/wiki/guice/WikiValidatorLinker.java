package de.benjaminborbe.wiki.guice;

import de.benjaminborbe.lib.validation.ValidatorRegistry;
import de.benjaminborbe.wiki.dao.WikiPageValidator;
import de.benjaminborbe.wiki.dao.WikiSpaceValidator;

import javax.inject.Inject;

public class WikiValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final WikiSpaceValidator wikiSpaceValidator, final WikiPageValidator wikiPageValidator) {
		validatorRegistry.register(wikiSpaceValidator);
		validatorRegistry.register(wikiPageValidator);
	}
}
