package de.benjaminborbe.monitoring.check;

import javax.naming.NamingException;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.jndi.JndiContext;

public class RisikoNode extends HasChildNodesImpl implements HasChildNodes {

	@Inject
	public RisikoNode(final Logger logger, final HudsonCheckBuilder hudsonCheckBuilder, final JndiContext jndiContext) {

		// hudson checks
		{
			final String name = "Hudson-Check on Risiko-Tests";
			final String url = "https://code.allianz24.de/hudson/";
			final String job = "Risikoleben trunk";
			try {
				addNode(new HasCheckNodeImpl(hudsonCheckBuilder.buildCheck(name, url, job, (String) jndiContext.lookup("az_username"), (String) jndiContext.lookup("az_password"))));
			}
			catch (final NamingException e) {
				logger.info("skip add node: " + name);
			}
		}
	}
}
