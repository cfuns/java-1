package de.benjaminborbe.analytics.gui.chart;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class AnalyticsReportChartColorGenerator {

	private final class Color {

		private final int red;

		private final int green;

		private final int blue;

		public Color(final int red, final int green, final int blue) {
			super();
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		public String getCode() {
			final StringBuilder sb = new StringBuilder();
			sb.append("#");
			sb.append(fillNull(Integer.toHexString(red)));
			sb.append(fillNull(Integer.toHexString(green)));
			sb.append(fillNull(Integer.toHexString(blue)));
			return sb.toString();
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("r: ");
			sb.append(fillNull(Integer.toHexString(red)));
			sb.append(" g: ");
			sb.append(fillNull(Integer.toHexString(green)));
			sb.append(" b: ");
			sb.append(fillNull(Integer.toHexString(blue)));
			return sb.toString();
		}
	}

	private final List<Color> colors = new ArrayList<>();

	@Inject
	public AnalyticsReportChartColorGenerator() {
		int red = 0xFF;
		int green = 0x0;
		int blue = 0x0;
		while (blue < 0xFF) {
			colors.add(buildColor(red, green, blue));
			blue++;
		}
		while (red > 0x0) {
			colors.add(buildColor(red, green, blue));
			red--;
		}
		while (green < 0xFF) {
			colors.add(buildColor(red, green, blue));
			green++;
		}
		while (blue > 0x0) {
			colors.add(buildColor(red, green, blue));
			blue--;
		}
		while (red < 0xFF) {
			colors.add(buildColor(red, green, blue));
			red++;
		}
		while (green > 0x0) {
			colors.add(buildColor(red, green, blue));
			green--;
		}
	}

	private Color buildColor(final int red, final int green, final int blue) {
		final Color color = new Color(red, green, blue);
		return color;
	}

	private String fillNull(final String hexString) {
		return hexString.length() == 1 ? "0" + hexString.toUpperCase() : hexString.toUpperCase();
	}

	public List<String> getColors(final int amount) {
		final int limit = Math.min(colors.size(), amount);
		final List<String> result = new ArrayList<>();
		if (limit == 0) {
			return result;
		}
		final int parts = colors.size() / limit;
		for (int i = 0; i < limit; ++i) {
			result.add(colors.get(i * parts).getCode());
		}
		return result;
	}
}
