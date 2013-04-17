package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;
import de.benjaminborbe.api.ValidationError;
import de.benjaminborbe.api.ValidationErrorSimple;
import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckIdentifier;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;
import de.benjaminborbe.monitoring.tools.MonitoringCheckResultDto;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraint;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintAnd;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerGE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintIntegerLE;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNotNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintNull;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintOr;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMaxLength;
import de.benjaminborbe.tools.validation.constraint.ValidationConstraintStringMinLength;
import org.slf4j.Logger;

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

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final ParseUtil parseUtil;

	private final ValidationConstraintValidator validationConstraintValidator;

	@Inject
	public MonitoringCheckHttp(
		final Logger logger,
		final HttpDownloader httpDownloader,
		final HttpDownloadUtil httpDownloadUtil,
		final ParseUtil parseUtil,
		final ValidationConstraintValidator validationConstraintValidator) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.parseUtil = parseUtil;
		this.validationConstraintValidator = validationConstraintValidator;
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

	private MonitoringCheckResult check(final String urlString, final int timeout, final String titleMatch, final String contentMatch, final String username, final String password) {
		URL url = null;
		try {
			url = new URL(urlString);
			final HttpDownloadResult result;
			if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
				result = httpDownloader.getUrlUnsecure(url, timeout, username, password);
			} else {
				result = httpDownloader.getUrlUnsecure(url, timeout);
			}
			logger.trace("downloaded " + url + " in " + result.getDuration() + " ms");
			if (result.getDuration() > timeout) {
				final String msg = "timeout while downloading url: " + url;
				logger.trace(msg);
				return new MonitoringCheckResultDto(this, false, msg, url);
			}
			final String content = httpDownloadUtil.getContent(result);
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
		} catch (final HttpDownloaderException e) {
			logger.warn(e.getClass().getName(), e);
			return new MonitoringCheckResultDto(this, e, url);
		}
	}

	private boolean checkContent(final String content, final String contentMatch) {
		return contentMatch == null || content.contains(contentMatch);
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
		final List<ValidationError> result = new ArrayList<>();

		// url
		{
			try {
				final URL url = parseUtil.parseURL(parameter.get(URL));
				final List<ValidationConstraint<URL>> constraints = new ArrayList<>();
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
				final List<ValidationConstraint<Integer>> constraints = new ArrayList<>();
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
			final List<ValidationConstraint<String>> constraints = new ArrayList<>();

			// null
			final ValidationConstraint<String> v1 = new ValidationConstraintNull<>();

			// not null
			final ValidationConstraint<String> v2 = new ValidationConstraintAnd<String>().add(new ValidationConstraintNotNull<String>()).add(new ValidationConstraintStringMinLength(1))
				.add(new ValidationConstraintStringMaxLength(255));

			constraints.add(new ValidationConstraintOr<String>().add(v1).add(v2));
			result.addAll(validationConstraintValidator.validate("titlematch", titlematch, constraints));
		}

		// contentmatch
		{
			final String contentmatch = parameter.get(CONTENTMATCH);
			final List<ValidationConstraint<String>> constraints = new ArrayList<>();
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
