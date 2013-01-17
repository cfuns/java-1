package de.benjaminborbe.microblog.post;

import de.benjaminborbe.microblog.api.MicroblogPost;

public interface MicroblogPostMailer {

	void mailPost(MicroblogPost post) throws MicroblogPostMailerException;

}
