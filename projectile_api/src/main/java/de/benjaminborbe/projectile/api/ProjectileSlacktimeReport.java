package de.benjaminborbe.projectile.api;

public interface ProjectileSlacktimeReport {

	String getUsername();

	Double getWeekIntern();

	Double getWeekExtern();

	Double getMonthIntern();

	Double getMonthExtern();

	Double getYearIntern();

	Double getYearExtern();

	Double getWeekTarget();

	Double getWeekBillable();

	Double getMonthTarget();

	Double getMonthBillable();

	Double getYearTarget();

	Double getYearBillable();

}
