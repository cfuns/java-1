package de.benjaminborbe.wiki.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import de.benjaminborbe.tools.validation.ValidationResultImpl;
import de.benjaminborbe.wiki.api.WikiPage;
import de.benjaminborbe.wiki.api.WikiPageContentType;
import de.benjaminborbe.wiki.api.WikiPageIdentifier;
import de.benjaminborbe.wiki.api.WikiPageNotFoundException;
import de.benjaminborbe.wiki.api.WikiService;
import de.benjaminborbe.wiki.api.WikiServiceException;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;
import de.benjaminborbe.wiki.api.WikiSpaceNotFoundException;
import de.benjaminborbe.wiki.dao.WikiPageBean;
import de.benjaminborbe.wiki.dao.WikiPageDao;
import de.benjaminborbe.wiki.dao.WikiSpaceBean;
import de.benjaminborbe.wiki.dao.WikiSpaceDao;
import de.benjaminborbe.wiki.render.WikiRenderer;
import de.benjaminborbe.wiki.render.WikiRendererFactory;
import de.benjaminborbe.wiki.render.WikiRendererNotFoundException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Singleton
public class WikiServiceImpl implements WikiService {

	private final Logger logger;

	private final WikiSpaceDao wikiSpaceDao;

	private final WikiPageDao wikiPageDao;

	private final WikiRendererFactory wikiRendererFactory;

	private final ValidationExecutor validationExecutor;

	@Inject
	public WikiServiceImpl(
		final Logger logger,
		final WikiSpaceDao wikiSpaceDao,
		final WikiPageDao wikiPageDao,
		final WikiRendererFactory wikiRendererFactory,
		final ValidationExecutor validationExecutor) {
		this.logger = logger;
		this.wikiSpaceDao = wikiSpaceDao;
		this.wikiPageDao = wikiPageDao;
		this.wikiRendererFactory = wikiRendererFactory;
		this.validationExecutor = validationExecutor;
	}

	@Override
	public Collection<WikiSpaceIdentifier> getSpaceIdentifiers() throws WikiServiceException {
		logger.trace("getSpaceIdentifiers");
		try {
			final List<WikiSpaceIdentifier> result = new ArrayList<>();
			final IdentifierIterator<WikiSpaceIdentifier> i = wikiSpaceDao.getIdentifierIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		} catch (final StorageException | IdentifierIteratorException e) {
			throw new WikiServiceException(e);
		}
	}

