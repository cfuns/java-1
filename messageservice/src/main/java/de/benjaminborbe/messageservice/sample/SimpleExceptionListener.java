package de.benjaminborbe.messageservice.sample;

import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleExceptionListener implements ExceptionListener {

	private final static Logger logger = LoggerFactory.getLogger("example");

	@Override
	public void onException(final JMSException jmsException) {
		logger.warn(jmsException.getMessage(), jmsException.getCause());
	}

}
