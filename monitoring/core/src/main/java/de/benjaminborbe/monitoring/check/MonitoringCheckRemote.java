package de.benjaminborbe.monitoring.check;

import javax.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MapperJsonObjectMonitoringNode;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONParseException;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.url.UrlUtil;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerGE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerLE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringUrl;
import org.slf4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MonitoringCheckRemote implements MonitoringCheck {

	public static final String ID = "REMOTE";

	private static final String URL = "url";

	private static final String TIMEOUT = "timeout";

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	private final UrlUtil urlUtil;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final JSONParser jsonParser;

	@Inject
	public MonitoringCheckRemote(
		final Logger logger,
		final ParseUtil parseUtil,
		final HttpDownloader httpDownloader,
		final HttpDownloadUtil httpDownloadUtil,
		final JSONParser jsonParser,
		final ValidationConstraintValidator validationConstraintValidator,
		final UrlUtil urlUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.jsonParser = jsonParser;
		this.validationConstraintValidator = validationConstraintValidator;
		this.urlUtil = urlUtil;
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(URL, TIMEOUT);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		logger.debug("check");
		final java.net.URL url;
		try {
			url = parseUtil.parseURL(parameter.get(URL));
		} catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, false, "illegal paremter " + URL);
		}
		final int timeout;
		try {
			timeout = parseUtil.parseInt(parameter.get(TIMEOUT));
		} catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, false, "illegal paremter " + TIMEOUT);
		}
		return check(url, timeout);
	}

	private MonitoringCheckResult check(final java.net.URL url, final int timeout) {
		try {
			logger.debug("check - url: " + url);
			final HttpDownloadResult httpResult = httpDownloader.getUrl(url, timeout);
			final String httpContent = httpDownloadUtil.getContent(httpResult);
			final Object object = jsonParser.parse(httpContent);
			if (object instanceof JSONObject) {
				logger.debug("json-data found");
				final JSONObject jsonObject = (JSONObject) object;
				final String message = asString(jsonObject.get(MapperJsonObjectMonitoringNode.MESSAGE));
				final boolean result = parseUtil.parseBoolean(asString(jsonObject.get(MapperJsonObjectMonitoringNode.RESULT)), false);
				if (result) {
					return new MonitoringCheckResultDto(this, result, message, url);
				} else {
					return new MonitoringCheckResultDto(this, result, message != null ? message : httpContent, url);
				}
			} else {
				logger.debug("no json-data found");
				return new MonitoringCheckResultDto(this, false, "invalid json response", url);
			}
		} catch (final HttpDownloaderException e) {
			logger.warn(e.getClass().getName(), e);
			return new MonitoringCheckResultDto(this, e, url);
		} catch (final UnsupportedEncodingException e) {
			logger.warn(e.getClass().getName(), e);
			return new MonitoringCheckResultDto(this, e, url);
		} catch (final JSONParseException e) {
			logger.warn(e.getClass().getName(), e);
			return new MonitoringCheckResultDto(this, e, url);
		}
	}

	private String asString(final Object object) {
		return object != null ? String.valueOf(object) : null;
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		final String url = parameter.get(URL);
		return "RemoteCheck-Check on " + url;
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		final List<ValidationError> result = new ArrayList<>();

		// url
		{
			final String url = parameter.get(URL);
			final List<ValidationConstraint<String>> constraints = new ArrayList<>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringUrl(urlUtil));
			result.addAll(validationConstraintValidator.validate(URL, url, constraints));
		}

		// timeout
		{
			try {
				final int timeout = parseUtil.parseInt(parameter.get(TIMEOUT));
				final List<ValidationConstraint<Integer>> constraints = new ArrayList<>();
				constraints.add(new ValidationConstraintNotNull<Integer>());
				constraints.add(new ValidationConstraintIntegerGE(0));
				constraints.add(new ValidationConstraintIntegerLE(60000));
				result.addAll(validationConstraintValidator.validate("timeout", timeout, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple("timeout invalid"));
			}
		}

		// valid on create
		{
			final MonitoringCheckResult checkResult = check(parameter);
			if (!Boolean.TRUE.equals(checkResult.getSuccessful())) {
				final StringBuilder sb = new StringBuilder();
				sb.append("check failed:");
				if (checkResult.getMessage() != null) {
					sb.append(" ");
					sb.append(checkResult.getMessage());
				}
				if (checkResult.getException() != null) {
					sb.append(" ");
					sb.append(checkResult.getException());
				}
				result.add(new ValidationErrorSimple(sb.toString()));
			}
		}

		return result;
	}

	@Override
	public String getTitle() {
		return "Remote";
	}

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}
}
