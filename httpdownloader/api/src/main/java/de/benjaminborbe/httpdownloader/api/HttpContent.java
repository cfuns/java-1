package de.benjaminborbe.httpdownloader.api;

import java.io.InputStream;

public interface HttpContent {

	byte[] getContent();

	InputStream getContentStream();
}
