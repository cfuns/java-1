package de.benjaminborbe.timetracker.connector;

import de.benjaminborbe.httpdownloader.api.HttpMethod;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.json.JSONArray;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONParseException;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class TimetrackerConnectorImpl implements TimetrackerConnector {

	// 3 sec
	private static final int TIMEOUT = 3 * 1000;

	private final ParseUtil parseUtil;

	private final Logger logger;

	private final DateUtil dateUtil;

	private final JSONParser jsonParser;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	@Inject
	public TimetrackerConnectorImpl(
		final Logger logger,
		final ParseUtil parseUtil,
		final DateUtil dateUtil,
		final JSONParser jsonParser,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil
	) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.dateUtil = dateUtil;
		this.jsonParser = jsonParser;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
	}

	@Override
	public boolean isCompleted(final String sessionId, final Date date) throws TimetrackerConnectorException {
		try {
			logger.debug("isCompleted");
			final URL url = new URL("https://timetracker.rp.seibert-media.net/timetracker/ajax/getTasksAndBookings");
			final Map<String, String> parameter = new HashMap<>();
			parameter.put("date", dateUtil.germanDateString(date));
			final Map<String, String> cookies = new HashMap<>();
			cookies.put("JSESSIONID", sessionId);
			final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(url).addTimeout(TIMEOUT).addCookies(cookies).addParameters(parameter).addHttpMethod(HttpMethod.POST).build());
			final String content = httpUtil.getContent(httpResponse);

			final Object object = jsonParser.parse(content);
			if (object instanceof JSONObject) {
				final JSONObject root = (JSONObject) object;
				final Object bookingsObject = root.get("bookings");
				if (bookingsObject instanceof JSONArray) {
					final JSONArray bookings = (JSONArray) bookingsObject;
					logger.debug("found " + bookings.size() + " bookings");
					if (bookings.size() > 0) {
						final Object bookingObject = bookings.get(0);
						if (bookingObject instanceof JSONObject) {
							final JSONObject booking = (JSONObject) bookingObject;
							final Object dataObject = booking.get("data");
							if (dataObject instanceof JSONObject) {
								final JSONObject data = (JSONObject) dataObject;
								return "transfered".equals(data.get("state"));
							}
						}
					}
				}
			}
			// System.err.println("content: " + content);
			return false;
		} catch (final HttpdownloaderServiceException | JSONParseException | IOException e) {
			throw new TimetrackerConnectorException(e);
		}
	}

	@Override
	public String getSessionId(final String username, final String password) throws TimetrackerConnectorException {
		try {
			logger.debug("getSessionId");
			final URL url = parseUtil.parseURL("https://timetracker.rp.seibert-media.net/timetracker/authentication/signIn/login");
			final Map<String, String> data = new HashMap<>();
			data.put("username", username);
			data.put("password", password);
			final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(url).addTimeout(TIMEOUT).addHttpMethod(HttpMethod.POST).build());
			final String content = httpUtil.getContent(httpResponse);

			final String startString = "jsessionid=";
			final int pos1 = content.indexOf(startString);
			final int pos2 = content.indexOf("\"", pos1);
			if (pos1 != -1 && pos2 != -1) {
				return content.substring(pos1 + startString.length(), pos2);
			} else {
				return null;
			}
		} catch (final ParseException | IOException | HttpdownloaderServiceException e) {
			logger.debug(e.getClass().getSimpleName(), e);
			throw new TimetrackerConnectorException(e.getClass().getSimpleName(), e);
		}
	}
}
