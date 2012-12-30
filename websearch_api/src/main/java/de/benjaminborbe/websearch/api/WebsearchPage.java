package de.benjaminborbe.websearch.api;

import java.util.Calendar;

public interface WebsearchPage {

	WebsearchPageIdentifier getId();

	Calendar getLastVisit();

	String getUrl();

}
