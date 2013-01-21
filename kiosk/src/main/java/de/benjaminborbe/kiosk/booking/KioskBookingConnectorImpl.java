package de.benjaminborbe.kiosk.booking;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.kiosk.KioskConstants;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;

public class KioskBookingConnectorImpl implements KioskBookingConnector {

	private static final int TIMEOUT = 60 * 1000; // 60 sec

	private static final int DELAY = 10 * 1000; // 10 sec

	private static final long DURATION_WARN = 5000;

	private final Logger logger;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final UrlUtil urlUtil;

	private final DurationUtil durationUtil;

	@Inject
	public KioskBookingConnectorImpl(
			final Logger logger,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final UrlUtil urlUtil,
			final DurationUtil durationUtil) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.urlUtil = urlUtil;
		this.durationUtil = durationUtil;
	}

	@Override
	public boolean book(final long customer, final long ean) {
		final Duration duration = durationUtil.getDuration();
		try {
			logger.info("book - customer: " + customer);
			// login
			final String sessionId = getLogin(customer);

			// open cart
			{
				final String htmlContent = getCartContent(sessionId);
				if (htmlContent.indexOf("Hallo ") == -1) {
					logger.warn("open cart failed");
					logger.debug("htmlContent: " + htmlContent);
					return false;
				}
				else {
					logger.debug("open cart success");
				}
			}

			// delete all
			{
				final String htmlContent = addProduct(sessionId, KioskConstants.DELETE_EAN);
				if (htmlContent.indexOf("<a href=\"http://kiosk/index.cgi/cart\">Redirect-URL</a>") == -1) {
					logger.warn("clear cart failed");
					logger.debug("htmlContent: " + htmlContent);
					return false;
				}
				else {
					logger.debug("clear cart success");
				}
			}

			// add mittag essen
			{
				final String htmlContent = addProduct(sessionId, ean);
				if (htmlContent.indexOf("Redirect-URL") == -1) {
					logger.warn("add failed");
					logger.debug("htmlContent: " + htmlContent);
					return false;
				}
				else {
					logger.debug("add success");
				}
			}
			// check cart content
			{
				final String htmlContent = getCartContent(sessionId);
				if (count(htmlContent, "list_row_uneven") != 1) {
					logger.warn("product not in cart");
					logger.debug("htmlContent: " + htmlContent);
					return false;
				}
				else {
					logger.debug("product in cart");
				}
			}

			// logout
			{
				final String htmlContent = logout(sessionId, customer);
				if (htmlContent.indexOf("<a href=\"http://kiosk/index.cgi/end_shopping\">Redirect-URL</a>") == -1) {
					logger.warn("book cart failed");
					logger.debug("htmlContent: " + htmlContent);
					return false;
				}
				else {
					logger.debug("book cart success");
				}
			}
			// end shopping
			{
				final String htmlContent = endShopping(sessionId);
				if (htmlContent.indexOf("Der Einkauf wurde gespeichert.") == -1) {
					logger.warn("end shopping failed");
					logger.debug("htmlContent: " + htmlContent);
					return false;
				}
				else {
					logger.debug("end shopping success");
				}
			}
			return true;
		}
		catch (final MalformedURLException e) {
			logger.info(e.getClass().getName(), e);
			return false;
		}
		catch (final HttpDownloaderException e) {
			logger.info(e.getClass().getName(), e);
			return false;
		}
		catch (final UnsupportedEncodingException e) {
			logger.info(e.getClass().getName(), e);
			return false;
		}
		finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private String logout(final String sessionId, final long customer) throws MalformedURLException, HttpDownloaderException, UnsupportedEncodingException {
		return addProduct(sessionId, customer);
	}

	private String addProduct(final String sessionId, final long ean) throws HttpDownloaderException, MalformedURLException, UnsupportedEncodingException {
		final String url = "https://kiosk.lf.seibert-media.net/index.cgi/cart";
		final HttpDownloadResult result = postUrl(new URL(url), new MapChain<String, String>().add("ean", String.valueOf(ean)).add("form_action", "add"),
				new MapChain<String, String>().add("sessionID", sessionId), TIMEOUT);
		final String htmlContent = httpDownloadUtil.getContent(result);
		return htmlContent;
	}

	private String getCartContent(final String sessionId) throws UnsupportedEncodingException, HttpDownloaderException, MalformedURLException {
		final String url = urlUtil.buildUrl("https://kiosk.lf.seibert-media.net/index.cgi/cart", new MapParameter());
		final HttpDownloadResult result = getUrl(new URL(url), TIMEOUT, new MapChain<String, String>().add("sessionID", sessionId));
		final String htmlContent = httpDownloadUtil.getContent(result);
		return htmlContent;
	}

	private String endShopping(final String sessionId) throws UnsupportedEncodingException, HttpDownloaderException, MalformedURLException {
		final String url = urlUtil.buildUrl("https://kiosk.lf.seibert-media.net/index.cgi/end_shopping", new MapParameter());
		final HttpDownloadResult result = getUrl(new URL(url), TIMEOUT, new MapChain<String, String>().add("sessionID", sessionId));
		final String htmlContent = httpDownloadUtil.getContent(result);
		return htmlContent;
	}

	private String getLogin(final long customer) throws UnsupportedEncodingException, HttpDownloaderException, MalformedURLException {
		final String url = urlUtil.buildUrl("https://kiosk.lf.seibert-media.net/index.cgi", new MapParameter().add("customer_no", String.valueOf(customer)));
		final HttpDownloadResult result = getUrl(new URL(url), TIMEOUT);
		final Map<String, List<String>> headers = result.getHeaders();
		final List<String> cookies = headers.get("Set-Cookie");
		for (final String cookie : cookies) {
			logger.debug("login success");
			return parseSessionId(cookie);
		}
		logger.debug("login failed");
		return null;
	}

	private HttpDownloadResult postUrl(final URL url, final MapChain<String, String> data, final MapChain<String, String> cookies, final int timeout) throws HttpDownloaderException {
		try {
			Thread.sleep(DELAY);
		}
		catch (final InterruptedException e) {
		}
		return httpDownloader.postUrl(url, data, cookies, timeout);
	}

	private HttpDownloadResult getUrl(final URL url, final int timeout, final MapChain<String, String> add) throws HttpDownloaderException {
		try {
			Thread.sleep(DELAY);
		}
		catch (final InterruptedException e) {
		}
		return httpDownloader.getUrl(url, timeout, add);
	}

	private HttpDownloadResult getUrl(final URL url, final int timeout) throws HttpDownloaderException {
		try {
			Thread.sleep(DELAY);
		}
		catch (final InterruptedException e) {
		}
		return httpDownloader.getUrl(url, timeout);
	}

	protected String parseSessionId(final String cookie) {
		// sessionID=6B1E463C6C2238F591BDFF127F8EBC50; path=/
		final String s = "sessionID=";
		final int pos = cookie.indexOf(s);
		final int pos2 = cookie.indexOf(";", pos + s.length());
		if (pos != -1 && pos2 != -1) {
			return cookie.substring(pos + s.length(), pos2);
		}
		return null;
	}

	private int count(final String content, final String search) {
		int counter = 0;
		int pos = -1;
		while ((pos = content.indexOf(search, pos + 1)) != -1) {
			counter++;
		}
		return counter;
	}
}
