package de.benjaminborbe.tools.html;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HtmlUtilImpl implements HtmlUtil {

	private final Logger logger;

	@Inject
	public HtmlUtilImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public String escapeHtml(final String content) {
		return StringEscapeUtils.escapeHtml(content);
	}

	@Override
	public String unescapeHtml(final String content) {
		return StringEscapeUtils.unescapeHtml(content);
	}

	@Override
	public String filterHtmlTages(final String content) {
		return unescapeHtml(content.replaceAll("<.*?>", " ").replaceAll("\\s+", " ").trim());
	}

	@Override
	public Set<String> parseLinks(final String htmlContent) {
		final Set<String> result = new HashSet<String>();
		if (htmlContent == null || htmlContent.length() == 0) {
			logger.trace("no htmlcontent to parse");
			return result;
		}
		final Document document = Jsoup.parse(htmlContent);

		for (final Element head : document.getElementsByTag("head")) {
			for (final Element meta : head.getElementsByTag("meta")) {
				// <meta name="robots" content="noindex,nofollow">
				if ("robots".equalsIgnoreCase(meta.attr("name"))) {
					final String content = meta.attr("content");
					final String[] parts = content.split(",");
					for (final String part : parts) {
						if ("nofollow".equalsIgnoreCase(part)) {
							return result;
						}
					}
				}
			}
		}

		final Elements elements = document.getElementsByTag("a");
		logger.trace("found " + elements.size() + " a elements");
		for (final Element element : elements) {
			if (!"nofollow".equalsIgnoreCase(element.attr("rel"))) {
				final String href = element.attr("href");
				logger.trace("add link to result " + href);
				result.add(href);
			}
		}
		return result;
	}
}
