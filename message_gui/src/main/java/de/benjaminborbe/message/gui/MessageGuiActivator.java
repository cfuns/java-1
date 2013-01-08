package de.benjaminborbe.message.gui;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.BundleContext;

import com.google.inject.Inject;

import de.benjaminborbe.message.gui.guice.MessageGuiModules;
import de.benjaminborbe.message.gui.servlet.MessageGuiDeleteByTypeServlet;
import de.benjaminborbe.message.gui.servlet.MessageGuiServlet;
import de.benjaminborbe.message.gui.servlet.MessageGuiUnlockExpiredMessagesServlet;
import de.benjaminborbe.tools.guice.Modules;
import de.benjaminborbe.tools.osgi.HttpBundleActivator;
import de.benjaminborbe.tools.osgi.ServletInfo;

public class MessageGuiActivator extends HttpBundleActivator {

	@Inject
	private MessageGuiDeleteByTypeServlet messageGuiDeleteByTypeServlet;

	@Inject
	private MessageGuiUnlockExpiredMessagesServlet messageserviceGuiUnlockExpiredMessagesServlet;

	@Inject
	private MessageGuiServlet messageserviceGuiServlet;

	public MessageGuiActivator() {
		super(MessageGuiConstants.NAME);
	}

	@Override
	protected Modules getModules(final BundleContext context) {
		return new MessageGuiModules(context);
	}

	@Override
	protected Collection<ServletInfo> getServletInfos() {
		final Set<ServletInfo> result = new HashSet<ServletInfo>(super.getServletInfos());
		result.add(new ServletInfo(messageserviceGuiServlet, MessageGuiConstants.URL_HOME));
		result.add(new ServletInfo(messageserviceGuiUnlockExpiredMessagesServlet, MessageGuiConstants.URL_UNLOCK));
		result.add(new ServletInfo(messageGuiDeleteByTypeServlet, MessageGuiConstants.URL_DELETE));
		return result;
	}
}
