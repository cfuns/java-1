package de.benjaminborbe.microblog.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public class MicroblogConnectorImpl implements MicroblogConnector {

	private final static String MICROBLOG_URL = "https://micro.rp.seibert-media.net/api/statuses/friends_timeline/bborbe.rss";

	// 5 sec
	private static final int TIMEOUT = 5 * 1000;

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final ParseUtil parseUtil;

	@Inject
	public MicroblogConnectorImpl(final Logger logger, final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil, final ParseUtil parseUtil) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.parseUtil = parseUtil;
	}

	@Override
	public long getLatestRevision() throws MicroblogConnectorException {
		logger.debug("getLatestRevision");
		try {
			final HttpDownloadResult result = httpDownloader.downloadUrlUnsecure(new URL(MICROBLOG_URL), TIMEOUT);
			final String content = httpDownloadUtil.getContent(result);
			final Pattern pattern = Pattern.compile("<guid>https://micro.rp.seibert-media.net/notice/(\\d+)</guid>");
			final Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
				final MatchResult matchResult = matcher.toMatchResult();
				final String number = matchResult.group(1);
				return parseUtil.parseLong(number);
			}
			else {
				throw new MicroblogConnectorException("can't find latest revision");
			}
		}
		catch (final MalformedURLException e) {
			throw new MicroblogConnectorException("MalformedURLException", e);
		}
		catch (final IOException e) {
			throw new MicroblogConnectorException("IOException", e);
		}
		catch (final ParseException e) {
			throw new MicroblogConnectorException("ParseException", e);
		}
	}
}
