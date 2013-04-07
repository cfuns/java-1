package de.benjaminborbe.virt.api;

public class VirtIpAddress {

	private final int a;

	private final int b;

	private final int c;

	private final int d;

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public int getC() {
		return c;
	}

	public int getD() {
		return d;
	}

	public VirtIpAddress(final int a, final int b, final int c, final int d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final VirtIpAddress that = (VirtIpAddress) o;

		if (a != that.a) return false;
		if (b != that.b) return false;
		if (c != that.c) return false;
		if (d != that.d) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = a;
		result = 31 * result + b;
		result = 31 * result + c;
		result = 31 * result + d;
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(a);
		sb.append(".");
		sb.append(b);
		sb.append(".");
		sb.append(c);
		sb.append(".");
		sb.append(d);
		return sb.toString();
	}
}
