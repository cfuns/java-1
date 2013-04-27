package de.benjaminborbe.projectile.util;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;
import de.benjaminborbe.projectile.dao.ProjectileReportBean;
import de.benjaminborbe.projectile.dao.ProjectileReportDao;
import de.benjaminborbe.storage.api.StorageException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ProjectileReportListenerDao implements ProjectileReportListener {

	private final ProjectileReportDao projectileReportDao;

	private final Logger logger;

	@Inject
	public ProjectileReportListenerDao(final Logger logger, final ProjectileReportDao projectileReportDao) {
		this.logger = logger;
		this.projectileReportDao = projectileReportDao;
	}

	@Override
	public void onReport(final ProjectileSlacktimeReportInterval interval, final ProjectileCsvReportToDto dto) {
		try {
			final ProjectileReportBean bean = projectileReportDao.findOrCreateByUsername(dto.getUsername());
			if (ProjectileSlacktimeReportInterval.WEEK.equals(interval)) {
				bean.setWeekExtern(dto.getExtern());
				bean.setWeekIntern(dto.getIntern());
				bean.setWeekBillable(dto.getBillable());
				bean.setWeekTarget(dto.getTarget());
				bean.setWeekUpdateDate(dto.getUpdateDate());
			}
			if (ProjectileSlacktimeReportInterval.MONTH.equals(interval)) {
				bean.setMonthExtern(dto.getExtern());
				bean.setMonthIntern(dto.getIntern());
				bean.setMonthBillable(dto.getBillable());
				bean.setMonthTarget(dto.getTarget());
				bean.setMonthUpdateDate(dto.getUpdateDate());
			}
			if (ProjectileSlacktimeReportInterval.YEAR.equals(interval)) {
				bean.setYearExtern(dto.getExtern());
				bean.setYearIntern(dto.getIntern());
				bean.setYearBillable(dto.getBillable());
				bean.setYearTarget(dto.getTarget());
				bean.setYearUpdateDate(dto.getUpdateDate());
			}
			projectileReportDao.save(bean);
		} catch (final StorageException e) {
			logger.warn(e.getClass().getName(), e);
		}
	}

}
