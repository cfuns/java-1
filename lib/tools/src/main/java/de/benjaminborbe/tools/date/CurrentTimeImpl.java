package de.benjaminborbe.tools.date;

public class CurrentTimeImpl implements CurrentTime {

	public CurrentTimeImpl() {
	}

	@Override
	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}

}
