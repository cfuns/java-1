package de.benjaminborbe.monitoring.check.twentyfeet;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.inject.Inject;

import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class TwentyfeetChangeConnector {

	private static final int TIMEOUT = 5000;

	private final int MAX_CONSUMER = 10;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final ParseUtil parseUtil;

	@Inject
	public TwentyfeetChangeConnector(final HttpDownloader httpDownloader, final HttpDownloadUtil httpDownloadUtil, final ParseUtil parseUtil) {
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.parseUtil = parseUtil;
	}

	public int getMessagesConsumedTotal() {
		int total = 0;
		for (int i = 1; i <= MAX_CONSUMER; ++i) {
			final Integer count = getMessagesConsumed(i);
			if (count != null)
				total += count;
		}
		return total;
	}

	protected Integer getMessagesConsumed(final int i) {
		try {
			final String urlString = buildUrl(i);
			final URL url = new URL(urlString);
			final HttpDownloadResult result = httpDownloader.getUrlUnsecure(url, TIMEOUT, "admin", "xxxx");
			final String html = httpDownloadUtil.getContent(result);
			final Pattern pattern = Pattern.compile("MessagesConsumed: (\\d+)");
			final Matcher matcher = pattern.matcher(html);
			if (!matcher.find()) {
				return null;
			}
			final MatchResult matchResult = matcher.toMatchResult();
			return parseUtil.parseInt(matchResult.group(1));
		}
		catch (final MalformedURLException e) {
		}
		catch (final HttpDownloaderException e) {
		}
		catch (final ParseException e) {
		}
		catch (final UnsupportedEncodingException e) {
		}
		return null;
	}

	private String buildUrl(final int consumerNumber) {
		return "https://consumer" + consumerNumber + ".twentyfeet.com/app/admin/messageadmin?action=messages";
	}

}
