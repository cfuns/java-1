package de.benjaminborbe.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Home implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final Label title = new Label("Hello World");
		final Button button = new Button("press me");
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				Window.alert("welcome");
			}
		});
		final VerticalPanel vp = new VerticalPanel();
		vp.add(title);
		vp.add(button);
		RootPanel.get().add(vp);

	}
}
