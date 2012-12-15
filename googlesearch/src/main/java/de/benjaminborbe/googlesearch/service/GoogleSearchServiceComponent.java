package de.benjaminborbe.googlesearch.service;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.search.api.SearchResult;
import de.benjaminborbe.search.api.SearchResultImpl;
import de.benjaminborbe.search.api.SearchServiceComponent;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.search.BeanMatch;
import de.benjaminborbe.tools.search.BeanSearcher;
import de.benjaminborbe.tools.url.UrlUtil;

@Singleton
public class GoogleSearchServiceComponent implements SearchServiceComponent {

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

	private static final String PREFIX = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";

	private static final String SEARCH_TYPE = "Google";

	private static final int TIMEOUT = 1000;

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HtmlUtil htmlUtil;

	private final HttpDownloadUtil httpDownloadUtil;

	private final UrlUtil urlUtil;

	@Inject
	public GoogleSearchServiceComponent(
			final Logger logger,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final HtmlUtil htmlUtil,
			final UrlUtil urlUtil) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.htmlUtil = htmlUtil;
		this.urlUtil = urlUtil;
	}

	@Override
	public List<SearchResult> search(final SessionIdentifier sessionIdentifier, final String query, final int maxResults, final List<String> words) {
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
		}
		catch (final HttpDownloaderException e) {
			logger.error("HttpDownloaderException", e);
		}
		catch (final MalformedURLException e) {
			logger.error("MalformedURLException", e);
		}
		catch (final UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException", e);
		}
		catch (final ParseException e) {
			logger.error("ParseException", e);
		}

		return result;
	}

	private SearchResult map(final BeanMatch<SearchResult> beanResult) {
		final SearchResult bean = beanResult.getBean();
		return new SearchResultImpl(SEARCH_TYPE, beanResult.getMatchCounter(), bean.getTitle(), bean.getUrl(), bean.getDescription());
	}

	protected String downloadContent(final URL url) throws HttpDownloaderException, UnsupportedEncodingException {
		final HttpDownloadResult httpDownloadResult = httpDownloader.getUrlUnsecure(url, TIMEOUT);
		return httpDownloadUtil.getContent(httpDownloadResult);
	}

	protected List<SearchResult> buildResults(final String content) throws ParseException, MalformedURLException {
		final List<SearchResult> searchResults = new ArrayList<SearchResult>();
		final JSONParser parser = new JSONParser();
		final Object object = parser.parse(content);
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
}
