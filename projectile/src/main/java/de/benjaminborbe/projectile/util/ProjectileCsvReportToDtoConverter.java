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

	private final ProjectileNameMapper projectileNameMapper;

	@Inject
	public ProjectileCsvReportToDtoConverter(final Logger logger, final ParseUtil parseUtil, final ProjectileNameMapper projectileNameMapper) {
		this.logger = logger;
		this.parseUtil = parseUtil;
		this.projectileNameMapper = projectileNameMapper;
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
				if (parts != null && parts.length >= 5) {
					final String username = parts[0];
					final String target = parts[1];
					final String extern = parts[2];
					final String intern = parts[3];
					final String billable = parts[4];
					if (username != null && username.trim().length() > 0) {
						result.add(buildBean(username, target, extern, intern, billable));
					}
				}
			}
		}
		logger.debug("convert result.size " + result.size());

		return result;
	}

	private ProjectileCsvReportToDto buildBean(final String username, final String target, final String extern, final String intern, final String billable) throws ParseException {
		final ProjectileCsvReportToDto projectileReport = new ProjectileCsvReportToDto();
		projectileReport.setUsername(projectileNameMapper.fullnameToLogin(username));
		projectileReport.setExtern(parseDouble(extern));
		projectileReport.setIntern(parseDouble(intern));
		projectileReport.setBillable(parseDouble(billable));
		projectileReport.setTarget(parseDouble(target));
		return projectileReport;
	}

	private Double parseDouble(final String number) throws ParseException {
		return parseUtil.parseDouble(number.replaceAll("\\.", "").replaceAll(",", "."));
	}

	private void consumeHeader(final LineIterator lineIterator) {
		while (lineIterator.hasNext()) {
			final String line = lineIterator.next();
			if (line != null && (line.contains("Mitarbeiter") || line.contains("Login"))) {
				return;
			}
			else {
				logger.trace("consume line: " + line);
			}
		}
	}

}
