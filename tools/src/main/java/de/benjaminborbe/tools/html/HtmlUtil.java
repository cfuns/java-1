package de.benjaminborbe.tools.html;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class HtmlUtil {

	@Inject
	public HtmlUtil() {
	}

	public String escapeHtml(final String content) {
		return StringEscapeUtils.escapeHtml(content);
	}

	public String unescapeHtml(final String content) {
		return StringEscapeUtils.unescapeHtml(content);
	}

	public String filterHtmlTages(final String content) {
		return unescapeHtml(content.replaceAll("<.*?>", " ").replaceAll("\\s+", " ").trim());
	}
}
