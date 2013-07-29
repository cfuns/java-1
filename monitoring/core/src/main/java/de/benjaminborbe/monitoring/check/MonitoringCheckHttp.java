package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderService;
import de.benjaminborbe.httpdownloader.api.HttpdownloaderServiceException;
import de.benjaminborbe.httpdownloader.tools.HttpRequestBuilder;
import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraint;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintAnd;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIntegerGE;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintIntegerLE;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintNull;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintOr;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.lib.validation.constraint.ValidationConstraintStringMinLength;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MonitoringCheckHttp implements MonitoringCheck {

	public static final String ID = "HTTP";

	private static final String URL = "url";

	private static final String TIMEOUT = "timeout";

	private static final String TITLEMATCH = "titlematch";

	private static final String CONTENTMATCH = "contentmatch";

	private static final String USERNAME = "username";

	private static final String PASSWORD = "password";

	private final Logger logger;

	private final ParseUtil parseUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	private final HttpdownloaderService httpdownloaderService;

	private final HttpUtil httpUtil;

	@Inject
	public MonitoringCheckHttp(
		final Logger logger,
		final ParseUtil parseUtil,
		final ValidationConstraintValidator validationConstraintValidator,
		final HttpdownloaderService httpdownloaderService,
		final HttpUtil httpUtil
	) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.validationConstraintValidator = validationConstraintValidator;
		this.httpdownloaderService = httpdownloaderService;
		this.httpUtil = httpUtil;
	}

	@Override
	public Collection<String> getRequireParameters() {
		return Arrays.asList(URL, TIMEOUT, TITLEMATCH, CONTENTMATCH, USERNAME, PASSWORD);
	}

	@Override
	public MonitoringCheckResult check(final Map<String, String> parameter) {
		final String urlString = parameter.get(URL);
		final String titleMatch = parameter.get(TITLEMATCH);
		final String contentMatch = parameter.get(CONTENTMATCH);
		final String username = parameter.get(USERNAME);
		final String password = parameter.get(PASSWORD);
		final int timeout;
		try {
			timeout = parseUtil.parseInt(parameter.get(TIMEOUT));
		} catch (final ParseException e) {
			return new MonitoringCheckResultDto(this, false, "illegal paremter " + TIMEOUT);
		}
		return check(urlString, timeout, titleMatch, contentMatch, username, password);
	}

	private MonitoringCheckResult check(
		final String urlString,
		final int timeout,
		final String titleMatch,
		final String contentMatch,
		final String username,
		final String password
	) {
		URL url = null;
		try {
			url = new URL(urlString);
			final HttpResponse httpResponse = httpdownloaderService.fetch(new HttpRequestBuilder(url).addSecure(false).addTimeout(timeout).build());
			logger.trace("downloaded " + url + " in " + httpResponse.getDuration() + " ms");
			if (httpResponse.getDuration() > timeout) {
				final String msg = "timeout while downloading url: " + url;
				logger.trace(msg);
				return new MonitoringCheckResultDto(this, false, msg, url);
			}
			final String content = httpUtil.getContent(httpResponse);
			if (!checkTitle(content, titleMatch)) {
				final String msg = "cannot find title " + titleMatch + " in content of " + url;
				logger.warn(msg);
				return new MonitoringCheckResultDto(this, false, msg, url);
			}
			if (!checkContent(content, contentMatch)) {
				final String msg = "cannot find string " + contentMatch + " in content of " + url;
				logger.warn(msg);
				return new MonitoringCheckResultDto(this, false, msg, url);
			}
			final String msg = "download url successful " + url;
			return new MonitoringCheckResultDto(this, true, msg, url);
		} catch (final MalformedURLException e) {
			logger.warn(e.getClass().getName(), e);
			return new MonitoringCheckResultDto(this, e, url);
		} catch (final IOException e) {
			logger.warn(e.getClass().getName(), e);
			return new MonitoringCheckResultDto(this, e, url);
		} catch (HttpdownloaderServiceException e) {
			logger.warn(e.getClass().getName(), e);
			return new MonitoringCheckResultDto(this, e, url);
		}
	}

	private boolean checkContent(final String content, final String contentMatch) {
		return contentMatch == null || content != null && content.contains(contentMatch);
	}

	private boolean checkTitle(final String content, final String titleMatch) {
		if (titleMatch == null) {
			logger.debug("titleMatch is null => skip check");
			return true;
		}
		if (content == null) {
			logger.debug("content is null => check failed");
			return false;
		}
		final String start = "<title>";
		final String end = "</title>";
		final int posStart = content.indexOf(start);
		final int posEnd = content.indexOf(end);
		if (posStart == -1 || posEnd == -1) {
			logger.debug("can't find title in content => check failed");
			return false;
		}
		final String title = content.substring(posStart + start.length(), posEnd);
		if (title.contains(titleMatch)) {
			logger.debug("title match => check success");
			return true;
		} else {
			logger.debug("title mismatch " + title + " != " + titleMatch + " => check failed");
			return false;
		}
	}

	@Override
	public String getDescription(final Map<String, String> parameter) {
		final String urlString = parameter.get(URL);
		return "HTTP-Check on " + urlString;
	}

	@Override
	public Collection<ValidationError> validate(final Map<String, String> parameter) {
		final List<ValidationError> result = new ArrayList<ValidationError>();

		// url
		{
			try {
				final URL url = parseUtil.parseURL(parameter.get(URL));
				final List<ValidationConstraint<URL>> constraints = new ArrayList<ValidationConstraint<java.net.URL>>();
				constraints.add(new ValidationConstraintNotNull<URL>());
				result.addAll(validationConstraintValidator.validate("url", url, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple("url invalid"));
			}
		}

		// timeout
		{
			try {
				final int timeout = parseUtil.parseInt(parameter.get(TIMEOUT));
				final List<ValidationConstraint<Integer>> constraints = new ArrayList<ValidationConstraint<Integer>>();
				constraints.add(new ValidationConstraintNotNull<Integer>());
				constraints.add(new ValidationConstraintIntegerGE(0));
				constraints.add(new ValidationConstraintIntegerLE(60000));
				result.addAll(validationConstraintValidator.validate("timeout", timeout, constraints));
			} catch (final ParseException e) {
				result.add(new ValidationErrorSimple("timeout invalid"));
			}
		}

		// titlematch
		{
			final String titlematch = parameter.get(TITLEMATCH);
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();

			// null
			final ValidationConstraint<String> v1 = new ValidationConstraintNull<String>();

			// not null
			final ValidationConstraint<String> v2 = new ValidationConstraintAnd<String>().add(new ValidationConstraintNotNull<String>())
				.add(new ValidationConstraintStringMinLength(1))
				.add(new ValidationConstraintStringMaxLength(255));

			constraints.add(new ValidationConstraintOr<String>().add(v1).add(v2));
			result.addAll(validationConstraintValidator.validate("titlematch", titlematch, constraints));
		}

		// contentmatch
		{
			final String contentmatch = parameter.get(CONTENTMATCH);
			final List<ValidationConstraint<String>> constraints = new ArrayList<ValidationConstraint<String>>();
			constraints.add(new ValidationConstraintNotNull<String>());
			constraints.add(new ValidationConstraintStringMinLength(1));
			constraints.add(new ValidationConstraintStringMaxLength(255));
			result.addAll(validationConstraintValidator.validate("contentmatch", contentmatch, constraints));
		}

		return result;
	}

	@Override
	public String getTitle() {
		return "Http";
	}

	@Override
	public MonitoringCheckIdentifier getId() {
		return new MonitoringCheckIdentifier(ID);
	}
}
