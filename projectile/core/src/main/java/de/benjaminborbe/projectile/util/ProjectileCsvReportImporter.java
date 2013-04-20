package de.benjaminborbe.projectile.util;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;

import javax.inject.Inject;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.ParseException;

public class ProjectileCsvReportImporter {

	private final Logger logger;

	private final ProjectileCsvReportToDtoConverter projectileCsvReportToBeanConverter;

	private final ProjectileReportListenerRegistry projectileReportListenerRegistry;

	@Inject
	public ProjectileCsvReportImporter(
			final Logger logger,
			final ProjectileReportListenerRegistry projectileReportListenerRegistry,
			final ProjectileCsvReportToDtoConverter projectileCsvReportToBeanConverter) {
		this.logger = logger;
		this.projectileReportListenerRegistry = projectileReportListenerRegistry;
		this.projectileCsvReportToBeanConverter = projectileCsvReportToBeanConverter;
	}

	public void importCsvReport(final Calendar date, final String csvString, final ProjectileSlacktimeReportInterval interval) throws StorageException, ParseException {
		logger.debug("importCsvReport");
		final List<ProjectileCsvReportToDto> dtos = projectileCsvReportToBeanConverter.convert(date, csvString);
		logger.debug("extracted " + dtos.size() + " report");
		for (final ProjectileReportListener listener : projectileReportListenerRegistry.getAll()) {
			for (final ProjectileCsvReportToDto dto : dtos) {
				listener.onReport(interval, dto);
			}
		}
	}
}
