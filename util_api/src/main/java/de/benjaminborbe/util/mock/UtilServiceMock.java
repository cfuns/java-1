package de.benjaminborbe.util.mock;

import de.benjaminborbe.util.api.UtilService;
import de.benjaminborbe.util.api.UtilServiceException;

public class UtilServiceMock implements UtilService {

	@Override
	public double calc(String expression) throws UtilServiceException {
		return 0;
	}

}
