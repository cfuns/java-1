package de.benjaminborbe.projectile.service;

import com.google.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.httpdownloader.api.HttpMethod;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIntegerGE;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIntegerLE;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringUrl;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ProjectileMonitoringCheckRemote implements MonitoringCheck {

	public static final String ID = "9bf66004-e932-41da-9913-93a05e6befcc";

	public static final String TITLE = "ProjectileMonitoringCheckRemote";

	private static final String PARAMETER_TOKEN = "token";

	private static final String PARAMETER_TIMEOUT = "timeout";

	private static final String PARAMETER_USERNAME = "username";

	private static final String PARAMETER_URL = "url";

	private static final int MAX_AGE_IN_DAYS = 1;

	private final Logger logger;

	private final ValidationConstraintValidator validationConstraintValidator;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	private final ParseUtil parseUtil;

	private final JSONParser jsonParser;

	private final CalendarUtil calendarUtil;

	private final TimeZoneUtil timeZoneUtil;

	@Inject
	public ProjectileMonitoringCheckRemote(
		final Logger logger,
		final ValidationConstraintValidator validationConstraintValidator,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil,
		final ParseUtil parseUtil,
		final JSONParser jsonParser,
		final CalendarUtil calendarUtil,
		final TimeZoneUtil timeZoneUtil
	) {
		this.logger = logger;
		this.validationConstraintValidator = validationConstraintValidator;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
		this.parseUtil = parseUtil;
		this.jsonParser = jsonParser;
		this.calendarUtil = calendarUtil;
		this.timeZoneUtil = timeZoneUtil;
	}

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(PARAMETER_URL, PARAMETER_TOKEN, PARAMETER_USERNAME, PARAMETER_TIMEOUT);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		try {
			final String username = getUsername(parameter);
			final String token = getToken(parameter);
			final String urlString = getUrl(parameter) + "?token=" + token + "&username=" + username;
			logger.debug("check url " + urlString);
			final URL url = parseUtil.parseURL(urlString);
			final int timeout = parseUtil.parseInt(getTimeout(parameter));
			final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(url).addHttpMethod(HttpMethod.GET).addTimeout(timeout).build());
			if (httpResponse.getReturnCode() != 200) {
				final String message = "request failed, returncode != 200";
				logger.warn(message);
				return new MonitoringCheckResultDto(this, false, message);
			}
			final String content = httpUtil.getContent(httpResponse);
			logger.debug("json " + content);
			final Object object = jsonParser.parse(content);
			if (object instanceof JSONObject) {
				final JSONObject jsonObject = (JSONObject) object;
				final Object weekObject = jsonObject.get("week");
				if (weekObject instanceof JSONObject) {
					final JSONObject weekJsonObject = (JSONObject) weekObject;
					final Object updateDateObject = weekJsonObject.get("updateDate");
					final String updateDateString = updateDateObject != null ? String.valueOf(updateDateObject) : null;
					final Calendar updateTime = calendarUtil.parseDateTime(timeZoneUtil.getUTCTimeZone(), updateDateString);
					final Calendar maxAge = calendarUtil.subDays(calendarUtil.now(timeZoneUtil.getUTCTimeZone()), MAX_AGE_IN_DAYS);
					logger.debug("updateTime: " + calendarUtil.toDateTimeString(updateTime) + " maxAge: " + calendarUtil.toDateTimeString(maxAge));
					if (updateTime.before(maxAge)) {
						final String message = "updateTime to old";
						logger.debug(message);
						return new MonitoringCheckResultDto(this, false, message);
					} else {
						logger.debug("check ok");
						return new MonitoringCheckResultDto(this, true);
					}
				}
			}
			final String message = "parse json failed";
			logger.warn(message);
			return new MonitoringCheckResultDto(this, false, message);
		} catch (Exception e) {
			logger.warn("check failed", e);
			return new MonitoringCheckResultDto(this, e);
		}
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		return getTitle() + "-Check on " + getUrl(parameter);
	}

	private String getTimeout(final Map<String, String> parameter) {
		return parameter.get(PARAMETER_TIMEOUT);
	}

	private String getUrl(final Map<String, String> parameter) {
		return parameter.get(PARAMETER_URL);
	}

	private String getToken(final Map<String, String> parameter) {
		return parameter.get(PARAMETER_TOKEN);
	}

	private String getUsername(final Map<String, String> parameter) {
		return parameter.get(PARAMETER_USERNAME);
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		final List<ValidationError> result = new ArrayList<ValidationError>();
		// url
		{
			final String value = getUrl(parameter);
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringUrl());
			result.addAll(validationConstraintValidator.validate(PARAMETER_URL, value, constraints));
		}
		// token
		{
			final String value = getToken(parameter);
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			result.addAll(validationConstraintValidator.validate(PARAMETER_TOKEN, value, constraints));
		}
		// username
		{
			final String value = getUsername(parameter);
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			result.addAll(validationConstraintValidator.validate(PARAMETER_USERNAME, value, constraints));
		}
		// timeout
		{
			try {
				final int timeout = parseUtil.parseInt(getTimeout(parameter));
				final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
				constraints.add(new ValidationConstraintNotNull<Integer>());
				constraints.add(new ValidationConstraintIntegerGE(0));
				constraints.add(new ValidationConstraintIntegerLE(60000));
				result.addAll(validationConstraintValidator.validate("timeout", timeout, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple("timeout invalid"));
			}
		}
		return result;
	}
}
