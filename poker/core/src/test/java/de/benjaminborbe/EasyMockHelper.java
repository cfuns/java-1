package de.benjaminborbe;

import org.easymock.EasyMock;

import java.util.ArrayList;
import java.util.List;

public class EasyMockHelper {

	private final List<Object> mocks = new ArrayList<Object>();

	public <T extends Object> T createMock(final Class<T> clazz) {
		return add(EasyMock.createMock(clazz));
	}

	public <T extends Object> T createNiceMock(final Class<T> clazz) {
		return add(EasyMock.createNiceMock(clazz));
	}

	private <T extends Object> T add(final T mock) {
		mocks.add(mock);
		return mock;
	}

	public void replay() {
		EasyMock.replay(mocks.toArray(new Object[mocks.size()]));
	}

	public void verify() {
		EasyMock.verify(mocks.toArray(new Object[mocks.size()]));
	}

}