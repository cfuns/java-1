package de.benjaminborbe.tools.bk;

public class BkUtil {

	public static boolean equals(final Object o1, final Object o2) {
		if (!(o1 instanceof HasBk) || !(o2 instanceof HasBk))
			return false;
		final HasBk bk1 = (HasBk) o1;
		final HasBk bk2 = (HasBk) o2;
		if (bk1 == null || bk1.getBk() == null || bk2 == null || bk2.getBk() == null)
			return false;
		if (!bk1.getClass().equals(bk2.getClass()))
			return false;
		return bk1.getBk().equals(bk2.getBk());
	}
}
