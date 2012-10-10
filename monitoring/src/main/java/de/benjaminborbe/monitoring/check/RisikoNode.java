package de.benjaminborbe.monitoring.check;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.config.MonitoringConfig;

public class RisikoNode extends HasChildNodesImpl implements HasChildNodes {

	@Inject
	public RisikoNode(final Logger logger, final HudsonCheckBuilder hudsonCheckBuilder, final MonitoringConfig monitoringConfig) {
		// hudson checks
		{
			final String name = "Hudson-Check on Risiko-Tests";
			final String url = "https://code.allianz24.de/hudson/";
			final String job = "Risikoleben trunk";
			addNode(new HasCheckNodeImpl(hudsonCheckBuilder.buildCheck(name, url, job, monitoringConfig.getAzUsername(), monitoringConfig.getAzPassword())));
		}
	}
}
