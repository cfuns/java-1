package de.benjaminborbe.monitoring.check;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import de.benjaminborbe.tools.util.HttpDownloader;

public class UrlCheckBuilder {

	private final static List<String> urls = Arrays.asList("http://www.twentyfeet.com/", "http://test.twentyfeet.com/",
			"http://www.benjamin-borbe.de/", "http://www.benjaminborbe.de/", "http://www.harteslicht.com/",
			"http://confluence.rocketnews.de/");

	private UrlCheckBuilder() {

	}

	@Inject
	public static void link(final Logger logger, final CheckRegistry registry, final HttpDownloader httpDownloader) {
		for (final String url : urls) {
			registry.register(buildCheck(logger, httpDownloader, url));
		}
	}

	protected static UrlCheck buildCheck(final Logger logger, final HttpDownloader httpDownloader, final String url) {
		return new UrlCheck(logger, httpDownloader, url);
	}
}
