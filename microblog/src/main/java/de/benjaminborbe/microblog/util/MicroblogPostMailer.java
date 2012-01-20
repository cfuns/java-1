package de.benjaminborbe.microblog.util;

public interface MicroblogPostMailer {

	void mailPost(long number) throws MicroblogPostMailerException;
}
