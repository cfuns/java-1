package de.benjaminborbe.microblog.revision;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;

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

	private String getEncoding() {
		return storageService.getEncoding();
	}

	@Override
	public MicroblogPostIdentifier getLastRevision() throws MicroblogRevisionStorageException {
		logger.trace("getLastRevision");
		try {
			final StorageValue value = storageService.get(COLUMNFAMILY, new StorageValue(ID, getEncoding()), new StorageValue(KEY, getEncoding()));
			final long result = parseUtil.parseLong(value.getString());
			logger.trace("getLastRevision - found " + result);
			return new MicroblogPostIdentifier(result);
		} catch (final ParseException e) {
			logger.trace("ParseException", e);
			return null;
		} catch (final StorageException | UnsupportedEncodingException e) {
			throw new MicroblogRevisionStorageException(e.getClass().getName(), e);
		}
	}

	@Override
	public void setLastRevision(final MicroblogPostIdentifier revision) throws MicroblogRevisionStorageException {
		logger.trace("setLastRevision to " + revision);
		try {
			storageService.set(COLUMNFAMILY, new StorageValue(ID, getEncoding()), new StorageValue(KEY, getEncoding()), new StorageValue(String.valueOf(revision), getEncoding()));
		} catch (final StorageException e) {
			logger.trace("StorageException", e);
			throw new MicroblogRevisionStorageException("StorageException", e);
		}
	}

}
