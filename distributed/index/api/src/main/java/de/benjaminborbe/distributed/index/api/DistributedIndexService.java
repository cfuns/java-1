package de.benjaminborbe.distributed.index.api;

import java.util.Collection;
import java.util.Map;

public interface DistributedIndexService {

	void add(String index, String id, Map<String, Integer> data) throws DistributedIndexServiceException;

	void remove(String index, String id) throws DistributedIndexServiceException;

	DistributedIndexSearchResultIterator search(String index, Collection<String> words) throws DistributedIndexServiceException;

	Map<String, Integer> getWordRatingForEntry(String index, String url) throws DistributedIndexServiceException;

	Map<String, Integer> getEntryRatingForWord(String index, String word) throws DistributedIndexServiceException;
}
