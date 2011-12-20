package de.benjaminborbe.worktime.api;

import de.benjaminborbe.storage.api.StorageException;

public interface WorktimeRecorder {

	void recordWorktime() throws StorageException;
}
