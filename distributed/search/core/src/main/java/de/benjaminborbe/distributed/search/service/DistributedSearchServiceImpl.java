package de.benjaminborbe.distributed.search.service;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResultIterator;
import de.benjaminborbe.distributed.index.api.DistributedIndexService;
import de.benjaminborbe.distributed.index.api.DistributedIndexServiceException;
import de.benjaminborbe.distributed.search.api.DistributedSearchResult;
import de.benjaminborbe.distributed.search.api.DistributedSearchService;
import de.benjaminborbe.distributed.search.api.DistributedSearchServiceException;
import de.benjaminborbe.distributed.search.dao.DistributedSearchPageBean;
import de.benjaminborbe.distributed.search.dao.DistributedSearchPageDao;
import de.benjaminborbe.distributed.search.dao.DistributedSearchPageIdentifier;
import de.benjaminborbe.distributed.search.util.DistributedSearchAnalyser;
import de.benjaminborbe.distributed.search.util.DistributedSearchResultImpl;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class DistributedSearchServiceImpl implements DistributedSearchService {

	private final Logger logger;

	private final DistributedSearchPageDao distributedSearchPageDao;

	private final DistributedIndexService distributedIndexService;

	private final DistributedSearchAnalyser distributedSearchAnalyser;

	private final ValidationExecutor validationExecutor;

	private final CalendarUtil calendarUtil;

	@Inject
	public DistributedSearchServiceImpl(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final ValidationExecutor validationExecutor,
		final DistributedSearchPageDao distributedSearchPageDao,
		final DistributedIndexService distributedIndexService,
		final DistributedSearchAnalyser distributedSearchAnalyser
	) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.validationExecutor = validationExecutor;
		this.distributedSearchPageDao = distributedSearchPageDao;
		this.distributedIndexService = distributedIndexService;
		this.distributedSearchAnalyser = distributedSearchAnalyser;
	}

	@Override
	public void addToIndex(
		final String index,
		final URL url,
		final String title,
		final String content,
		final Calendar date
	) throws DistributedSearchServiceException {
		try {
			logger
				.debug("addToIndex - index: " + index + " url: " + url.toExternalForm() + " date: " + calendarUtil.toDateTimeString(date) + " title: " + title + " content: " + content);
			final DistributedSearchPageIdentifier id = new DistributedSearchPageIdentifier(index, url.toExternalForm());
			final DistributedSearchPageBean distributedSearchPage = distributedSearchPageDao.findOrCreate(id);
			distributedSearchPage.setId(id);
			distributedSearchPage.setTitle(title);
			distributedSearchPage.setContent(content);
			distributedSearchPage.setIndex(index);
			if (date != null) {
				distributedSearchPage.setDate(date);
			} else {
				distributedSearchPage.setDate(calendarUtil.now());
			}

			final ValidationResult errors = validationExecutor.validate(distributedSearchPage);
			if (errors.hasErrors()) {
				logger.warn(distributedSearchPage.getClass().getSimpleName() + " " + errors.toString());
				throw new ValidationException(errors);
			}
			distributedSearchPageDao.save(distributedSearchPage);

			distributedIndexService.add(index, url.toExternalForm(), buildData(distributedSearchPage));
		} catch (final DistributedIndexServiceException | ValidationException | StorageException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

	@Override
	public List<DistributedSearchResult> search(final String index, final String searchQuery, final int limit) throws DistributedSearchServiceException {
		try {
			logger.debug("search - index: " + index + " searchQuery: " + searchQuery + " limit: " + limit);
			final Collection<String> words = distributedSearchAnalyser.parseSearchTerm(searchQuery);
			final DistributedIndexSearchResultIterator resultIterator = distributedIndexService.search(index, words);
			final List<DistributedSearchResult> result = new ArrayList<>();
			while (resultIterator.hasNext() && result.size() < limit) {
				final DistributedSearchResult e = buildResult(resultIterator.next());
				if (e != null) {
					result.add(e);
				}
			}
			return result;
		} catch (final DistributedIndexServiceException | StorageException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

	private DistributedSearchResult buildResult(final DistributedIndexSearchResult indexResult) throws StorageException {
		final DistributedSearchPageIdentifier distributedSearchPageIdentifier = new DistributedSearchPageIdentifier(indexResult.getIndex(), indexResult.getId());
		final DistributedSearchPageBean distributedSearchPage = distributedSearchPageDao.load(distributedSearchPageIdentifier);
		if (distributedSearchPage != null) {
			return buildResult(distributedSearchPage);
		} else {
			return null;
		}
	}

	private DistributedSearchResult buildResult(final DistributedSearchPageBean distributedSearchPage) {
		if (distributedSearchPage != null) {
			return new DistributedSearchResultImpl(distributedSearchPage.getIndex(), distributedSearchPage.getId().getPageId(), distributedSearchPage.getTitle(),
				distributedSearchPage.getContent(), distributedSearchPage.getDate(), distributedSearchPage.getCreated(), distributedSearchPage.getModified());
		} else {
			return null;
		}
	}

	@Override
	public void clear(final String index) throws DistributedSearchServiceException {
		try {
			logger.debug("clear - index: " + index);
			final IdentifierIterator<DistributedSearchPageIdentifier> i = distributedSearchPageDao.getIdentifierIteratorByIndex(index);
			while (i.hasNext()) {
				final DistributedSearchPageIdentifier id = i.next();
				logger.debug("clear - delete: " + id);
				distributedSearchPageDao.delete(id);
				distributedIndexService.remove(index, id.getPageId());
			}
			logger.debug("clear - finished");
		} catch (final StorageException | DistributedIndexServiceException | IdentifierIteratorException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

	@Override
	public void removeFromIndex(final String index, final URL url) throws DistributedSearchServiceException {
		try {
			distributedIndexService.remove(index, url.toExternalForm());
			distributedSearchPageDao.delete(new DistributedSearchPageIdentifier(index, url.toExternalForm()));
		} catch (final StorageException | DistributedIndexServiceException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

	@Override
	public void rebuildAll() throws DistributedSearchServiceException {
		try {
			logger.debug("rebuildAll");

			final EntityIterator<DistributedSearchPageBean> distributedSearchPageIterator = distributedSearchPageDao.getEntityIterator();
			while (distributedSearchPageIterator.hasNext()) {
				final DistributedSearchPageBean distributedSearchPage = distributedSearchPageIterator.next();
				rebuildPage(distributedSearchPage);
			}

			logger.debug("rebuildIndex - finished");
		} catch (final StorageException | EntityIteratorException | DistributedIndexServiceException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

	@Override
	public void rebuildIndex(final String index) throws DistributedSearchServiceException {
		try {
			logger.debug("rebuildIndex - index: " + index);

			final EntityIterator<DistributedSearchPageBean> distributedSearchPageIterator = distributedSearchPageDao.getEntityIteratorByIndex(index);
			while (distributedSearchPageIterator.hasNext()) {
				final DistributedSearchPageBean distributedSearchPage = distributedSearchPageIterator.next();
				rebuildPage(distributedSearchPage);
			}

			logger.debug("rebuildIndex - finished");
		} catch (final StorageException | EntityIteratorException | DistributedIndexServiceException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

	public void rebuildPage(final DistributedSearchPageBean distributedSearchPage) throws DistributedIndexServiceException {
		logger.debug("rebuildPage - remove url from index: " + distributedSearchPage.getId());
		distributedIndexService.remove(distributedSearchPage.getIndex(), distributedSearchPage.getId().getPageId());

		logger.debug("rebuildPage - add url from index: " + distributedSearchPage.getId());
		distributedIndexService.add(distributedSearchPage.getIndex(), distributedSearchPage.getId().getPageId(), buildData(distributedSearchPage));
	}

	@Override
	public DistributedSearchResult getPage(final String index, final String url) throws DistributedSearchServiceException {
		try {
			logger.debug("getPage - index: " + index + " url: " + url);
			final DistributedSearchPageIdentifier id = new DistributedSearchPageIdentifier(index, url);
			final DistributedSearchPageBean distributedSearchPage = distributedSearchPageDao.load(id);
			return buildResult(distributedSearchPage);
		} catch (final StorageException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

	private Map<String, Integer> buildData(final DistributedSearchPageBean bean) {
		final Map<String, Integer> data = new HashMap<>();
		distributedSearchAnalyser.parseWordRating(bean.getId().getPageId(), 3, data);
		distributedSearchAnalyser.parseWordRating(bean.getTitle(), 3, data);
		distributedSearchAnalyser.parseWordRating(bean.getContent(), 1, data);
		return data;
	}

	@Override
	public void rebuildPage(final String index, final String url) throws DistributedSearchServiceException {
		try {
			logger.debug("rebuildPage - index: " + index + " url: " + url);
			final DistributedSearchPageIdentifier distributedSearchPageIdentifier = new DistributedSearchPageIdentifier(index, url);
			final DistributedSearchPageBean distributedSearchPage = distributedSearchPageDao.load(distributedSearchPageIdentifier);
			rebuildPage(distributedSearchPage);
		} catch (final DistributedIndexServiceException | StorageException e) {
			throw new DistributedSearchServiceException(e);
		}
	}
}
