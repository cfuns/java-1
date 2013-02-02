package de.benjaminborbe.tools.html;

import java.util.Collection;

import de.benjaminborbe.tools.util.ParseException;

public interface HtmlUtil {

	String escapeHtml(final String content);

	String unescapeHtml(final String content);

	String filterHtmlTages(final String htmlContent) throws ParseException;

	Collection<String> parseLinks(final String htmlContent);

	String addLinks(String plainContent);

}
