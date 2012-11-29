package de.benjaminborbe.tools.html;

import java.util.Set;

public interface HtmlUtil {

	String escapeHtml(final String content);

	String unescapeHtml(final String content);

	String filterHtmlTages(final String htmlContent);

	Set<String> parseLinks(final String htmlContent);

	String addLinks(String plainContent);

}
