package de.benjaminborbe.storage.api;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class StorageValue {

	private final String valueString;

	private final byte[] valueByte;

	private final String encoding;

	public StorageValue(final String encoding) {
		this.encoding = encoding;
		this.valueString = null;
		this.valueByte = null;
	}

	public StorageValue(final byte[] valueByte, final String encoding) {
		this.encoding = encoding;
		this.valueByte = valueByte;
		this.valueString = null;
	}

	public StorageValue(final String valueString, final String encoding) {
		this.encoding = encoding;
		this.valueString = valueString;
		this.valueByte = null;
	}

	public byte[] getByte() throws UnsupportedEncodingException {
		if (valueByte != null) {
			return valueByte;
		}
		if (valueString != null) {
			return valueString.getBytes(encoding);
		}
		return null;
	}

	public String getString() throws UnsupportedEncodingException {
		if (valueString != null) {
			return valueString;
		}
		if (valueByte != null) {
			return new String(valueByte, encoding);
		}
		return null;
	}

	public boolean isEmpty() {
		return valueByte == null && valueString == null;
	}

	@Override
	public int hashCode() {
		try {
			final String s = getString();
			if (s != null) {
				return s.hashCode();
			}
		}
		catch (final UnsupportedEncodingException e) {
		}
		return 23;
	}

	@Override
	public String toString() {
		try {
			return getString();
		}
		catch (final UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (!(other instanceof StorageValue))
			return false;
		if (!getClass().equals(other.getClass()))
			return false;
		final StorageValue otherValue = (StorageValue) other;
		if (valueByte != null && otherValue.valueByte != null) {
			return Arrays.equals(valueByte, otherValue.valueByte);
		}
		if (valueString != null && otherValue.valueString != null) {
			return valueString.equals(otherValue.valueString);
		}
		try {
			final String s1 = getString();
			final String s2 = otherValue.getString();
			return s1 == null && s2 == null || s1 != null && s2 != null && s1.equals(s2);
		}
		catch (final UnsupportedEncodingException e) {
			return false;
		}
	}
}
