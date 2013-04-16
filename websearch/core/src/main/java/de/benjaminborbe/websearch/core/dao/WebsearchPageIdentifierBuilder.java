package de.benjaminborbe.websearch.core.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.api.IdentifierBuilderException;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

public class WebsearchPageIdentifierBuilder implements IdentifierBuilder<String, WebsearchPageIdentifier> {

	@Override
	public WebsearchPageIdentifier buildIdentifier(final String value) throws IdentifierBuilderException {
		return new WebsearchPageIdentifier(value);
	}

}
