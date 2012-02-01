package de.benjaminborbe.worktime.mock;

import java.util.ArrayList;
import java.util.List;

import de.benjaminborbe.worktime.api.Workday;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.api.WorktimeServiceException;

public class WorktimeServiceMock implements WorktimeService {

	@Override
	public List<Workday> getTimes(final int days) throws WorktimeServiceException {
		return new ArrayList<Workday>();
	}

}
