package de.benjaminborbe.proxy.api;

public interface ProxyConversation {

	ProxyContent getRequest();

	ProxyContent getResponse();

	ProxyConversationIdentifier getId();
}
