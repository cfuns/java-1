package de.benjaminborbe.microblog.connector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.microblog.api.MicroblogConversationIdentifier;
import de.benjaminborbe.microblog.api.MicroblogPostIdentifier;
import de.benjaminborbe.microblog.config.MicroblogConfig;
import de.benjaminborbe.microblog.conversation.MicroblogConversationResult;
import de.benjaminborbe.microblog.post.MicroblogPostResult;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class MicroblogConnectorImpl implements MicroblogConnector {

	// 5 sec
	private static final int TIMEOUT = 5 * 1000;

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final ParseUtil parseUtil;

	private final HtmlUtil htmlUtil;

	private final MicroblogConfig microblogConfig;

	@Inject
	public MicroblogConnectorImpl(
			final Logger logger,
			final MicroblogConfig microblogConfig,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final ParseUtil parseUtil,
			final HtmlUtil htmlUtil) {
		this.logger = logger;
		this.microblogConfig = microblogConfig;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.parseUtil = parseUtil;
		this.htmlUtil = htmlUtil;
	}

	@Override
	public MicroblogPostIdentifier getLatestRevision() throws MicroblogConnectorException {
		logger.trace("getLatestRevision");
		try {
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(new URL(microblogConfig.getMicroblogRssFeed()), TIMEOUT);
			final String content = httpDownloadUtil.getContent(result);
			final Pattern pattern = Pattern.compile("<guid>" + microblogConfig.getMicroblogUrl() + "/notice/(\\d+)</guid>");
			final Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				final MatchResult matchResult = matcher.toMatchResult();
				final String number = matchResult.group(1);
				return new MicroblogPostIdentifier(parseUtil.parseLong(number));
			}
			else {
				throw new MicroblogConnectorException("can't find latest revision");
			}
		}
		catch (final MalformedURLException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
		catch (final IOException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
		catch (final ParseException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
		catch (final HttpDownloaderException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
	}

	@Override
	public MicroblogPostResult getPost(final MicroblogPostIdentifier microblogPostIdentifier) throws MicroblogConnectorException {
		logger.trace("getPost");
		try {
			final String postUrl = microblogConfig.getMicroblogUrl() + "/notice/" + microblogPostIdentifier;
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(new URL(postUrl), TIMEOUT);
			final String pageContent = httpDownloadUtil.getContent(result);
			final String content = extractContent(pageContent);
			if (logger.isTraceEnabled())
				logger.trace("content=" + content);
			final String author = extractAuhor(pageContent);
			if (logger.isTraceEnabled())
				logger.trace("author=" + author);
			final String conversationUrl = extractConversationUrl(pageContent);
			if (logger.isTraceEnabled())
				logger.trace("conversationUrl=" + conversationUrl);
			return new MicroblogPostResult(microblogPostIdentifier, content, author, postUrl, conversationUrl, null);
		}
		catch (final MalformedURLException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
		catch (final IOException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
		catch (final HttpDownloaderException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
		catch (final ParseException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
	}

	protected String extractConversationUrl(final String pageContent) {
		final String content = extract(pageContent, "<span class=\"source\">", "</div>");
		return extract(content, " href=\"", "\"");
	}

	protected String extractAuhor(final String pageContent) {
		{
			final String content = extractAuhorMessage(pageContent);
			if (content != null && content.length() > 0) {
				return content;
			}
		}
		{
			final String content = extractAuhorJoinGroup(pageContent);
			if (content != null && content.length() > 0) {
				return content;
			}
		}
		return null;
	}

	protected String extractAuhorJoinGroup(final String pageContent) {
		final String content = extract(pageContent, "<div class=\"join-activity\">", "</div>");
		return extract(content, "<a href=\"" + microblogConfig.getMicroblogUrl() + "/", "\"");
	}

	protected String extractAuhorMessage(final String pageContent) {
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
		}
		else {
			return content;
		}
	}

	protected String extractContentMessage(final String pageContent) throws ParseException {
		final String startPattern = "<p class=\"entry-content\">";
		final String endPattern = "</p>";
		final String content = extract(pageContent, startPattern, endPattern);
		if (content != null && content.length() > 0) {
			return filterContent(content);
		}
		else {
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
		logger.trace("getConversation");

		final String conversationRssUrl = microblogConfig.getMicroblogUrl() + "/api/statusnet/conversation/" + microblogConversationIdentifier.getId() + ".rss";
		try {
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(new URL(conversationRssUrl), TIMEOUT);
			final String pageContent = httpDownloadUtil.getContent(result);
			final int open = pageContent.indexOf("<link>");
			final int close = pageContent.indexOf("</link>", open);
			if (open == -1 || close == -1) {
				throw new MicroblogConnectorException("parse content failed");
			}
			final String conversationUrl = pageContent.substring(open + 6, close);
			return buildMicroblogConversationResult(conversationUrl, pageContent);
		}
		catch (final MalformedURLException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
		catch (final HttpDownloaderException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
		catch (final UnsupportedEncodingException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
		}
		catch (final ParseException e) {
			throw new MicroblogConnectorException(e.getClass().getSimpleName(), e);
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
		return new MicroblogPostResult(microblogPostIdentifier, filterContent(content), author, postUrl, conversationUrl, null);
	}
}
