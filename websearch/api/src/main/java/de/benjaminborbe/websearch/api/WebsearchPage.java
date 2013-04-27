package de.benjaminborbe.websearch.api;

import de.benjaminborbe.httpdownloader.api.HttpResponse;

import java.util.Calendar;

public interface WebsearchPage extends HttpResponse {

	WebsearchPageIdentifier getId();

	Calendar getLastVisit();
}
