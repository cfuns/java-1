package de.benjaminborbe.distributed.search.api;

public class DistributedSearchServiceException extends Exception {

	private static final long serialVersionUID = -7494940987312260479L;

	public DistributedSearchServiceException(final String arg0, final Throwable arg1) {
		super(arg0, arg1);
	}

	public DistributedSearchServiceException(final String arg0) {
		super(arg0);
	}

	public DistributedSearchServiceException(final Throwable arg0) {
		super(arg0);
	}

}
