package de.benjaminborbe.websearch.api;

import java.net.URL;
import java.util.Date;

public interface WebsearchPage {

	WebsearchPageIdentifier getId();

	Date getLastVisit();

	URL getUrl();

}
