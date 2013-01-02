package de.benjaminborbe.distributed.search.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;

@Singleton
public class DistributedSearchServiceImpl implements DistributedSearchService {

	private final Logger logger;

	private final DistributedSearchPageDao distributedSearchPageDao;

	private final DistributedIndexService distributedIndexService;

	private final DistributedSearchAnalyser distributedSearchAnalyser;

	@Inject
	public DistributedSearchServiceImpl(
			final Logger logger,
			final DistributedSearchPageDao distributedSearchPageDao,
			final DistributedIndexService distributedIndexService,
			final DistributedSearchAnalyser distributedSearchAnalyser) {
		this.logger = logger;
		this.distributedSearchPageDao = distributedSearchPageDao;
		this.distributedIndexService = distributedIndexService;
		this.distributedSearchAnalyser = distributedSearchAnalyser;
	}

	@Override
	public void addToIndex(final String index, final URL url, final String title, final String content) throws DistributedSearchServiceException {
		try {
			logger.debug("addToIndex - index: " + index + " url: " + url.toExternalForm() + " title: " + title + " content: " + content);
			final DistributedSearchPageBean distributedSearchPage = distributedSearchPageDao.create();
			distributedSearchPage.setId(new DistributedSearchPageIdentifier(index, url.toExternalForm()));
			distributedSearchPage.setTitle(title);
			distributedSearchPage.setContent(content);
			distributedSearchPage.setIndex(index);
			distributedSearchPageDao.save(distributedSearchPage);
			final Map<String, Integer> data = distributedSearchAnalyser.parseWordRating(content);
			distributedIndexService.add(index, url.toExternalForm(), data);
		}
		catch (final DistributedIndexServiceException e) {
			throw new DistributedSearchServiceException(e);
		}
		catch (final StorageException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

	@Override
	public List<DistributedSearchResult> search(final String index, final String searchQuery, final int limit) throws DistributedSearchServiceException {
		try {
			logger.debug("search - index: " + index + " searchQuery: " + searchQuery + " limit: " + limit);
			final Collection<String> words = distributedSearchAnalyser.parseSearchTerm(searchQuery);
			final DistributedIndexSearchResultIterator i = distributedIndexService.search(index, words);
			final List<DistributedSearchResult> result = new ArrayList<DistributedSearchResult>();
			while (i.hasNext() && result.size() < limit) {
				final DistributedSearchResult e = buildResult(i.next());
				if (e != null) {
					result.add(e);
				}
			}
			return result;
		}
		catch (final DistributedIndexServiceException e) {
			throw new DistributedSearchServiceException(e);
		}
		catch (final StorageException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

	private DistributedSearchResult buildResult(final DistributedIndexSearchResult indexResult) throws StorageException {
		final DistributedSearchPageIdentifier distributedSearchPageIdentifier = new DistributedSearchPageIdentifier(indexResult.getIndex(), indexResult.getId());
		final DistributedSearchPageBean distributedSearchPage = distributedSearchPageDao.load(distributedSearchPageIdentifier);
		if (distributedSearchPage != null) {
			return new DistributedSearchResultImpl(distributedSearchPage.getIndex(), distributedSearchPage.getId().getPageId(), distributedSearchPage.getTitle(),
					distributedSearchPage.getContent());
		}
		else {
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
		}
		catch (final StorageException e) {
			throw new DistributedSearchServiceException(e);
		}
		catch (final IdentifierIteratorException e) {
			throw new DistributedSearchServiceException(e);
		}
		catch (final DistributedIndexServiceException e) {
			throw new DistributedSearchServiceException(e);
		}
	}

}
