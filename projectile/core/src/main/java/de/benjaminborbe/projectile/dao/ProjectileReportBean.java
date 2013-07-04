package de.benjaminborbe.projectile.dao;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.util.Calendar;

public class ProjectileReportBean extends EntityBase<ProjectileReportIdentifier> implements HasCreated, ProjectileSlacktimeReport, HasModified {

	private static final long serialVersionUID = 5362529362425496903L;

	private ProjectileReportIdentifier id;

	private Calendar created;

	private Calendar modified;

	private String name;

	private Double weekIntern;

	private Double weekExtern;

	private Double weekTarget;

	private Double weekBillable;

	private Double monthIntern;

	private Double monthExtern;

	private Double monthTarget;

	private Double monthBillable;

	private Double yearIntern;

	private Double yearExtern;

	private Double yearTarget;

	private Double yearBillable;

	private Calendar yearUpdateDate;

	private Calendar monthUpdateDate;

	private Calendar weekUpdateDate;

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
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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

	@Override
	public ProjectileReportIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final ProjectileReportIdentifier id) {
		this.id = id;
	}

	@Override
	public Double getWeekTarget() {
		return weekTarget;
	}

	public void setWeekTarget(final Double weekTarget) {
		this.weekTarget = weekTarget;
	}

	@Override
	public Double getWeekBillable() {
		return weekBillable;
	}

	public void setWeekBillable(final Double weekBillable) {
		this.weekBillable = weekBillable;
	}

	@Override
	public Double getMonthTarget() {
		return monthTarget;
	}

	public void setMonthTarget(final Double monthTarget) {
		this.monthTarget = monthTarget;
	}

	@Override
	public Double getMonthBillable() {
		return monthBillable;
	}

	public void setMonthBillable(final Double monthBillable) {
		this.monthBillable = monthBillable;
	}

	@Override
	public Double getYearTarget() {
		return yearTarget;
	}

	public void setYearTarget(final Double yearTarget) {
		this.yearTarget = yearTarget;
	}

	@Override
	public Double getYearBillable() {
		return yearBillable;
	}

	public void setYearBillable(final Double yearBillable) {
		this.yearBillable = yearBillable;
	}

	@Override
	public Calendar getYearUpdateDate() {
		return yearUpdateDate;
	}

	public void setYearUpdateDate(final Calendar yearUpdateDate) {
		this.yearUpdateDate = yearUpdateDate;
	}

	@Override
	public Calendar getMonthUpdateDate() {
		return monthUpdateDate;
	}

	public void setMonthUpdateDate(final Calendar monthUpdateDate) {
		this.monthUpdateDate = monthUpdateDate;
	}

	@Override
	public Calendar getWeekUpdateDate() {
		return weekUpdateDate;
	}

	public void setWeekUpdateDate(final Calendar weekUpdateDate) {
		this.weekUpdateDate = weekUpdateDate;
	}

}
