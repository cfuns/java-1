package de.benjaminborbe.proxy.api;

import java.net.URL;

public interface ProxyConversation {

	ProxyContent getRequest();

	ProxyContent getResponse();

	ProxyConversationIdentifier getId();

	URL getUrl();

	Long getDuration();
}
