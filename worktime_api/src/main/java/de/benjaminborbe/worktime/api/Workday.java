package de.benjaminborbe.worktime.api;

import java.util.Calendar;

public interface Workday {

	Calendar getDate();

	Calendar getStart();

	Calendar getEnd();
}
