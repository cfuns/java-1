package de.benjaminborbe.projectile.util;

import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;
import de.benjaminborbe.projectile.dao.ProjectileReportBean;
import de.benjaminborbe.projectile.dao.ProjectileReportDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.ParseException;

public class ProjectileCsvReportImporter {

	private final Logger logger;

	private final ProjectileCsvReportToDtoConverter projectileCsvReportToBeanConverter;

	private final ProjectileReportDao projectileReportDao;

	@Inject
	public ProjectileCsvReportImporter(final Logger logger, final ProjectileReportDao projectileReportDao, final ProjectileCsvReportToDtoConverter projectileCsvReportToBeanConverter) {
		this.logger = logger;
		this.projectileReportDao = projectileReportDao;
		this.projectileCsvReportToBeanConverter = projectileCsvReportToBeanConverter;
	}

	public void importCsvReport(final String csvString, final ProjectileSlacktimeReportInterval interval) throws StorageException, ParseException {
		logger.debug("importCsvReport");
		final List<ProjectileCsvReportToDto> dtos = projectileCsvReportToBeanConverter.convert(csvString);
		logger.debug("extracted " + dtos.size() + " report");
		for (final ProjectileCsvReportToDto dto : dtos) {
			final ProjectileReportBean bean = projectileReportDao.findOrCreateByUsername(dto.getUsername());
			if (ProjectileSlacktimeReportInterval.WEEK.equals(interval)) {
				bean.setWeekExtern(dto.getExtern());
				bean.setWeekIntern(dto.getIntern());
			}
			if (ProjectileSlacktimeReportInterval.MONTH.equals(interval)) {
				bean.setMonthExtern(dto.getExtern());
				bean.setMonthIntern(dto.getIntern());
			}
			if (ProjectileSlacktimeReportInterval.YEAR.equals(interval)) {
				bean.setYearExtern(dto.getExtern());
				bean.setYearIntern(dto.getIntern());
			}
			projectileReportDao.save(bean);
		}
	}
}
