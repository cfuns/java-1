package de.benjaminborbe.distributed.search.api;

import java.util.Calendar;

public interface DistributedSearchPage {

	String getIndex();

	String getTitle();

	String getContent();

	String getURL();

	Calendar getDate();

	Calendar getUpdated();

	Calendar getAdded();
}
