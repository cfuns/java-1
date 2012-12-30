package de.benjaminborbe.distributed.index.api;

public class DistributedIndexServiceException extends Exception {

	private static final long serialVersionUID = 4471027016324953520L;

	public DistributedIndexServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public DistributedIndexServiceException(final String arg0) {
		super(arg0);
	}

	public DistributedIndexServiceException(final Throwable arg0) {
		super(arg0);
	}

}
