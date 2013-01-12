package de.benjaminborbe.analytics.dao;

import java.util.Calendar;

import de.benjaminborbe.analytics.api.AnalyticsReport;
import de.benjaminborbe.analytics.api.AnalyticsReportAggregation;
import de.benjaminborbe.analytics.api.AnalyticsReportIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class AnalyticsReportBean extends EntityBase<AnalyticsReportIdentifier> implements HasCreated, AnalyticsReport, HasModified {

	private static final long serialVersionUID = 5362529362425496903L;

	private AnalyticsReportIdentifier id;

	private Calendar created;

	private Calendar modified;

	private String name;

	private AnalyticsReportAggregation aggregation;

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
	public AnalyticsReportIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final AnalyticsReportIdentifier id) {
		this.id = id;
	}

	public AnalyticsReportAggregation getAggregation() {
		return aggregation;
	}

	public void setAggregation(AnalyticsReportAggregation aggregation) {
		this.aggregation = aggregation;
	}

}
