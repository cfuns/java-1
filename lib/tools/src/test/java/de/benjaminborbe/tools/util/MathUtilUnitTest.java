package de.benjaminborbe.tools.util;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MathUtilUnitTest {

	@Test
	public void testMax() throws Exception {
		MathUtil mathUtil = new MathUtil();
		assertThat(mathUtil.maxLong(0l), is(0l));
		assertThat(mathUtil.maxLong(0l, 1l), is(1l));
		assertThat(mathUtil.maxLong(null, 1l), is(1l));
		assertThat(mathUtil.maxLong(1l, null), is(1l));
	}

	@Test
	public void testMaxInteger() throws Exception {
		MathUtil mathUtil = new MathUtil();
		assertThat(mathUtil.maxInteger(0), is(0));
		assertThat(mathUtil.maxInteger(0, 1), is(1));
		assertThat(mathUtil.maxInteger(null, 1), is(1));
		assertThat(mathUtil.maxInteger(1, null), is(1));
	}

	@Test
	public void testMinInteger() throws Exception {
		MathUtil mathUtil = new MathUtil();
		assertThat(mathUtil.minInteger(2), is(2));
		assertThat(mathUtil.minInteger(0, 2), is(0));
		assertThat(mathUtil.minInteger(null, 1), is(1));
		assertThat(mathUtil.minInteger(1, null), is(1));
	}

	@Test
	public void testMinLong() throws Exception {
		MathUtil mathUtil = new MathUtil();
		assertThat(mathUtil.minLong(2l), is(2l));
		assertThat(mathUtil.minLong(0l, 2l), is(0l));
		assertThat(mathUtil.minLong(null, 1l), is(1l));
		assertThat(mathUtil.minLong(1l, null), is(1l));
	}
}
