package de.benjaminborbe.microblog.post;

import de.benjaminborbe.microblog.api.MicroblogPost;

public interface MicroblogPostNotifier {

	void mailPost(MicroblogPost post) throws MicroblogPostNotifierException;

}
