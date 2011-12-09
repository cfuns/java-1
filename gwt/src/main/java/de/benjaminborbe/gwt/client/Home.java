package de.benjaminborbe.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import de.benjaminborbe.gwt.client.gin.GwtClientGinjector;

public class Home implements EntryPoint {

	private final GwtClientGinjector injector = GWT.create(GwtClientGinjector.class);

	@Override
	public void onModuleLoad() {
		final Widget panel = injector.getMainPanel();
		RootPanel.get().add(panel);

	}
}
