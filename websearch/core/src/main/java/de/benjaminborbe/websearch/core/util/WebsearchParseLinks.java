package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.crawler.api.CrawlerNotifierResult;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.StorageValueList;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.MathUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBeanMapper;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class WebsearchParseLinks {

	private final Logger logger;

	private final HtmlUtil htmlUtil;

	private final HttpUtil httpUtil;

	private final WebsearchPageDao pageDao;

	private final ParseUtil parseUtil;

	private final MathUtil mathUtil;

	@Inject
	public WebsearchParseLinks(
		final Logger logger,
		final HtmlUtil htmlUtil,
		final HttpUtil httpUtil,
		final WebsearchPageDao pageDao,
		final ParseUtil parseUtil,
		final MathUtil mathUtil
	) {
		this.logger = logger;
		this.htmlUtil = htmlUtil;
		this.httpUtil = httpUtil;
		this.pageDao = pageDao;
		this.parseUtil = parseUtil;
		this.mathUtil = mathUtil;
	}

	public void parseLinks(final CrawlerNotifierResult result) throws IOException {
		final Collection<String> links = htmlUtil.parseLinks(httpUtil.getContent(result));
		logger.trace("found " + links.size() + " links");
		for (final String link : links) {
			try {
				if (isValidLink(link)) {
					final URL resultUrl = result.getUrl();
					final URL targetUrl = buildUrl(resultUrl, link.trim());
					final Long depth = getDepth(result);
					final Integer timeout = result.getTimeout();
					logger.debug("found link to: " + targetUrl + " in " + resultUrl + " depth: " + depth + " timeout: " + timeout);
					WebsearchPageBean page = pageDao.findOrCreate(targetUrl);
					page.setDepth(mathUtil.maxLong(depth, page.getDepth()));
					page.setTimeout(mathUtil.maxInteger(timeout, page.getTimeout()));
					pageDao.save(page, new StorageValueList(pageDao.getEncoding()).add(WebsearchPageBeanMapper.DEPTH).add(WebsearchPageBeanMapper.TIMEOUT));
				}
			} catch (MalformedURLException | StorageException | ParseException e) {
				logger.debug(e.getClass().getName(), e);
			}
		}
	}

	private Long getDepth(final CrawlerNotifierResult result) {
		final Long depth = result.getDepth();
		if (depth == null || depth == 0l) {
			return null;
		} else {
			return depth - 1;
		}
	}

	private boolean isValidLink(final String link) {
		final String linkLower = link.toLowerCase().trim();
		return !linkLower.startsWith("javascript:") && !linkLower.startsWith("feed:");
	}

	protected URL buildUrl(final URL baseUrl, final String link) throws MalformedURLException, ParseException {
		logger.trace("buildUrl url: " + baseUrl + " link: " + link);
		final String url;
		if (link.startsWith("http://") || link.startsWith("https://")) {
			url = link;
		} else if (link.startsWith("/")) {
			final StringWriter sw = new StringWriter();
			sw.append(baseUrl.getProtocol());
			sw.append("://");
			sw.append(baseUrl.getHost());
			if (baseUrl.getPort() != 80 && baseUrl.getPort() != 443 && baseUrl.getPort() != -1) {
				sw.append(":");
				sw.append(String.valueOf(baseUrl.getPort()));
			}
			sw.append(link);
			url = sw.toString();
		} else {
			final StringWriter sw = new StringWriter();
			sw.append(baseUrl.getProtocol());
			sw.append("://");
			sw.append(baseUrl.getHost());
			if (baseUrl.getPort() != 80 && baseUrl.getPort() != 443 && baseUrl.getPort() != -1) {
				sw.append(":");
				sw.append(String.valueOf(baseUrl.getPort()));
			}
			sw.append("/");
			final String path = baseUrl.getPath();
			final int pos = path.lastIndexOf("/");
			if (pos != -1) {
				sw.append(path.substring(0, pos));
			}
			sw.append("/");
			sw.append(link);
			url = sw.toString();
		}
		final String result = cleanUpUrl(url);
		logger.trace("result = " + result);
		return parseUtil.parseURL(result);
	}

	protected String cleanUpUrl(final String url) {
		final String u = url.replaceAll("//", "/").replaceFirst(":/", "://");

		final int posDoubleDash = u.indexOf("://");
		final int posHost = u.indexOf("/", posDoubleDash + 3);
		final int posHash = u.indexOf('#', posDoubleDash + 3);

		final String host;
		final String uri;
		if (posHash == -1) {
			if (posHost == -1) {
				host = u;
				uri = "";
			} else {
				host = u.substring(0, posHost);
				uri = u.substring(posHost);
			}
		} else {
			if (posHost == -1) {
				host = u.substring(0, posHash);
				uri = "";
			} else {
				host = u.substring(0, posHost);
				uri = u.substring(posHost, posHash);
			}
		}

		final String[] parts = uri.split("/");
		final List<String> p = new ArrayList<>();
		for (int i = parts.length - 1; i >= 0; i--) {
			final String part = parts[i];
			if (part != null && !part.isEmpty()) {
				if ("..".equals(part)) {
					i--;
				} else {
					p.add(part);
				}
			}
		}

		p.add(host);
		Collections.reverse(p);

		return StringUtils.join(p, "/");
	}
}
