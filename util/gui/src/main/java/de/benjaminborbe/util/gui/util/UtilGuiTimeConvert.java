package de.benjaminborbe.util.gui.util;

import java.util.Calendar;
import java.util.TimeZone;

public interface UtilGuiTimeConvert {

	Calendar convert(final Calendar calendar, final TimeZone timeZone);
}
