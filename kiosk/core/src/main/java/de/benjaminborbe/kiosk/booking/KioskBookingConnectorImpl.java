package de.benjaminborbe.kiosk.booking;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpMethod;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.kiosk.KioskConstants;
import de.benjaminborbe.kiosk.config.KioskConfig;
import de.benjaminborbe.tools.map.MapChain;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.Duration;
import de.benjaminborbe.tools.util.DurationUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class KioskBookingConnectorImpl implements KioskBookingConnector {

	private static final int TIMEOUT = 60 * 1000; // 60 sec

	private static final int DELAY = 10 * 1000; // 10 sec

	private static final long DURATION_WARN = 5000;

	private final Logger logger;

	private final UrlUtil urlUtil;

	private final DurationUtil durationUtil;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	private final KioskConfig kioskConfig;

	@Inject
	public KioskBookingConnectorImpl(
		final Logger logger,
		final KioskConfig kioskConfig,
		final UrlUtil urlUtil,
		final DurationUtil durationUtil,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil
	) {
		this.logger = logger;
		this.kioskConfig = kioskConfig;
		this.urlUtil = urlUtil;
		this.durationUtil = durationUtil;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
	}

	@Override
	public boolean book(final long customer, final long ean) {
		final Duration duration = durationUtil.getDuration();
		try {
			if (!kioskConfig.isKioskConnectorEnabled()) {
				logger.info("book - customer: " + customer + " skipped");
				return true;
			} else {
				logger.info("book - customer: " + customer + " started");

				// login
				final String sessionId = getLogin(customer);

				// open cart
				{
					final String htmlContent = getCartContent(sessionId);
					if (!htmlContent.contains("Hallo ")) {
						logger.warn("open cart failed");
						logger.debug("htmlContent: " + htmlContent);
						return false;
					} else {
						logger.debug("open cart success");
					}
				}

				// delete all
				{
					final String htmlContent = addProduct(sessionId, KioskConstants.DELETE_EAN);
					if (!htmlContent.contains("<a href=\"http://kiosk/index.cgi/cart\">Redirect-URL</a>")) {
						logger.warn("clear cart failed");
						logger.debug("htmlContent: " + htmlContent);
						return false;
					} else {
						logger.debug("clear cart success");
					}
				}

				// add mittag essen
				{
					final String htmlContent = addProduct(sessionId, ean);
					if (!htmlContent.contains("Redirect-URL")) {
						logger.warn("add failed");
						logger.debug("htmlContent: " + htmlContent);
						return false;
					} else {
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
					} else {
						logger.debug("product in cart");
					}
				}

				// logout
				{
					final String htmlContent = logout(sessionId, customer);
					if (!htmlContent.contains("<a href=\"http://kiosk/index.cgi/end_shopping\">Redirect-URL</a>")) {
						logger.warn("book cart failed");
						logger.debug("htmlContent: " + htmlContent);
						return false;
					} else {
						logger.debug("book cart success");
					}
				}
				// end shopping
				{
					final String htmlContent = endShopping(sessionId);
					if (!htmlContent.contains("Der Einkauf wurde gespeichert.")) {
						logger.warn("end shopping failed");
						logger.debug("htmlContent: " + htmlContent);
						return false;
					} else {
						logger.debug("end shopping success");
					}
				}
				return true;
			}
		} catch (final MalformedURLException e) {
			logger.info(e.getClass().getName(), e);
			return false;
		} catch (final UnsupportedEncodingException e) {
			logger.info(e.getClass().getName(), e);
			return false;
		} catch (HttpdownloaderServiceException e) {
			logger.info(e.getClass().getName(), e);
			return false;
		} catch (IOException e) {
			logger.info(e.getClass().getName(), e);
			return false;
		} finally {
			if (duration.getTime() > DURATION_WARN)
				logger.debug("duration " + duration.getTime());
		}
	}

	private String logout(final String sessionId, final long customer) throws IOException, HttpdownloaderServiceException {
		return addProduct(sessionId, customer);
	}

	private String addProduct(final String sessionId, final long ean) throws IOException, HttpdownloaderServiceException {
		final String url = "https://kiosk.lf.seibert-media.net/index.cgi/cart";
		return httpUtil.getContent(postUrl(new URL(url), new MapChain<String, String>().add("ean", String.valueOf(ean)).add("form_action", "add"),
			new MapChain<String, String>().add("sessionID", sessionId), TIMEOUT));
	}

	private String getCartContent(final String sessionId) throws IOException, HttpdownloaderServiceException {
		final String url = urlUtil.buildUrl("https://kiosk.lf.seibert-media.net/index.cgi/cart", new MapParameter());
		return httpUtil.getContent(getUrl(new URL(url), TIMEOUT, new MapChain<String, String>().add("sessionID", sessionId)));
	}

	private String endShopping(final String sessionId) throws IOException, HttpdownloaderServiceException {
		final String url = urlUtil.buildUrl("https://kiosk.lf.seibert-media.net/index.cgi/end_shopping", new MapParameter());
		return httpUtil.getContent(getUrl(new URL(url), TIMEOUT, new MapChain<String, String>().add("sessionID", sessionId)));
	}

	private String getLogin(final long customer) throws UnsupportedEncodingException, MalformedURLException, HttpdownloaderServiceException {
		final String url = urlUtil.buildUrl("https://kiosk.lf.seibert-media.net/index.cgi", new MapParameter().add("customer_no", String.valueOf(customer)));
		final HttpResponse result = getUrl(new URL(url), TIMEOUT);
		final HttpHeader headers = result.getHeader();
		final List<String> cookies = headers.getValues("Set-Cookie");
		for (final String cookie : cookies) {
			logger.debug("login success");
			return parseSessionId(cookie);
		}
		logger.debug("login failed");
		return null;
	}

	private HttpResponse postUrl(
		final URL url,
		final MapChain<String, String> data,
		final MapChain<String, String> cookies,
		final int timeout
	) throws HttpdownloaderServiceException {
		try {
			Thread.sleep(DELAY);
		} catch (final InterruptedException e) {
		}
		final HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(url).addTimeout(timeout).addCookies(cookies).addHttpMethod(HttpMethod.POST);
		return httpdownloaderService.fetch(httpRequestBuilder.build());
	}

	private HttpResponse getUrl(final URL url, final int timeout, final MapChain<String, String> cookies) throws HttpdownloaderServiceException {
		try {
			Thread.sleep(DELAY);
		} catch (final InterruptedException e) {
		}
		final HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(url).addTimeout(timeout).addCookies(cookies).addHttpMethod(HttpMethod.GET);
		return httpdownloaderService.fetch(httpRequestBuilder.build());
	}

	private HttpResponse getUrl(final URL url, final int timeout) throws HttpdownloaderServiceException {
		try {
			Thread.sleep(DELAY);
		} catch (final InterruptedException e) {
		}
		final HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder(url).addTimeout(timeout).addHttpMethod(HttpMethod.GET);
		return httpdownloaderService.fetch(httpRequestBuilder.build());
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
