package de.benjaminborbe.lunch.api;

import java.net.URL;
import java.util.Date;

public interface Lunch {

	String getName();

	Date getDate();

	boolean isSubscribed();

	URL getUrl();
}
