package de.benjaminborbe.index.service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import de.benjaminborbe.tools.util.HttpDownloadResult;

public interface TwentyFeetPerformanceService {

	Map<URL, HttpDownloadResult> getPerformance() throws IOException;
}
