package de.benjaminborbe.projectile.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.LineIterator;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class ProjectileCsvReportToDtoConverter {

	private final ParseUtil parseUtil;

	private final Logger logger;

	@Inject
	public ProjectileCsvReportToDtoConverter(final Logger logger, final ParseUtil parseUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	public List<ProjectileCsvReportToDto> convert(final String csvString) throws ParseException {
		logger.debug("convert");
		final List<ProjectileCsvReportToDto> result = new ArrayList<ProjectileCsvReportToDto>();
		if (csvString == null) {
			return result;
		}
		final LineIterator lineIterator = new LineIterator(csvString);
		consumeHeader(lineIterator);

		while (lineIterator.hasNext()) {
			final String line = lineIterator.next();
			if (line != null) {
				final String[] parts = line.split(";");
				if (parts != null && parts.length >= 4) {
					final String username = parts[0];
					final String extern = parts[2];
					final String intern = parts[3];
					if (username != null && username.trim().length() > 0) {
						result.add(buildBean(username, extern, intern));
					}
				}
			}
		}

		return result;
	}

	private ProjectileCsvReportToDto buildBean(final String username, final String extern, final String intern) throws ParseException {
		final ProjectileCsvReportToDto projectileReportBean = new ProjectileCsvReportToDto();
		projectileReportBean.setUsername(username);
		projectileReportBean.setExtern(parseDouble(extern));
		projectileReportBean.setIntern(parseDouble(intern));
		return projectileReportBean;
	}

	private Double parseDouble(final String number) throws ParseException {
		return parseUtil.parseDouble(number.replaceAll("\\.", "").replaceAll(",", "."));
	}

	private void consumeHeader(final LineIterator lineIterator) {
		while (lineIterator.hasNext()) {
			final String line = lineIterator.next();
			if (line != null && line.startsWith("Mitarbeiter")) {
				return;
			}
		}
	}

}
