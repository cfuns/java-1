package de.benjaminborbe.tools.url;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UrlUtilImpl implements UrlUtil {

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
			Collections.sort(keys);
			boolean first = false;
			for (final String key : keys) {
				if (!first) {
					sw.append('?');
					first = true;
				}
				else {
					sw.append('&');
				}
				sw.append(key);
				sw.append('=');
				sw.append(encode(parameter.get(key)));
			}
		}
		return sw.toString();
	}
}
