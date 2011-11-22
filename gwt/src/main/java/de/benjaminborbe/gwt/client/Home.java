package de.benjaminborbe.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Home implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final VerticalPanel vPanel = new VerticalPanel();
		final Label label = new Label();
		label.setText("hello");
		vPanel.add(label);
		RootPanel.get().add(vPanel);
	}

}
