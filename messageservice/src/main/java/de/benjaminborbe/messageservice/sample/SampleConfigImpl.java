package de.benjaminborbe.messageservice.sample;

public class SampleConfigImpl implements SampleConfig {

	@Override
	public String getConnectionUrl() {
		// tcp://jms.twentyfeet.com:61616?jms.prefetchPolicy.all=1&wireFormat.maxInactivityDuration=1200000
		return "tcp://localhost:61616";
	}

	@Override
	public String getQueueName() {
		return "samplequeue";
	}

}
