package de.benjaminborbe.worktime.util;

import java.util.Calendar;
import java.util.Collection;

import de.benjaminborbe.storage.api.StorageException;

public interface WorktimeStorageService {

	void save(WorktimeValue value) throws StorageException;

	Collection<WorktimeValue> findByDate(Calendar calendar) throws StorageException;

}
