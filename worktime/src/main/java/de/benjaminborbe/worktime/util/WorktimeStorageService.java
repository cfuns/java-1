package de.benjaminborbe.worktime.util;

import java.util.Calendar;
import java.util.Collection;

public interface WorktimeStorageService {

	void save(WorktimeValue value);

	Collection<WorktimeValue> findByDate(Calendar calendar);

}
