package de.benjaminborbe.microblog.revision;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class MicroblogRevisionStorageImpl implements MicroblogRevisionStorage {

	private static final String KEY = "revision";

	private static final String COLUMNFAMILY = "microblog";

	private static final String ID = "latestpost";

	private final StorageService storageService;

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public MicroblogRevisionStorageImpl(final Logger logger, final StorageService storageService, final ParseUtil parseUtil) {
		this.logger = logger;
		this.storageService = storageService;
		this.parseUtil = parseUtil;
	}

	@Override
	public MicroblogPostIdentifier getLastRevision() throws MicroblogRevisionStorageException {
		logger.trace("getLastRevision");
		try {
			final long result = parseUtil.parseLong(storageService.get(COLUMNFAMILY, ID, KEY));
			logger.trace("getLastRevision - found " + result);
			return new MicroblogPostIdentifier(result);
		}
		catch (final ParseException e) {
			logger.trace("ParseException", e);
			return null;
		}
		catch (final StorageException e) {
			logger.trace("StorageException", e);
			throw new MicroblogRevisionStorageException("StorageException", e);
		}
	}

	@Override
	public void setLastRevision(final MicroblogPostIdentifier revision) throws MicroblogRevisionStorageException {
		logger.trace("setLastRevision to " + revision);
		try {
			storageService.set(COLUMNFAMILY, ID, KEY, String.valueOf(revision));
		}
		catch (final StorageException e) {
			logger.trace("StorageException", e);
			throw new MicroblogRevisionStorageException("StorageException", e);
		}
	}

}
