package de.benjaminborbe.projectile.util;

public class ProjectileCsvReportToDto {

	private String username;

	private Double intern;

	private Double extern;

	private Double billable;

	private Double target;

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public Double getIntern() {
		return intern;
	}

	public void setIntern(final Double intern) {
		this.intern = intern;
	}

	public Double getExtern() {
		return extern;
	}

	public void setExtern(final Double extern) {
		this.extern = extern;
	}

	public Double getBillable() {
		return billable;
	}

	public void setBillable(final Double billable) {
		this.billable = billable;
	}

	public Double getTarget() {
		return target;
	}

	public void setTarget(final Double target) {
		this.target = target;
	}
}
