package de.benjaminborbe.poker.client;

import com.google.inject.Injector;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.poker.client.guice.PokerClientModules;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONParseException;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.json.JSONParserSimple;
import de.benjaminborbe.tools.url.MapParameter;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ThreadRunner;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PokerClient {

	private static final int DELAY = 200;

	private static final int TIMEOUT = 5000;

	private final Logger logger;

	private final UrlUtil urlUtil;

	private final ParseUtil parseUtil;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	private final JSONParser jsonParser;

	private static boolean running = true;

	@Inject
	public PokerClient(
		final Logger logger,
		final UrlUtil urlUtil,
		final ParseUtil parseUtil,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil
	) {
		this.logger = logger;
		this.urlUtil = urlUtil;
		this.parseUtil = parseUtil;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
		this.jsonParser = new JSONParserSimple();
	}

	public static void main(final String[] args) {
		final Injector injector = GuiceInjectorBuilder.getInjector(new PokerClientModules());
		final String baseUrl = "http://bb";
		final ThreadRunner threadRunner = injector.getInstance(ThreadRunner.class);

		threadRunner.run("playerC", new Runnable() {

			@Override
			public void run() {
				final String token = "d830c7fd-a0eb-4c1a-8997-a87b310206c6";
				final String playerId = "fd0e8ed0-7bd9-45cc-b72d-18f1ef51d199";
				final PokerClient client = injector.getInstance(PokerClient.class);
				client.run(baseUrl, token, playerId);
			}
		});

		threadRunner.run("bborbe", new Runnable() {

			@Override
			public void run() {
				final String token = "223ba30c-5db0-4e8b-8f73-6e1cd3a29a6a";
				final String playerId = "63f8ed37-2bbd-4463-9566-44277df96e54";
				final PokerClient client = injector.getInstance(PokerClient.class);
				client.run(baseUrl, token, playerId);
			}
		});

		try {
			// 30 sec
			Thread.sleep(30 * 1000);
		} catch (final InterruptedException e) {
		}

		running = false;
	}

	private void run(final String baseUrl, final String token, final String playerId) {
		logger.debug("run");

		while (running) {
			try {
				final String statusContent = getContent(getStatusUrl(baseUrl, token, playerId));
				logger.debug("status: " + statusContent);
				final Object statusObject = jsonParser.parse(statusContent);
				if (statusObject instanceof JSONObject) {
					final JSONObject statusJsonObject = (JSONObject) statusObject;
					if ("true".equals(String.valueOf(statusJsonObject.get("gameRunning")))) {
						logger.debug("game running");
						if (playerId.equals(String.valueOf(statusJsonObject.get("gameActivePlayer")))) {
							logger.debug("active player");
							final String callContent = getContent(getCallUrl(baseUrl, token, playerId));
							final Object callObject = jsonParser.parse(callContent);
							if (statusObject instanceof JSONObject) {
								final JSONObject callJsonObject = (JSONObject) callObject;
								if ("true".equals(String.valueOf(callJsonObject.get("success")))) {
									logger.debug("call success");
								} else {
									logger.debug("call failed: " + callJsonObject.get("error"));
								}
							}
						} else {
							logger.debug("not active player");
						}
					} else {
						logger.debug("game not running");
					}
				}
				try {
					Thread.sleep(DELAY);
				} catch (final InterruptedException e) {
				}
			} catch (JSONParseException | ParseException | HttpdownloaderServiceException | IOException e) {
				logger.warn(e.getClass().getSimpleName(), e);
			}
		}
	}

	public String getContent(final String url) throws IOException, ParseException, HttpdownloaderServiceException {
		final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(parseUtil.parseURL(url)).build());
		return httpUtil.getContent(httpResponse);
	}

	private String getCallUrl(final String baseUrl, final String token, final String playerId) throws UnsupportedEncodingException {
		final MapParameter parameter = new MapParameter();
		parameter.add("token", token);
		parameter.add("player_id", playerId);
		return urlUtil.buildUrl(baseUrl + "/bb/poker/call/json", parameter);
	}

	@SuppressWarnings("unused")
	private String getFoldUrl(final String baseUrl, final String token, final String playerId) throws UnsupportedEncodingException {
		final MapParameter parameter = new MapParameter();
		parameter.add("token", token);
		parameter.add("player_id", playerId);
		return urlUtil.buildUrl(baseUrl + "/bb/poker/fold/json", parameter);
	}

	@SuppressWarnings("unused")
	private String getRaiseUrl(final String baseUrl, final String token, final String playerId, final long amount) throws UnsupportedEncodingException {
		final MapParameter parameter = new MapParameter();
		parameter.add("token", token);
		parameter.add("player_id", playerId);
		parameter.add("amount", String.valueOf(amount));
		return urlUtil.buildUrl(baseUrl + "/bb/poker/raise/json", parameter);
	}

	private String getStatusUrl(final String baseUrl, final String token, final String playerId) throws UnsupportedEncodingException {
		final MapParameter parameter = new MapParameter();
		parameter.add("token", token);
		parameter.add("player_id", playerId);
		return urlUtil.buildUrl(baseUrl + "/bb/poker/status/json", parameter);
	}

}
