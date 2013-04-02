package de.benjaminborbe.xmlrpc.api;

public class XmlrpcServiceException extends Exception {

	private static final long serialVersionUID = 1292855184604109311L;

	public XmlrpcServiceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public XmlrpcServiceException(final String message) {
		super(message);
	}

	public XmlrpcServiceException(final Throwable cause) {
		super(cause);
	}

}
