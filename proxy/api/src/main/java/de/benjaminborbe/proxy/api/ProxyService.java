package de.benjaminborbe.proxy.api;

public interface ProxyService {

	long calc(long value) throws ProxyServiceException;

	void start() throws ProxyServiceException;

	void stop() throws ProxyServiceException;
}
