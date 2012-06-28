package de.benjaminborbe.lunch.mock;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.lunch.api.Lunch;
import de.benjaminborbe.lunch.api.LunchService;

@Singleton
public class LunchServiceMock implements LunchService {

	@Inject
	public LunchServiceMock() {
	}

	@Override
	public Collection<Lunch> getLunchs(final SessionIdentifier sessionIdentifier) {
		final List<Lunch> result = new ArrayList<Lunch>();
		final Lunch lunch = new Lunch() {

			@Override
			public String getName() {
				return "Pizza";
			}

			@Override
			public Date getDate() {
				return new Date();
			}

			@Override
			public boolean isSubscribed() {
				return true;
			}

			@Override
			public URL getUrl() {
				try {
					return new URL("http://test.de");
				}
				catch (final MalformedURLException e) {
					return null;
				}
			}
		};
		result.add(lunch);
		return result;
	}

}
