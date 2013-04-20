package de.benjaminborbe.poker.table.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import javax.inject.Singleton;

import de.benjaminborbe.poker.table.client.MainPanel;
import de.benjaminborbe.poker.table.client.Model;
import de.benjaminborbe.poker.table.client.message.DefaultMessages;
import de.benjaminborbe.poker.table.client.message.DefaultMessagesImpl;

public class PokerTableClientModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(Model.class).in(Singleton.class);
		bind(MainPanel.class).in(Singleton.class);
		bind(DefaultMessages.class).to(DefaultMessagesImpl.class).in(Singleton.class);
	}

}
