package de.benjaminborbe.shortener.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.shortener.api.ShortenerService;
import de.benjaminborbe.shortener.api.ShortenerServiceException;
import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;
import de.benjaminborbe.shortener.dao.ShortenerUrlBean;
import de.benjaminborbe.shortener.dao.ShortenerUrlDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.password.PasswordCharacter;
import de.benjaminborbe.tools.password.PasswordGenerator;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.slf4j.Logger;

import java.net.URL;

@Singleton
public class ShortenerServiceImpl implements ShortenerService {

	private final Logger logger;

	private final ShortenerUrlDao shortenerUrlDao;

	private final ValidationExecutor validationExecutor;

	private final ParseUtil parseUtil;

	private final PasswordGenerator passwordGenerator;

	@Inject
	public ShortenerServiceImpl(
		final Logger logger,
		final ParseUtil parseUtil,
		final PasswordGenerator passwordGenerator,
		final ValidationExecutor validationExecutor,
		final ShortenerUrlDao shortenerUrlDao) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.passwordGenerator = passwordGenerator;
		this.validationExecutor = validationExecutor;
		this.shortenerUrlDao = shortenerUrlDao;
	}

	@Override
	public ShortenerUrlIdentifier shorten(final URL url) throws ShortenerServiceException, ValidationException {
		try {
			logger.debug("shorten url: " + url);

			final ShortenerUrlIdentifier shortenerUrlIdentifier = getUnusedIdentifier();
			final ShortenerUrlBean bean = shortenerUrlDao.create();
			bean.setId(shortenerUrlIdentifier);
			bean.setUrl(url.toExternalForm());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("Entry " + errors.toString());
				throw new ValidationException(errors);
			}
			shortenerUrlDao.save(bean);

			return shortenerUrlIdentifier;
		} catch (final StorageException e) {
			throw new ShortenerServiceException(e);
		}
	}

	private ShortenerUrlIdentifier getUnusedIdentifier() throws StorageException {
		ShortenerUrlIdentifier id;
		do {
			id = new ShortenerUrlIdentifier(passwordGenerator.generatePassword(6, PasswordCharacter.LOWER, PasswordCharacter.UPPER, PasswordCharacter.NUMBER));
		} while (shortenerUrlDao.exists(id));
		return id;
	}

	@Override
	public URL getUrl(final ShortenerUrlIdentifier shortenerUrlIdentifier) throws ShortenerServiceException {
		try {
			logger.debug("getUrl");

			final ShortenerUrlBean bean = shortenerUrlDao.load(shortenerUrlIdentifier);
			if (bean == null) {
				return null;
			}
			return parseUtil.parseURL(bean.getUrl());
		} catch (final StorageException | ParseException e) {
			throw new ShortenerServiceException(e);
		} finally {
		}
	}

}
