package de.benjaminborbe.microblog.api;

public interface MicroblogService {

	Long getLastRevision() throws MicroblogRevisionStorageException;

	void mailPost(long number) throws MicroblogPostMailerException;

}
