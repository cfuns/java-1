package de.benjaminborbe.tools.url;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UrlUtilImpl implements UrlUtil {

	private final class Sort implements Comparator<String> {

		@Override
		public int compare(final String arg0, final String arg1) {
			if (arg0 != null && arg1 != null) {
				return arg0.compareTo(arg1);
			}
			else if (arg0 != null && arg1 == null) {
				return 1;
			}
			else if (arg0 == null && arg1 != null) {
				return -1;
			}
			return 0;
		}
	}

	@Inject
	public UrlUtilImpl() {
	}

	@Override
	public String encode(final String url) throws UnsupportedEncodingException {
		return URLEncoder.encode(url, "UTF8");
	}

	@Override
	public String decode(final String url) throws UnsupportedEncodingException {
		return URLDecoder.decode(url, "UTF8");
	}

	@Override
	public String buildUrl(final String path, final Map<String, String> parameter) throws UnsupportedEncodingException {
		final StringWriter sw = new StringWriter();
		sw.append(path);
		if (!parameter.isEmpty()) {
			final List<String> keys = new ArrayList<String>(parameter.keySet());
			Collections.sort(keys, new Sort());
			boolean first = false;
			for (final String key : keys) {
				final String value = parameter.get(key);
				if (key != null && value != null) {
					if (!first) {
						sw.append('?');
						first = true;
					}
					else {
						sw.append('&');
					}
					sw.append(key);
					sw.append('=');
					sw.append(encode(value));
				}
			}
		}
		return sw.toString();
	}
}
