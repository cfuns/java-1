package de.benjaminborbe.filestorage.mock;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.filestorage.api.FileDescriptor;
import de.benjaminborbe.filestorage.api.FilestorageService;

@Singleton
public class FilestorageServiceMock implements FilestorageService {

	@Inject
	public FilestorageServiceMock() {
	}

	@Override
	public List<FileDescriptor> listFiles() {
		return null;
	}

}
