package de.benjaminborbe.poker.table.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;

import de.benjaminborbe.poker.table.client.MainPanel;
import de.benjaminborbe.poker.table.client.message.DefaultMessages;
import de.benjaminborbe.poker.table.client.message.DefaultMessagesImpl;

public class PokerTableClientModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(MainPanel.class).in(Singleton.class);
		bind(DefaultMessages.class).to(DefaultMessagesImpl.class).in(Singleton.class);
	}

}
