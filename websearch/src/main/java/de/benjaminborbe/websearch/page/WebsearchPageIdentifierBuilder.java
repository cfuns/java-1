package de.benjaminborbe.websearch.page;

import java.net.MalformedURLException;
import java.net.URL;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.api.IdentifierBuilderException;
import de.benjaminborbe.websearch.api.PageIdentifier;

public class WebsearchPageIdentifierBuilder implements IdentifierBuilder<String, PageIdentifier> {

	@Override
	public PageIdentifier buildIdentifier(final String value) throws IdentifierBuilderException {
		try {
			return new PageIdentifier(new URL(value));
		}
		catch (final MalformedURLException e) {
			throw new IdentifierBuilderException(e);
		}
	}

}
