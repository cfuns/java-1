package de.benjaminborbe.poker.table.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;

import de.benjaminborbe.poker.table.client.message.DefaultMessages;

public class MainPanel extends Composite {

	@Inject
	public MainPanel(final DefaultMessages defaultMessages, final PokerConnector pokerConnector) {
		final Label title = new Label(defaultMessages.getTitle());
		final Button button = new Button(defaultMessages.getButtonText());
		button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				// Window.alert(defaultMessages.getWelcomeMessage());
				pokerConnector.getStatus();
			}
		});
		final VerticalPanel vp = new VerticalPanel();
		vp.add(title);
		vp.add(button);

		// All composites must call initWidget() in their constructors.
		initWidget(vp);
	}

}
