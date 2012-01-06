package de.benjaminborbe.navigation.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.benjaminborbe.navigation.api.NavigationWidget;

public class NavigationWidgetMock implements NavigationWidget {

	@Override
	public void render(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
	}

}
