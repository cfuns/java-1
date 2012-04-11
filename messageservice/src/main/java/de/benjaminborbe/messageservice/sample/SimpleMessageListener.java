package de.benjaminborbe.messageservice.sample;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMessageListener implements MessageListener {

	private final static Logger logger = LoggerFactory.getLogger("example");

	@Override
	public void onMessage(final Message message) {
		if (message instanceof TextMessage) {
			final TextMessage txtMsg = (TextMessage) message;
			try {
				txtMsg.getText();
				// System.err.println("text: " + );
			}
			catch (final JMSException e) {
				logger.error("JMSException", e);
			}
		}
	}
}
