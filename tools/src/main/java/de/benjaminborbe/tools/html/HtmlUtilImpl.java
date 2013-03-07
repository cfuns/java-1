package de.benjaminborbe.tools.html;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.util.LineIterator;
import de.benjaminborbe.tools.util.ParseException;

@Singleton
public class HtmlUtilImpl implements HtmlUtil {

	private final Logger logger;

	private final HtmlTagParser htmlTagParser;

	@Inject
	public HtmlUtilImpl(final Logger logger, final HtmlTagParser htmlTagParser) {
		this.logger = logger;
		this.htmlTagParser = htmlTagParser;
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
	public String filterHtmlTages(final String content) throws ParseException {
		if (content == null) {
			return null;
		}
		final HtmlContentIterator i = new HtmlContentIterator(htmlTagParser, content);
		final List<String> parts = new ArrayList<String>();
		while (i.hasNext()) {
			parts.add(i.next());
		}
		return StringUtils.join(parts, ' ');
	}

	@Override
	public Collection<String> parseLinks(final String htmlContent) {
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

	@Override
	public String addLinks(final String plainContent) {
		if (plainContent == null) {
			return null;
		}
		final StringBuffer result = new StringBuffer();
		final LineIterator i = new LineIterator(plainContent);
		boolean first = true;
		while (i.hasNext()) {
			if (first) {
				first = false;
			}
			else {
				result.append("\n");
			}

			final String line = i.next();

			result.append(addLinksParse(line));
		}
		return result.toString();
	}

	private String addLinksParse(final String line) {
		final List<String> ps = Arrays.asList("http://", "https://");
		String result = line;
		for (final String p : ps) {
			result = addLinksParse(result, p);
		}
		return result;
	}

	private String addLinksParse(final String line, final String p) {
		int start = line.indexOf(p);
		int end = 0;
		final StringBuffer result = new StringBuffer();
		while (start != -1) {
			result.append(line.substring(end, start));
			end = addLinksEnd(line, start);
			result.append(addLinksBuild(line.substring(start, end)));
			start = line.indexOf(p, end);
		}
		result.append(line.substring(end));
		return result.toString();
	}

	private String addLinksBuild(final String link) {
		return "<a href=\"" + link + "\">" + link + "</a>";
	}

	private int addLinksEnd(final String line, final int startPos) {
		for (int pos = startPos; pos < line.length(); ++pos) {
			final char c = line.charAt(pos);
			if (c == ' ' || c == '\n') {
				return pos;
			}
		}
		return line.length();
	}

}
