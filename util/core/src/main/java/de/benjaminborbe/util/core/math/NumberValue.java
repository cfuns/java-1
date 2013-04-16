package de.benjaminborbe.util.core.math;

public class NumberValue implements HasValue {

	private final long value;

	private final long offset;

	public NumberValue(final String value) {
		final int pos = value.indexOf('.');
		if (pos == -1) {
			this.value = Long.parseLong(value);
			this.offset = 1;
		} else {
			final String p1 = value.substring(0, pos);
			final String p2 = value.substring(pos + 1);
			if (Long.parseLong(p2) == 0l) {
				this.value = Long.parseLong(p1);
				this.offset = 1;
			} else {
				this.value = Long.parseLong(p1 + p2);
				final int h = value.length() - pos - 1;
				int o = 1;
				for (int i = 0; i < h; ++i) {
					o = o * 10;
				}
				this.offset = o;
			}
		}
	}

	@Override
	public double getValue() {
		return 1d * value / offset;
	}

	@Override
	public String asString() {
		if (offset == 1) {
			return String.valueOf(value);
		} else {
			return String.valueOf(getValue());
		}
	}

}
