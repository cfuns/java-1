package de.benjaminborbe.tools.html;

import java.util.NoSuchElementException;

import org.apache.commons.lang.StringEscapeUtils;

import de.benjaminborbe.api.IteratorWithException;
import de.benjaminborbe.tools.util.ParseException;

public class HtmlContentIterator implements IteratorWithException<String, ParseException> {

	private static final char TAG_OPEN = '<';

	private static final char TAG_CLOSE = '>';

	private String nextContent;

	private final HtmlTokenIterator tokens;

	private final HtmlTagParser htmlTagParser;

	public HtmlContentIterator(final HtmlTagParser htmlTagParser, final String htmlContent) {
		this.htmlTagParser = htmlTagParser;
		tokens = new HtmlTokenIterator(htmlContent);
	}

	@Override
	public boolean hasNext() throws ParseException {
		boolean script = false;
		boolean style = false;
		while (nextContent == null && tokens.hasNext()) {
			final String token = tokens.next();
			if (isCDATA(token)) {
				nextContent = token.substring(9, token.length() - 3);
			}
			else if (isTag(token)) {
				final HtmlTag htmlTag = htmlTagParser.parse(token);
				if (!script && htmlTag.isOpening() && !htmlTag.isClosing() && "script".equals(htmlTag.getName())) {
					final String type = htmlTag.getAttribute("type");
					if (type == null || type.contains("script")) {
						script = true;
					}
				}
				else if (!style && htmlTag.isOpening() && !htmlTag.isClosing() && "style".equals(htmlTag.getName())) {
					style = true;
				}
				else if (script && htmlTag.isClosing() && "script".equals(htmlTag.getName())) {
					script = false;
				}
				else if (style && htmlTag.isClosing() && "style".equals(htmlTag.getName())) {
					style = false;
				}
			}
			else if (!script && !style) {
				nextContent = token;
			}
		}
		return nextContent != null;
	}

	private boolean isCDATA(final String token) {
		return token.startsWith("<![CDATA[") && token.endsWith("]]>");
	}

	private boolean isTag(final String token) {
		return token.charAt(0) == TAG_OPEN && token.charAt(token.length() - 1) == TAG_CLOSE;
	}

	@Override
	public String next() throws ParseException {
		if (hasNext()) {
			final String result = nextContent;
			nextContent = null;
			return StringEscapeUtils.unescapeHtml(result);
		}
		else {
			throw new NoSuchElementException();
		}
	}
}
