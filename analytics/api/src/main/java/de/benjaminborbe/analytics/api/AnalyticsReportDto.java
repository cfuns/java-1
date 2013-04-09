package de.benjaminborbe.analytics.api;

public class AnalyticsReportDto implements AnalyticsReport {

	private AnalyticsReportIdentifier id;

	private String name;

	private AnalyticsReportAggregation aggregation;

	public AnalyticsReportDto() {
	}

	public AnalyticsReportDto(final AnalyticsReportIdentifier id, final String name, final AnalyticsReportAggregation aggregation) {
		this.id = id;
		this.name = name;
		this.aggregation = aggregation;
	}

	@Override
	public AnalyticsReportIdentifier getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setId(final AnalyticsReportIdentifier id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public AnalyticsReportAggregation getAggregation() {
		return aggregation;
	}

	public void setAggregation(final AnalyticsReportAggregation aggregation) {
		this.aggregation = aggregation;
	}

}
