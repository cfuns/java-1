package de.benjaminborbe.googlesearch.service;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.json.JSONArray;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONParseException;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;
import de.benjaminborbe.tools.search.SearchUtil;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class GoogleSearchServiceComponent implements SearchServiceComponent {

	private static final String PREFIX = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";

	private static final String SEARCH_TYPE = "Google";

	private static final int TIMEOUT = 1000;

	private final Logger logger;

	private final HtmlUtil htmlUtil;

	private final UrlUtil urlUtil;

	private final SearchUtil searchUtil;

	private final JSONParser jsonParser;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	@Inject
	public GoogleSearchServiceComponent(
		final Logger logger,
		final SearchUtil searchUtil,
		final HtmlUtil htmlUtil,
		final UrlUtil urlUtil,
		final JSONParser jsonParser,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil
	) {
		this.logger = logger;
		this.searchUtil = searchUtil;
		this.htmlUtil = htmlUtil;
		this.urlUtil = urlUtil;
		this.jsonParser = jsonParser;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults) {
		final List<String> words = searchUtil.buildSearchParts(query);
		logger.trace("search");
		final List<SearchResult> result = new ArrayList<SearchResult>();
		// https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=foo
		try {
			final URL url = buildQueryUrl(words);
			final String content = downloadContent(url);
			// sort
			final BeanSearcher<SearchResult> searcher = new BeanSearchImpl();
			final List<BeanMatch<SearchResult>> beanResults = searcher.search(buildResults(content), maxResults, words);
			for (final BeanMatch<SearchResult> beanResult : beanResults) {
				result.add(map(beanResult));
			}
		} catch (final HttpdownloaderServiceException e) {
			logger.error(e.getClass().getName(), e);
		} catch (final IOException e) {
			logger.error(e.getClass().getName(), e);
		} catch (final ParseException e) {
			logger.error(e.getClass().getName(), e);
		} catch (final JSONParseException e) {
			logger.error(e.getClass().getName(), e);
		}

		return result;
	}

	private SearchResult map(final BeanMatch<SearchResult> beanResult) {
		final SearchResult bean = beanResult.getBean();
		return new SearchResultImpl(SEARCH_TYPE, beanResult.getMatchCounter(), bean.getTitle(), bean.getUrl(), bean.getDescription());
	}

	protected String downloadContent(final URL url) throws IOException, HttpdownloaderServiceException {
		final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(url).addTimeout(TIMEOUT).build());
		return httpUtil.getContent(httpResponse);
	}

	protected List<SearchResult> buildResults(final String content) throws JSONParseException, ParseException {
		final List<SearchResult> searchResults = new ArrayList<SearchResult>();
		final Object object = jsonParser.parse(content);
		if (object instanceof JSONObject) {
			final JSONObject root = (JSONObject) object;
			final JSONObject responseData = (JSONObject) root.get("responseData");
			if (responseData != null) {
				final JSONArray results = (JSONArray) responseData.get("results");
				for (int i = 0; i < results.size(); ++i) {
					final JSONObject result = (JSONObject) results.get(i);
					final String url = (String) result.get("url");
					final String title = htmlUtil.filterHtmlTages((String) result.get("title"));
					final String description = htmlUtil.filterHtmlTages((String) result.get("content"));
					final int matchCounter = 1;
					searchResults.add(new SearchResultImpl(SEARCH_TYPE, matchCounter, title, url, description));
				}
			}
		}
		return searchResults;
	}

	protected URL buildQueryUrl(final List<String> words) throws MalformedURLException, UnsupportedEncodingException {
		final String url = PREFIX + urlUtil.encode(StringUtils.join(words, ' '));
		return new URL(url);
	}

	@Override
	public String getName() {
		return SEARCH_TYPE;
	}

	@Override
	public Collection<String> getAliases() {
		return Arrays.asList("google");
	}

	private final class BeanSearchImpl extends BeanSearcher<SearchResult> {

		private static final String URL = "url";

		private static final String DESCRIPTION = "description";

		private static final String TITLE = "title";

		@Override
		protected Map<String, String> getSearchValues(final SearchResult bean) {
			final Map<String, String> values = new HashMap<String, String>();
			values.put(TITLE, bean.getTitle());
			values.put(DESCRIPTION, bean.getDescription());
			values.put(URL, bean.getUrl());
			return values;
		}

		@Override
		protected Map<String, Integer> getSearchPrio() {
			final Map<String, Integer> values = new HashMap<String, Integer>();
			values.put(TITLE, 2);
			values.put(DESCRIPTION, 1);
			values.put(URL, 2);
			return values;
		}
	}
}
