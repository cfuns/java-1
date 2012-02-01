package de.benjaminborbe.microblog.util;

import de.benjaminborbe.microblog.api.MicroblogPostMailerException;

public interface MicroblogPostMailer {

	void mailPost(long number) throws MicroblogPostMailerException;
}
