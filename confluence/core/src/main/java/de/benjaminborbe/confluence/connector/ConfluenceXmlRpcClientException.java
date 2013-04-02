package de.benjaminborbe.confluence.connector;

public class ConfluenceXmlRpcClientException extends Exception {

	private static final long serialVersionUID = -8955508463382915520L;

	public ConfluenceXmlRpcClientException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public ConfluenceXmlRpcClientException(final String arg0) {
		super(arg0);
	}

	public ConfluenceXmlRpcClientException(final Throwable arg0) {
		super(arg0);
	}

}
