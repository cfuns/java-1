package de.benjaminborbe.confluence.api;

import java.util.Calendar;

public interface ConfluencePage {

	ConfluencePageIdentifier getId();

	Calendar getLastVisit();

	String getPageId();

	ConfluenceInstanceIdentifier getInstanceId();

}
