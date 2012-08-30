package de.benjaminborbe.checklist.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.checklist.api.ChecklistService;

@Singleton
public class ChecklistServiceMock implements ChecklistService {

	@Inject
	public ChecklistServiceMock() {
	}

	@Override
	public void execute() {
	}
}
