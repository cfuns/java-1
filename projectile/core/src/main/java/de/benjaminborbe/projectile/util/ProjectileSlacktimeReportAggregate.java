package de.benjaminborbe.projectile.util;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;

import java.util.Calendar;
import java.util.List;

public class ProjectileSlacktimeReportAggregate implements ProjectileSlacktimeReport {

	private final String name;

	private final List<ProjectileSlacktimeReport> reports;

	public ProjectileSlacktimeReportAggregate(final String name, final List<ProjectileSlacktimeReport> reports) {
		this.name = name;
		this.reports = reports;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Double getWeekIntern() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getWeekIntern();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getWeekExtern() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getWeekExtern();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getMonthIntern() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getMonthIntern();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getMonthExtern() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getMonthExtern();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getYearIntern() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getYearIntern();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getYearExtern() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getYearExtern();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getWeekTarget() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getWeekTarget();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getWeekBillable() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getWeekBillable();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getMonthTarget() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getMonthTarget();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getMonthBillable() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getMonthBillable();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getYearTarget() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getYearTarget();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Double getYearBillable() {
		Double result = 0d;
		for (final ProjectileSlacktimeReport report : reports) {
			final Double value = report.getYearBillable();
			if (value == null) {
				return null;
			}
			result += value;
		}
		return result;
	}

	@Override
	public Calendar getMonthUpdateDate() {
		for (final ProjectileSlacktimeReport report : reports) {
			final Calendar value = report.getMonthUpdateDate();
			if (value == null) {
				return null;
			}
		}
		return null;
	}

	@Override
	public Calendar getWeekUpdateDate() {
		for (final ProjectileSlacktimeReport report : reports) {
			final Calendar value = report.getWeekUpdateDate();
			if (value == null) {
				return null;
			}
		}
		return null;
	}

	@Override
	public Calendar getYearUpdateDate() {
		for (final ProjectileSlacktimeReport report : reports) {
			final Calendar value = report.getYearUpdateDate();
			if (value == null) {
				return null;
			}
		}
		return null;
	}

}
