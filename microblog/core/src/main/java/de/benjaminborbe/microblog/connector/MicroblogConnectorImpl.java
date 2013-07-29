package de.benjaminborbe.microblog.connector;

import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.config.MicroblogConfig;
import de.benjaminborbe.microblog.conversation.MicroblogConversationResult;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class MicroblogConnectorImpl implements MicroblogConnector {

	// 5 sec
	public static final int TIMEOUT = 5 * 1000;

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final HtmlUtil htmlUtil;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	private final MicroblogConfig microblogConfig;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public MicroblogConnectorImpl(
		final Logger logger,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil,
		final MicroblogConfig microblogConfig,
		final ParseUtil parseUtil,
		final HtmlUtil htmlUtil,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil
	) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
		this.microblogConfig = microblogConfig;
		this.parseUtil = parseUtil;
		this.htmlUtil = htmlUtil;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
	}

	@Override
	public MicroblogPostIdentifier getLatestRevision() throws MicroblogConnectorException {
		logger.trace("getLatestRevision");
		try {
			final String url = microblogConfig.getMicroblogRssFeed();
			final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(parseUtil.parseURL(url)).addTimeout(TIMEOUT).addSecure(false).build());
			final String content = httpUtil.getContent(httpResponse);
			final Pattern pattern = Pattern.compile("<guid>" + microblogConfig.getMicroblogUrl() + "/notice/(\\d+)</guid>");
			final Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				final MatchResult matchResult = matcher.toMatchResult();
				final String number = matchResult.group(1);
				return new MicroblogPostIdentifier(parseUtil.parseLong(number));
			} else {
				throw new MicroblogConnectorException("can't find latest revision");
			}
		} catch (final HttpdownloaderServiceException e) {
			throw new MicroblogConnectorException(e);
		} catch (ParseException e) {
			throw new MicroblogConnectorException(e);
		} catch (IOException e) {
			throw new MicroblogConnectorException(e);
		}
	}

	@Override
	public MicroblogPostResult getPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogConnectorException {
		logger.trace("getPost");
		try {
			final String url = microblogConfig.getMicroblogUrl() + "/notice/" + microblogPostIdentifier;
			final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(parseUtil.parseURL(url)).addTimeout(TIMEOUT).addSecure(false).build());
			final String pageContent = httpUtil.getContent(httpResponse);
			final String content = extractContent(pageContent);
			if (logger.isTraceEnabled())
				logger.trace("content=" + content);
			final String author = extractAuthor(pageContent);
			if (logger.isTraceEnabled())
				logger.trace("author=" + author);
			final String conversationUrl = extractConversationUrl(pageContent);
			if (logger.isTraceEnabled())
				logger.trace("conversationUrl=" + conversationUrl);
			final Calendar date = extractCalendar(pageContent);
			return new MicroblogPostResult(microblogPostIdentifier, content, author, url, conversationUrl, date);
		} catch (final ParseException e) {
			throw new MicroblogConnectorException(e);
		} catch (HttpdownloaderServiceException e) {
			throw new MicroblogConnectorException(e);
		} catch (IOException e) {
			throw new MicroblogConnectorException(e);
		}
	}

	protected String extractConversationUrl(final String pageContent) {
		final String content = extract(pageContent, "<span class=\"source\">", "</div>");
		return extract(content, " href=\"", "\"");
	}

	// <abbr class="published" title="2013-02-22T08:22:33+01:00">vor ca. 3 Stunden</abbr>
	protected Calendar extractCalendar(final String pageContent) throws ParseException {
		final String startPattern = "<abbr class=\"published\"";
		final String endPattern = "</abbr>";
		final String abbrContent = extract(pageContent, startPattern, endPattern);
		final Calendar result;
		if (abbrContent != null && abbrContent.length() > 0) {
			final String startPatternTitle = "title=\"";
			final String endPatternTitle = "\"";
			final String contentTitle = extract(abbrContent, startPatternTitle, endPatternTitle);
			result = calendarUtil.parseDateTime(timeZoneUtil.getEuropeBerlinTimeZone(), contentTitle);
		} else {
			result = null;
		}
		logger.trace("extractCalendar => " + calendarUtil.toDateTimeString(result));
		return result;
	}

	protected String extractAuthor(final String pageContent) {
		{
			final String content = extractAuthorMessage(pageContent);
			if (content != null && content.length() > 0) {
				return content;
			}
		}
		{
			final String content = extractAuthorJoinGroup(pageContent);
			if (content != null && content.length() > 0) {
				return content;
			}
		}
		return null;
	}

	protected String extractAuthorJoinGroup(final String pageContent) {
		final String content = extract(pageContent, "<div class=\"join-activity\">", "</div>");
		return extract(content, "<a href=\"" + microblogConfig.getMicroblogUrl() + "/", "\"");
	}

	protected String extractAuthorMessage(final String pageContent) {
		final String content = extract(pageContent, "<span class=\"vcard author\">", "</span>");
		return extract(content, "<a href=\"" + microblogConfig.getMicroblogUrl() + "/", "\"");
	}

	protected String extractContent(final String pageContent) throws ParseException {
		{
			final String content = extractContentMessage(pageContent);
			if (content != null && content.length() > 0) {
				return content;
			}
		}
		{
			final String content = extractContentJoinGroup(pageContent);
			if (content != null && content.length() > 0) {
				return content;
			}
		}
		logger.debug("extractContent failed! return null");
		return null;
	}

	protected String extractContentJoinGroup(final String pageContent) throws ParseException {
		final String startPattern = "<div class=\"join-activity\">";
		final String endPattern = "</div>";
		final String content = extract(pageContent, startPattern, endPattern);
		if (content != null && content.length() > 0) {
			return filterContent(content);
		} else {
			return content;
		}
	}

	protected String extractContentMessage(final String pageContent) throws ParseException {
		final String startPattern = "<p class=\"entry-content\">";
		final String endPattern = "</p>";
		final String content = extract(pageContent, startPattern, endPattern);
		if (content != null && content.length() > 0) {
			return filterContent(content);
		} else {
			return content;
		}
	}

	protected String filterContent(final String content) throws ParseException {
		return htmlUtil.unescapeHtml(htmlUtil.filterHtmlTages(content));
	}

	protected String extract(final String pageContent, final String startPattern, final String endPattern) {
		if (pageContent == null)
			return null;
		final int startPos = pageContent.indexOf(startPattern);
		if (startPos == -1)
			return null;
		final int endPos = pageContent.indexOf(endPattern, startPos + startPattern.length());
		if (endPos == -1)
			return null;
		return pageContent.substring(startPos + startPattern.length(), endPos);
	}

	@Override
	public MicroblogConversationResult getConversation(final MicroblogConversationIdentifier microblogConversationIdentifier) throws MicroblogConnectorException {
		try {
			logger.trace("getConversation");
			final String url = microblogConfig.getMicroblogUrl() + "/api/statusnet/conversation/" + microblogConversationIdentifier.getId() + ".rss";
			final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(parseUtil.parseURL(url)).addTimeout(TIMEOUT).addSecure(false).build());
			final String pageContent = httpUtil.getContent(httpResponse);
			final int open = pageContent.indexOf("<link>");
			final int close = pageContent.indexOf("</link>", open);
			if (open == -1 || close == -1) {
				throw new MicroblogConnectorException("parse content failed");
			}
			final String conversationUrl = pageContent.substring(open + 6, close);
			return buildMicroblogConversationResult(conversationUrl, pageContent);
		} catch (final ParseException e) {
			throw new MicroblogConnectorException(e);
		} catch (HttpdownloaderServiceException e) {
			throw new MicroblogConnectorException(e);
		} catch (IOException e) {
			throw new MicroblogConnectorException(e);
		}
	}

	protected MicroblogConversationResult buildMicroblogConversationResult(final String conversationUrl, final String pageContent) throws ParseException {
		final List<MicroblogPostResult> list = new ArrayList<MicroblogPostResult>();
		int itemIndexOpen = pageContent.indexOf("<item>");
		int itemIndexClose = pageContent.indexOf("</item>", itemIndexOpen);
		while (itemIndexOpen != -1 && itemIndexClose != -1) {
			final String itemContent = pageContent.substring(itemIndexOpen + 6, itemIndexClose);
			list.add(buildPost(conversationUrl, itemContent));

			// search next
			itemIndexOpen = pageContent.indexOf("<item>", itemIndexClose);
			itemIndexClose = pageContent.indexOf("</item>", itemIndexOpen);
		}
		Collections.reverse(list);
		return new MicroblogConversationResult(conversationUrl, list);

	}

	protected MicroblogPostResult buildPost(final String conversationUrl, final String itemContent) throws ParseException {
		if (logger.isTraceEnabled())
			logger.trace("itemContent: " + itemContent);
		final int titleIndexOpen = itemContent.indexOf("<title>");
		final int titleIndexClose = itemContent.indexOf("</title>");

		final int authorIndexClose = itemContent.indexOf(":", titleIndexOpen);
		final String author = itemContent.substring(titleIndexOpen + 7, authorIndexClose).trim();
		final String content = itemContent.substring(authorIndexClose + 1, titleIndexClose).trim();

		final int linkIndexOpen = itemContent.indexOf("<link>");
		final int linkIndexClose = itemContent.indexOf("</link>");
		final String postUrl = itemContent.substring(linkIndexOpen + 6, linkIndexClose);
		final int slashpos = postUrl.lastIndexOf("/");
		final String id = postUrl.substring(slashpos + 1);
		final MicroblogPostIdentifier microblogPostIdentifier = new MicroblogPostIdentifier(parseUtil.parseLong(id));
		final Calendar date = null;
		return new MicroblogPostResult(microblogPostIdentifier, filterContent(content), author, postUrl, conversationUrl, date);
	}
}
