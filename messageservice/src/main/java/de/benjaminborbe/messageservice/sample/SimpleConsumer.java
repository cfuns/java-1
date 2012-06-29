package de.benjaminborbe.messageservice.sample;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.naming.NamingException;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;

import com.google.inject.Inject;

public final class SimpleConsumer {

	private final Logger logger;

	private final SampleConfig sampleConfig;

	@Inject
	public SimpleConsumer(final Logger logger, final SampleConfig sampleConfig) {
		this.logger = logger;
		this.sampleConfig = sampleConfig;
	}

	public void receive() throws NamingException {

		Connection connection = null;
		try {
			final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(sampleConfig.getConnectionUrl());
			connection = connectionFactory.createConnection();

			final ExceptionListener exceptionListener = new SimpleExceptionListener();
			connection.setExceptionListener(exceptionListener);

			final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			final Destination destination = session.createQueue(sampleConfig.getQueueName());

			final MessageConsumer consumer = session.createConsumer(destination);

			final MessageListener messageListener = new SimpleMessageListener();
			consumer.setMessageListener(messageListener);
			connection.start();

			// while (true) {
			// try {
			// Thread.sleep(1000);
			// }
			// catch (final InterruptedException e) {
			// return;
			// }
			// }
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
}
