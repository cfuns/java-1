package de.benjaminborbe.worktime.core.util;

import de.benjaminborbe.storage.api.StorageException;

import java.util.Calendar;
import java.util.Collection;

public interface WorktimeStorageService {

	void save(WorktimeValue value) throws StorageException;

	Collection<WorktimeValue> findByDate(Calendar calendar) throws StorageException;

}
