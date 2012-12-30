package de.benjaminborbe.message.util;

import com.google.inject.Singleton;

import de.benjaminborbe.message.api.MessageConsumer;
import de.benjaminborbe.tools.registry.RegistryBase;

@Singleton
public class MessageConsumerRegistry extends RegistryBase<MessageConsumer> {

	// private final Map<String, Set<MessageConsumer>> data = new HashMap<String,
	// Set<MessageConsumer>>();
	//
	// @Override
	// public void add(final MessageConsumer object) {
	// final Set<MessageConsumer> set = getSet(object.getType());
	// set.add(object);
	// super.add(object);
	// }
	//
	// @Override
	// public void remove(final MessageConsumer object) {
	// final Set<MessageConsumer> set = getSet(object.getType());
	// set.remove(object);
	// super.remove(object);
	// }
	//
	// private synchronized Set<MessageConsumer> getSet(final String type) {
	// if (data.containsKey(type)) {
	// return data.get(type);
	// }
	// else {
	// final Set<MessageConsumer> set = new HashSet<MessageConsumer>();
	// data.put(type, set);
	// return set;
	// }
	// }
	//
	// public Collection<MessageConsumer> get(final String type) {
	// return getSet(type);
	// }
}
