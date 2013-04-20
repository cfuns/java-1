package de.benjaminborbe.gwt.client.gin;

import com.google.gwt.inject.client.AbstractGinModule;
import javax.inject.Singleton;

import de.benjaminborbe.gwt.client.MainPanel;
import de.benjaminborbe.gwt.client.message.DefaultMessages;
import de.benjaminborbe.gwt.client.message.DefaultMessagesImpl;

public class GwtClientModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(MainPanel.class).in(Singleton.class);
		bind(DefaultMessages.class).to(DefaultMessagesImpl.class).in(Singleton.class);
	}

}