	@Override
	public Collection<WikiPageIdentifier> getPageIdentifiers(final WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException {
		logger.trace("getPageIdentifiers");
		try {
			final List<WikiPageIdentifier> result = new ArrayList<>();
			final EntityIterator<WikiPageBean> i = wikiPageDao.getEntityIterator();
			while (i.hasNext()) {
				final WikiPageBean bean = i.next();
				if (bean.getSpace().equals(wikiSpaceIdentifier)) {
					result.add(bean.getId());
				}
			}
			return result;
		} catch (final StorageException | EntityIteratorException e) {
			throw new WikiServiceException(e);
		}
	}

	@Override
	public WikiPage getPage(final WikiPageIdentifier wikiPageIdentifier) throws WikiServiceException, WikiPageNotFoundException {
		try {
			logger.trace("getPage");
			final WikiPageBean result = wikiPageDao.load(wikiPageIdentifier);
			if (result == null) {
				throw new WikiPageNotFoundException("wiki page " + wikiPageIdentifier + " not found");
			}
			return result;
		} catch (final StorageException e) {
			throw new WikiServiceException(e);
		}
	}

	@Override
	public WikiSpaceIdentifier getSpaceByName(final String spaceName) throws WikiServiceException {
		logger.trace("getSpaceByName");
		try {
			if (wikiSpaceDao.existsSpaceWithName(spaceName)) {
				return new WikiSpaceIdentifier(spaceName);
			} else {
				throw new WikiSpaceNotFoundException("space with name " + spaceName + " does not exists");
			}
		} catch (final StorageException e) {
			throw new WikiServiceException(e);
		}
	}

	@Override
	public WikiPage getPageByName(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageName) throws WikiServiceException, WikiPageNotFoundException {
		logger.trace("getPageByName");
		final WikiPageIdentifier wikiPageIdentifier = getPageIdentifierByName(wikiSpaceIdentifier, pageName);
		return getPage(wikiPageIdentifier);
	}

	@Override
	public WikiPageIdentifier getPageIdentifierByName(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageName) {
		logger.trace("getPageIdentifierByName");
		return new WikiPageIdentifier(wikiSpaceIdentifier.getId() + "-" + pageName);
	}

	@Override
	public WikiPageIdentifier createPage(final WikiSpaceIdentifier wikiSpaceIdentifier, final String pageTitle, final String pageContent) throws ValidationException,
		WikiServiceException {
		try {
			logger.debug("createPage");

			if (!wikiSpaceDao.exists(wikiSpaceIdentifier)) {
				final ValidationResultImpl validationResult = new ValidationResultImpl(new ValidationErrorSimple("space " + wikiSpaceIdentifier + " not found"));
				throw new ValidationException(validationResult);
			}

			final WikiPageIdentifier wikiPageIdentifier = getPageIdentifierByName(wikiSpaceIdentifier, pageTitle);

			if (wikiPageDao.exists(wikiPageIdentifier)) {
				final ValidationResultImpl validationResult = new ValidationResultImpl(new ValidationErrorSimple("page " + wikiPageIdentifier + " already exists"));
				throw new ValidationException(validationResult);
			}

			final WikiPageBean wikiPage = wikiPageDao.create();
			wikiPage.setId(wikiPageIdentifier);
			wikiPage.setTitle(pageTitle);
			wikiPage.setContent(pageContent);
			wikiPage.setContentType(WikiPageContentType.CONFLUENCE);
			wikiPage.setSpace(wikiSpaceIdentifier);

			final ValidationResult errors = validationExecutor.validate(wikiPage);
			if (errors.hasErrors()) {
				logger.warn(wikiPage.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			wikiPageDao.save(wikiPage);

			return wikiPageIdentifier;
		} catch (final StorageException e) {
			throw new WikiServiceException(e);
		}
	}

	@Override
	public void updatePage(final WikiPageIdentifier wikiPageIdentifier, final String pageTitle, final String pageContent) throws WikiServiceException, ValidationException {
		try {
			logger.trace("updatePage");

			if (!wikiPageDao.exists(wikiPageIdentifier)) {
				final ValidationResultImpl validationResult = new ValidationResultImpl(new ValidationErrorSimple("page " + wikiPageIdentifier + " not found"));
				throw new ValidationException(validationResult);
			}

			final WikiPageBean wikiPage = wikiPageDao.load(wikiPageIdentifier);
			wikiPage.setTitle(pageTitle);
			wikiPage.setContent(pageContent);

			final ValidationResult errors = validationExecutor.validate(wikiPage);
			if (errors.hasErrors()) {
				logger.warn(wikiPage.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			wikiPageDao.save(wikiPage);
		} catch (final StorageException e) {
			throw new WikiServiceException(e);
		}
	}

	@Override
	public void deletePage() {
		logger.trace("deletePage");
	}

	@Override
	public WikiSpaceIdentifier createSpace(final String spaceIdentifier, final String spaceTitle) throws WikiServiceException, ValidationException {
		try {
			logger.debug("createSpace");

			if (wikiSpaceDao.existsSpaceWithName(spaceIdentifier)) {
				final ValidationResultImpl validationResult = new ValidationResultImpl(new ValidationErrorSimple("space " + spaceIdentifier + " already exists"));
				throw new ValidationException(validationResult);
			}

			final WikiSpaceIdentifier id = new WikiSpaceIdentifier(spaceIdentifier);
			final WikiSpaceBean wikiSpace = wikiSpaceDao.create();
			wikiSpace.setId(id);
			wikiSpace.setName(spaceTitle);

			final ValidationResult errors = validationExecutor.validate(wikiSpace);
			if (errors.hasErrors()) {
				logger.warn(wikiSpace.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}

			wikiSpaceDao.save(wikiSpace);

			return id;
		} catch (final StorageException e) {
			throw new WikiServiceException(e);
		}
	}

	@Override
	public void deleteSpace(final WikiSpaceIdentifier wikiSpaceIdentifier) throws WikiServiceException {
		try {
			logger.trace("deleteSpace");
			wikiSpaceDao.delete(wikiSpaceIdentifier);
		} catch (final StorageException e) {
			throw new WikiServiceException(e);
		}
	}

	@Override
	public String renderPage(final WikiPageIdentifier wikiPageIdentifier) throws WikiServiceException, WikiPageNotFoundException {
		final WikiPage page = getPage(wikiPageIdentifier);
		final String content = page.getContent();
		final WikiPageContentType contentType = page.getContentType();
		try {
			final WikiRenderer renderer = wikiRendererFactory.getRenderer(contentType);
			return renderer.render(content);
		} catch (final WikiRendererNotFoundException e) {
			throw new WikiServiceException(e);
		}
	}

	@Override
	public WikiPageIdentifier createPageIdentifier(final String id) throws WikiServiceException {
		return new WikiPageIdentifier(id);
	}

	@Override
	public WikiSpaceIdentifier createSpaceIdentifier(final String id) throws WikiServiceException {
		return new WikiSpaceIdentifier(id);
	}
}
