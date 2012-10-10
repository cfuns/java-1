package de.benjaminborbe.monitoring.check.twentyfeet;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.Check;
import de.benjaminborbe.monitoring.api.CheckResult;
import de.benjaminborbe.monitoring.check.CheckResultException;
import de.benjaminborbe.monitoring.check.CheckResultImpl;
import de.benjaminborbe.monitoring.config.MonitoringConfig;
import de.benjaminborbe.tools.http.HttpDownloadResult;
import de.benjaminborbe.tools.http.HttpDownloadUtil;
import de.benjaminborbe.tools.http.HttpDownloader;
import de.benjaminborbe.tools.http.HttpDownloaderException;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class TwentyfeetQueueCheck implements Check {

	private final class TwentyfeetApidataCheckResult {

		private final Map<String, TwentyfeetApidataCheckResultEntry> data = new HashMap<String, TwentyfeetApidataCheckResultEntry>();

		public TwentyfeetApidataCheckResult(final String htmlContent) throws ParseException {
			final int start = htmlContent.indexOf("<pre>");
			final int end = htmlContent.indexOf("</pre>");
			final String content = htmlContent.substring(start + 5, end);
			final String[] rows = content.trim().split("\n");
			for (int i = 1; i < rows.length; ++i) {
				final String[] fields = rows[i].trim().split(";");
				data.put(fields[0], new TwentyfeetApidataCheckResultEntry(fields[1], fields[2]));
			}
		}

		public TwentyfeetApidataCheckResultEntry getResult(final String serviceType) {
			return data.get(serviceType);
		}

		public Collection<String> getServiceTypes() {
			return data.keySet();
		}
	}

	private final class TwentyfeetApidataCheckResultEntry {

		private final int missing;

		private final int total;

		public TwentyfeetApidataCheckResultEntry(final String missing, final String total) throws ParseException {
			this.missing = parseUtil.parseInt(missing);
			this.total = parseUtil.parseInt(total);
		}

		public int getMissing() {
			return missing;
		}

		public int getTotal() {
			return total;
		}
	}

	private final Logger logger;

	private final static String CHECK_URL = "https://www.twentyfeet.com/app/admin/apidata_check";

	private static final int TIMEOUT = 20000;

	private static final long SLEEP = 10000;

	// more 50% missing
	private static final double WARNING = 0.3d;

	private final HttpDownloader httpDownloader;

	private final HttpDownloadUtil httpDownloadUtil;

	private final ParseUtil parseUtil;

	private final MonitoringConfig monitoringConfig;

	@Inject
	public TwentyfeetQueueCheck(
			final Logger logger,
			final HttpDownloader httpDownloader,
			final HttpDownloadUtil httpDownloadUtil,
			final ParseUtil parseUtil,
			final MonitoringConfig monitoringConfig) {
		this.logger = logger;
		this.httpDownloader = httpDownloader;
		this.httpDownloadUtil = httpDownloadUtil;
		this.parseUtil = parseUtil;
		this.monitoringConfig = monitoringConfig;
	}

	@Override
	public String getName() {
		return "Twentyfeet Queue Check";
	}

	@Override
	public String getDescription() {
		return "Twentyfeet Queue Check";
	}

	@Override
	public CheckResult check() {
		logger.trace("check");
		final URL url = buildURL(CHECK_URL);
		try {
			final HttpDownloadResult firstResult = downloadResult(url);
			Thread.sleep(SLEEP);
			final HttpDownloadResult secondResult = downloadResult(url);

			return buildResult(firstResult, secondResult, url);
		}
		catch (final Exception e) {
			logger.info(e.getClass().getSimpleName() + " for check url " + url);
			return new CheckResultException(this, e, url);
		}
	}

	protected CheckResult buildResult(final HttpDownloadResult firstResult, final HttpDownloadResult secondResult, final URL url) throws UnsupportedEncodingException, ParseException {
		final String firstContent = httpDownloadUtil.getContent(firstResult);
		final String secondContent = httpDownloadUtil.getContent(secondResult);
		if (firstContent == null || secondContent == null) {
			return new CheckResultImpl(this, false, "fetch content failed", url);
		}
		return buildResult(firstContent, secondContent, url);
	}

	protected CheckResult buildResult(final String firstContent, final String secondContent, final URL url) throws ParseException {
		final DecimalFormat df = new DecimalFormat("#####0.0");
		try {
			final TwentyfeetApidataCheckResult first = new TwentyfeetApidataCheckResult(firstContent);
			final TwentyfeetApidataCheckResult second = new TwentyfeetApidataCheckResult(secondContent);

			final Set<String> serviceTypes = new HashSet<String>();
			serviceTypes.addAll(first.getServiceTypes());
			serviceTypes.addAll(second.getServiceTypes());
			final List<String> listOfFailedServiceTypes = new ArrayList<String>();
			for (final String serviceType : serviceTypes) {
				final TwentyfeetApidataCheckResultEntry firstEntry = first.getResult(serviceType);
				final TwentyfeetApidataCheckResultEntry secondEntry = second.getResult(serviceType);
				if (firstEntry != null && secondEntry != null) {
					// skip if missing is not greater 0
					if (firstEntry.getMissing() != 0 && secondEntry.getMissing() != 0) {
						// add serviceType if no change happend
						final double rate = 1d * firstEntry.getMissing() / firstEntry.getTotal();
						logger.trace(serviceType + " missing: " + firstEntry.getMissing() + " total: " + firstEntry.getTotal() + " => " + df.format(rate * 100) + "%");
						if (firstEntry.getMissing() == secondEntry.getMissing()) {
							// skip if not more than WARNING% missing
							if (rate > WARNING) {
								// only facebook + twitter
								if ("facebook".equalsIgnoreCase(serviceType) || "twitter".equalsIgnoreCase(serviceType)) {
									listOfFailedServiceTypes.add(serviceType);
								}
							}
						}
						else {
							logger.trace(serviceType + " " + Math.abs(firstEntry.getMissing() - secondEntry.getMissing()) + " changed");
						}
					}
				}
			}
			if (listOfFailedServiceTypes.isEmpty()) {
				return new CheckResultImpl(this, true, "Twentyfeet queue ok", url);
			}
			else {
				Collections.sort(listOfFailedServiceTypes);
				final String msg = "no change in queue for serviceTypes " + StringUtils.join(listOfFailedServiceTypes, ",");
				logger.trace(msg);
				return new CheckResultImpl(this, false, msg, url);
			}
		}
		catch (final ParseException e) {
			logger.warn("fail to parse content! " + firstContent + " " + secondContent);
			throw e;
		}
	}

	protected HttpDownloadResult downloadResult(final URL url) throws NamingException, HttpDownloaderException {
		final HttpDownloadResult result;
		final String username = monitoringConfig.getTwentyfeetAdminUsername();
		final String password = monitoringConfig.getTwentyfeetAdminPassword();

		if (username != null && password != null) {
			result = httpDownloader.getUrlUnsecure(url, TIMEOUT, username, password);
		}
		else {
			result = httpDownloader.getUrlUnsecure(url, TIMEOUT);
		}
		return result;
	}

	protected URL buildURL(final String url) {
		try {
			return new URL(url);
		}
		catch (final MalformedURLException e) {
			return null;
		}
	}

}
