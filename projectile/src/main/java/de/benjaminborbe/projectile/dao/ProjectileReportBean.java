package de.benjaminborbe.projectile.dao;

import java.util.Calendar;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class ProjectileReportBean extends EntityBase<ProjectileReportIdentifier> implements HasCreated, ProjectileSlacktimeReport, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private Calendar created;

	private Calendar modified;

	private String username;

	private Double weekIntern;

	private Double weekExtern;

	private Double monthIntern;

	private Double monthExtern;

	private Double yearIntern;

	private Double yearExtern;

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	@Override
	public Double getWeekIntern() {
		return weekIntern;
	}

	public void setWeekIntern(final Double weekIntern) {
		this.weekIntern = weekIntern;
	}

	@Override
	public Double getWeekExtern() {
		return weekExtern;
	}

	public void setWeekExtern(final Double weekExtern) {
		this.weekExtern = weekExtern;
	}

	@Override
	public Double getMonthIntern() {
		return monthIntern;
	}

	public void setMonthIntern(final Double monthIntern) {
		this.monthIntern = monthIntern;
	}

	@Override
	public Double getMonthExtern() {
		return monthExtern;
	}

	public void setMonthExtern(final Double monthExtern) {
		this.monthExtern = monthExtern;
	}

	@Override
	public Double getYearIntern() {
		return yearIntern;
	}

	public void setYearIntern(final Double yearIntern) {
		this.yearIntern = yearIntern;
	}

	@Override
	public Double getYearExtern() {
		return yearExtern;
	}

	public void setYearExtern(final Double yearExtern) {
		this.yearExtern = yearExtern;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
