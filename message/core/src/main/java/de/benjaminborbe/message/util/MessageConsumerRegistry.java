package de.benjaminborbe.message.util;

import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MessageConsumerRegistry extends RegistryBase<MessageConsumer> {

	private final Map<String, MessageConsumer> data = new HashMap<>();

	@Override
	protected void onElementAdded(final MessageConsumer object) {
		data.put(object.getType(), object);
	}

	@Override
	protected void onElementRemoved(final MessageConsumer object) {
		data.remove(object.getType());
	}

	public MessageConsumer get(final String type) {
		return data.get(type);
	}
}
