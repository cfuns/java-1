package de.benjaminborbe.filestorage.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.filestorage.api.FileDescriptor;
import de.benjaminborbe.filestorage.api.FilestorageService;

@Singleton
public class FilestorageServiceImpl implements FilestorageService {

	private final Logger logger;

	@Inject
	public FilestorageServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public List<FileDescriptor> listFiles() {
		logger.trace("listFiles");
		return new ArrayList<FileDescriptor>();
	}

}
