package de.benjaminborbe.websearch.api;

import de.benjaminborbe.httpdownloader.api.HttpHeader;

import java.net.URL;
import java.util.Calendar;

public interface WebsearchPage {

	WebsearchPageIdentifier getId();

	Calendar getLastVisit();

	URL getUrl();

	byte[] getContent();

	HttpHeader getHeader();

	Integer getReturnCode();
}
