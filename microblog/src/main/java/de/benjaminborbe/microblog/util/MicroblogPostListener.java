package de.benjaminborbe.microblog.util;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;

public interface MicroblogPostListener {

	void onNewPost(MicroblogPostIdentifier microblogPostIdentifier);

}
