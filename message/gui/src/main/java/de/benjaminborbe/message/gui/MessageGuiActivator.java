package de.benjaminborbe.message.gui;

import de.benjaminborbe.message.gui.guice.MessageGuiModules;
import de.benjaminborbe.message.gui.service.MessageGuiNavigationEntry;
import de.benjaminborbe.message.gui.servlet.MessageGuiDeleteByTypeServlet;
import de.benjaminborbe.message.gui.servlet.MessageGuiExchangeMessagesServlet;
import de.benjaminborbe.message.gui.servlet.MessageGuiMessageDeleteServlet;
import de.benjaminborbe.message.gui.servlet.MessageGuiMessageListServlet;
import de.benjaminborbe.message.gui.servlet.MessageGuiUnlockExpiredMessagesServlet;
import de.benjaminborbe.navigation.api.NavigationEntry;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServiceInfo;
import de.benjaminborbe.tools.osgi.ServletInfo;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MessageGuiActivator extends HttpBundleActivator {

	@Inject
	private MessageGuiExchangeMessagesServlet messageGuiExchangeMessagesServlet;

	@Inject
	private MessageGuiMessageDeleteServlet messageGuiMessageDeleteServlet;

	@Inject
	private MessageGuiNavigationEntry messageGuiNavigationEntry;

	@Inject
	private MessageGuiMessageListServlet messageGuiMessageListServlet;

	@Inject
	private MessageGuiDeleteByTypeServlet messageGuiDeleteByTypeServlet;

	@Inject
	private MessageGuiUnlockExpiredMessagesServlet messageserviceGuiUnlockExpiredMessagesServlet;

	public MessageGuiActivator() {
		super(MessageGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MessageGuiModules(context);
	}

	@Override
	public Collection<ServiceInfo> getServiceInfos() {
		final Set<ServiceInfo> result = new HashSet<>(super.getServiceInfos());
		result.add(new ServiceInfo(NavigationEntry.class, messageGuiNavigationEntry));
		return result;
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<>(super.getServletInfos());
		result.add(new ServletInfo(messageserviceGuiUnlockExpiredMessagesServlet, MessageGuiConstants.URL_UNLOCK));
		result.add(new ServletInfo(messageGuiDeleteByTypeServlet, MessageGuiConstants.URL_DELETE_BY_TYPE));
		result.add(new ServletInfo(messageGuiMessageListServlet, MessageGuiConstants.URL_MESSAGE_LIST));
		result.add(new ServletInfo(messageGuiMessageDeleteServlet, MessageGuiConstants.URL_MESSAGE_DELETE));
		result.add(new ServletInfo(messageGuiExchangeMessagesServlet, MessageGuiConstants.URL_MESSAGE_EXCHANGE));

		return result;
	}
}
