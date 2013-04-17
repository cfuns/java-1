package de.benjaminborbe.worktime.mock;

import de.benjaminborbe.worktime.api.Workday;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.api.WorktimeServiceException;

import java.util.ArrayList;
import java.util.List;

public class WorktimeServiceMock implements WorktimeService {

	@Override
	public List<Workday> getTimes(final int days) throws WorktimeServiceException {
		return new ArrayList<>();
	}

	@Override
	public boolean isOffice() throws WorktimeServiceException {
		return false;
	}

}
