package de.benjaminborbe.websearch.page;

import java.net.MalformedURLException;
import java.net.URL;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.api.IdentifierBuilderException;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

public class WebsearchPageIdentifierBuilder implements IdentifierBuilder<String, WebsearchPageIdentifier> {

	@Override
	public WebsearchPageIdentifier buildIdentifier(final String value) throws IdentifierBuilderException {
		try {
			return new WebsearchPageIdentifier(new URL(value));
		}
		catch (final MalformedURLException e) {
			throw new IdentifierBuilderException(e);
		}
	}

}
