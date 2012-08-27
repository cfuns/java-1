package de.benjaminborbe.wiki.service;

import java.util.Collection;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.wiki.api.WikiPage;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiServiceException;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;
import de.benjaminborbe.wiki.api.WikiSpaceNotFoundException;
import de.benjaminborbe.wiki.dao.WikiPageDao;
import de.benjaminborbe.wiki.dao.WikiSpaceBean;
import de.benjaminborbe.wiki.dao.WikiSpaceDao;

@Singleton
public class WikiServiceImpl implements WikiService {

	private final Logger logger;

	private final WikiSpaceDao wikiSpaceDao;

	private final WikiPageDao wikiPageDao;

	@Inject
	public WikiServiceImpl(final Logger logger, final WikiSpaceDao wikiSpaceDao, final WikiPageDao wikiPageDao) {
		this.logger = logger;
		this.wikiSpaceDao = wikiSpaceDao;
		this.wikiPageDao = wikiPageDao;
	}

	@Override
	public Collection<WikiSpaceIdentifier> getSpaceIdentifiers() throws WikiServiceException {
		logger.trace("getSpaceIdentifiers");
		try {
			return wikiSpaceDao.getIdentifiers();
		}
		catch (final StorageException e) {
			throw new WikiServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<WikiPageIdentifier> getPageIdentifiers(final WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException {
		logger.trace("getPageIdentifiers");
		try {
			return wikiPageDao.getIdentifiers();
		}
		catch (final StorageException e) {
			throw new WikiServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public WikiPage getPage(final WikiPageIdentifier wikiPageIdentifier) {
		logger.trace("getPage");
		return null;
	}

	@Override
	public WikiSpaceIdentifier getSpaceByName(final String spaceName) throws WikiServiceException {
		logger.trace("getSpaceByName");
		try {
			if (wikiSpaceDao.existsSpaceWithName(spaceName)) {
				return new WikiSpaceIdentifier(spaceName);
			}
			else {
				throw new WikiSpaceNotFoundException("space with name " + spaceName + " does not exists");
			}
		}
		catch (final StorageException e) {
			throw new WikiServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public WikiPage getPageByName(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageName) {
		logger.trace("getPageByName");
		return null;
	}

	@Override
	public WikiPageIdentifier createPage(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageTitle, final String pageContent) {
		logger.trace("createPage");
		return null;
	}

	@Override
	public boolean updatePage(final WikiPageIdentifier wikiPageIdentifier, final String pageTitle, final String pageContent) {
		logger.trace("updatePage");
		return false;
	}

	@Override
	public boolean deletePage(final WikiPageIdentifier wikiPageIdentifier) {
		logger.trace("deletePage");
		return false;
	}

	@Override
	public WikiSpaceIdentifier createSpace(final String spaceIdentifier, final String spaceTitle) throws WikiServiceException {
		try {
			logger.trace("createSpace");

			if (wikiSpaceDao.existsSpaceWithName(spaceIdentifier)) {
				throw new WikiServiceException("space " + spaceIdentifier + " already exists");
			}

			final WikiSpaceIdentifier id = new WikiSpaceIdentifier(spaceIdentifier);
			final WikiSpaceBean wikiSpace = wikiSpaceDao.create();
			wikiSpace.setId(id);
			wikiSpace.setName(spaceTitle);
			wikiSpaceDao.save(wikiSpace);

			return id;
		}
		catch (final StorageException e) {
			throw new WikiServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public boolean deleteSpace(final WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException {
		try {
			logger.trace("deleteSpace");
			wikiSpaceDao.delete(wikiSpaceIdentifier);
			return true;
		}
		catch (final StorageException e) {
			throw new WikiServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public String renderPage(final WikiPageIdentifier wikiPageIdentifier) throws WikiServiceException {
		return null;
	}
}
