package de.benjaminborbe.websearch.api;

import java.net.URL;
import java.util.Date;

public interface Page {

	PageIdentifier getId();

	Date getLastVisit();

	URL getUrl();

}
