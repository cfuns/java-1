package de.benjaminborbe.microblog.post;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;

public interface MicroblogPostMailer {

	void mailPost(MicroblogPostIdentifier number) throws MicroblogPostMailerException;
}
