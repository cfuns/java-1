package de.benjaminborbe.projectile.api;

import java.util.Calendar;

public interface ProjectileSlacktimeReport {

	Double getMonthBillable();

	Double getMonthExtern();

	Double getMonthIntern();

	Double getMonthTarget();

	Calendar getMonthUpdateDate();

	String getName();

	Double getWeekBillable();

	Double getWeekExtern();

	Double getWeekIntern();

	Double getWeekTarget();

	Calendar getWeekUpdateDate();

	Double getYearBillable();

	Double getYearExtern();

	Double getYearIntern();

	Double getYearTarget();

	Calendar getYearUpdateDate();

}
