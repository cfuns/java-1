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

	private static final int DELAY = 10;

	private static boolean running = true;

	private final Logger logger;

	private final UrlUtil urlUtil;

	private final ParseUtil parseUtil;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	private final JSONParser jsonParser;

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
		final String baseUrl = "http://bb/bb";
		final ThreadRunner threadRunner = injector.getInstance(ThreadRunner.class);
		threadRunner.run("a", new Runnable() {

			@Override
			public void run() {
				final String playerId = "54c9ba5c-4268-43ec-8fa7-436de2c76102";
				final String token = "919e18b3-2a59-4ab3-a562-4a9822962217";
				final PokerClient client = injector.getInstance(PokerClient.class);
				client.run(baseUrl, token, playerId);
			}
		});

		threadRunner.run("b", new Runnable() {

			@Override
			public void run() {
				final String playerId = "ba447ced-1aa0-48a2-b0de-7d37cf7b7745";
				final String token = "04a841d8-071b-4143-8318-4753aba06b5c";
				final PokerClient client = injector.getInstance(PokerClient.class);
				client.run(baseUrl, token, playerId);
			}
		});

		threadRunner.run("c", new Runnable() {

			@Override
			public void run() {
				final String playerId = "707f605c-78eb-4277-ae8e-4ac3d7ef140e";
				final String token = "524873a8-2c42-492a-831e-76a23a59d597";
				final PokerClient client = injector.getInstance(PokerClient.class);
				client.run(baseUrl, token, playerId);
			}
		});

		try {
			// 5min
			Thread.sleep(10 * 5 * 60 * 1000);
		} catch (final InterruptedException e) {
		}

		running = false;
	}

	private void run(final String baseUrl, final String token, final String playerId) {
		logger.info("run for player: " + playerId);

		while (running) {
			try {
				final String statusContent = getContent(getStatusUrl(baseUrl, token, playerId));
				logger.debug("status: " + statusContent);
				final Object statusObject = jsonParser.parse(statusContent);
				if (statusObject instanceof JSONObject) {
					final JSONObject statusJsonObject = (JSONObject) statusObject;
					if ("true".equals(String.valueOf(statusJsonObject.get("gameRunning")))) {
						logger.info("game is running");
						if (playerId.equals(String.valueOf(statusJsonObject.get("gameActivePlayer")))) {
							logger.info("current player is active");
							final String callContent = getContent(getCallUrl(baseUrl, token, playerId));
							final Object callObject = jsonParser.parse(callContent);
							if (statusObject instanceof JSONObject) {
								final JSONObject callJsonObject = (JSONObject) callObject;
								if ("true".equals(String.valueOf(callJsonObject.get("success")))) {
									logger.info("call success");
								} else {
									logger.info("call failed: " + callJsonObject.get("error"));
								}
							}
						} else {
							logger.info("current player is not active");
						}
					} else {
						logger.info("game is not running");
					}
				}
				try {
					Thread.sleep(DELAY);
				} catch (final InterruptedException e) {
				}
			} catch (final JSONParseException e) {
				logger.warn(e.getClass().getSimpleName(), e);
			} catch (final ParseException e) {
				logger.warn(e.getClass().getSimpleName(), e);
			} catch (final HttpdownloaderServiceException e) {
				logger.warn(e.getClass().getSimpleName(), e);
			} catch (final UnsupportedEncodingException e) {
				logger.warn(e.getClass().getSimpleName(), e);
			} catch (final IOException e) {
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
		return urlUtil.buildUrl(baseUrl + "/poker/call/json", parameter);
	}

	@SuppressWarnings("unused")
	private String getFoldUrl(final String baseUrl, final String token, final String playerId) throws UnsupportedEncodingException {
		final MapParameter parameter = new MapParameter();
		parameter.add("token", token);
		parameter.add("player_id", playerId);
		return urlUtil.buildUrl(baseUrl + "/poker/fold/json", parameter);
	}

	@SuppressWarnings("unused")
	private String getRaiseUrl(final String baseUrl, final String token, final String playerId, final long amount) throws UnsupportedEncodingException {
		final MapParameter parameter = new MapParameter();
		parameter.add("token", token);
		parameter.add("player_id", playerId);
		parameter.add("amount", String.valueOf(amount));
		return urlUtil.buildUrl(baseUrl + "/poker/raise/json", parameter);
	}

	private String getStatusUrl(final String baseUrl, final String token, final String playerId) throws UnsupportedEncodingException {
		final MapParameter parameter = new MapParameter();
		parameter.add("token", token);
		parameter.add("player_id", playerId);
		return urlUtil.buildUrl(baseUrl + "/poker/status/json", parameter);
	}

}
