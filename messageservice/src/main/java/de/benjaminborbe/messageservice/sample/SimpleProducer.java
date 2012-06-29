package de.benjaminborbe.messageservice.sample;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;

import com.google.inject.Inject;

public final class SimpleProducer {

	private final Logger logger;

	private final SampleConfig sampleConfig;

	@Inject
	public SimpleProducer(final Logger logger, final SampleConfig sampleConfig) {
		this.logger = logger;
		this.sampleConfig = sampleConfig;
	}

	public void addMessage(final int messageAmount, final int messageSize) throws NamingException {
		logger.debug("addMessage messageAmount: " + messageAmount + " messageSize: " + messageSize);
		Connection connection = null;
		try {
			final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(sampleConfig.getConnectionUrl());
			connection = connectionFactory.createConnection();
			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final Destination destination = session.createQueue(sampleConfig.getQueueName());
			final MessageProducer producer = session.createProducer(destination);
			for (int i = 1; i <= messageAmount; i++) {
				final TextMessage message = session.createTextMessage();
				message.setText(buildMessage(messageSize));
				logger.trace("send message " + i);
				producer.send(message);
			}

			// producer.send(session.createMessage());
		}
		catch (final JMSException e) {
			logger.info("Exception occurred: " + e);
		}
		finally {
			if (connection != null) {
				try {
					connection.close();
				}
				catch (final JMSException e) {
				}
			}
		}
	}

	private String buildMessage(final int messageSize) {
		final StringWriter sw = new StringWriter();
		for (int i = 0; i < messageSize; ++i) {
			sw.append('a');
		}
		return sw.toString();
	}
}
