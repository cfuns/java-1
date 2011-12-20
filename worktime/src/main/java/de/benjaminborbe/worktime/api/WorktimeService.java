package de.benjaminborbe.worktime.api;

import java.util.List;

public interface WorktimeService {

	List<Workday> getTimes(int days);
}
