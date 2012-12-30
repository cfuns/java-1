package de.benjaminborbe.cron.message;

import de.benjaminborbe.tools.mapper.MapException;

public interface CronMessageMapper {

	String map(CronMessage message) throws MapException;

	CronMessage map(String message) throws MapException;
}
