package de.benjaminborbe.worktime.api;

import java.util.List;

import de.benjaminborbe.storage.api.StorageException;

public interface WorktimeService {

	List<Workday> getTimes(int days) throws StorageException;
}
