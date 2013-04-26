package de.benjaminborbe.websearch.api;

import java.net.URL;
import java.util.Calendar;

public interface WebsearchPage {

	WebsearchPageIdentifier getId();

	Calendar getLastVisit();

	URL getUrl();

}
