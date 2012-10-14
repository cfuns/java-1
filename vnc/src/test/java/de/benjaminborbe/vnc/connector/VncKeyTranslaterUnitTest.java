package de.benjaminborbe.vnc.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.glavsoft.utils.Keymap;

import de.benjaminborbe.vnc.api.VncKey;

public class VncKeyTranslaterUnitTest {

	@Test
	public void testTranslate() throws Exception {
		final VncKeyTranslater vncKeyTranslater = new VncKeyTranslater();
		// upper
		assertEquals(Keymap.unicode2keysym('A'), vncKeyTranslater.translate(VncKey.A));
		assertEquals(Keymap.unicode2keysym('B'), vncKeyTranslater.translate(VncKey.B));
		assertEquals(Keymap.unicode2keysym('C'), vncKeyTranslater.translate(VncKey.C));
		assertEquals(Keymap.unicode2keysym('D'), vncKeyTranslater.translate(VncKey.D));
		assertEquals(Keymap.unicode2keysym('E'), vncKeyTranslater.translate(VncKey.E));
		assertEquals(Keymap.unicode2keysym('F'), vncKeyTranslater.translate(VncKey.F));
		assertEquals(Keymap.unicode2keysym('G'), vncKeyTranslater.translate(VncKey.G));
		assertEquals(Keymap.unicode2keysym('H'), vncKeyTranslater.translate(VncKey.H));
		assertEquals(Keymap.unicode2keysym('I'), vncKeyTranslater.translate(VncKey.I));
		assertEquals(Keymap.unicode2keysym('J'), vncKeyTranslater.translate(VncKey.J));
		assertEquals(Keymap.unicode2keysym('K'), vncKeyTranslater.translate(VncKey.K));
		assertEquals(Keymap.unicode2keysym('L'), vncKeyTranslater.translate(VncKey.L));
		assertEquals(Keymap.unicode2keysym('M'), vncKeyTranslater.translate(VncKey.M));
		assertEquals(Keymap.unicode2keysym('N'), vncKeyTranslater.translate(VncKey.N));
		assertEquals(Keymap.unicode2keysym('O'), vncKeyTranslater.translate(VncKey.O));
		assertEquals(Keymap.unicode2keysym('P'), vncKeyTranslater.translate(VncKey.P));
		assertEquals(Keymap.unicode2keysym('Q'), vncKeyTranslater.translate(VncKey.Q));
		assertEquals(Keymap.unicode2keysym('R'), vncKeyTranslater.translate(VncKey.R));
		assertEquals(Keymap.unicode2keysym('S'), vncKeyTranslater.translate(VncKey.S));
		assertEquals(Keymap.unicode2keysym('T'), vncKeyTranslater.translate(VncKey.T));
		assertEquals(Keymap.unicode2keysym('U'), vncKeyTranslater.translate(VncKey.U));
		assertEquals(Keymap.unicode2keysym('V'), vncKeyTranslater.translate(VncKey.V));
		assertEquals(Keymap.unicode2keysym('W'), vncKeyTranslater.translate(VncKey.W));
		assertEquals(Keymap.unicode2keysym('X'), vncKeyTranslater.translate(VncKey.X));
		assertEquals(Keymap.unicode2keysym('Y'), vncKeyTranslater.translate(VncKey.Y));
		assertEquals(Keymap.unicode2keysym('Z'), vncKeyTranslater.translate(VncKey.Z));
		// lower
		assertEquals(Keymap.unicode2keysym('a'), vncKeyTranslater.translate(VncKey.a));
		assertEquals(Keymap.unicode2keysym('b'), vncKeyTranslater.translate(VncKey.b));
		assertEquals(Keymap.unicode2keysym('c'), vncKeyTranslater.translate(VncKey.c));
		assertEquals(Keymap.unicode2keysym('d'), vncKeyTranslater.translate(VncKey.d));
		assertEquals(Keymap.unicode2keysym('e'), vncKeyTranslater.translate(VncKey.e));
		assertEquals(Keymap.unicode2keysym('f'), vncKeyTranslater.translate(VncKey.f));
		assertEquals(Keymap.unicode2keysym('g'), vncKeyTranslater.translate(VncKey.g));
		assertEquals(Keymap.unicode2keysym('h'), vncKeyTranslater.translate(VncKey.h));
		assertEquals(Keymap.unicode2keysym('i'), vncKeyTranslater.translate(VncKey.i));
		assertEquals(Keymap.unicode2keysym('j'), vncKeyTranslater.translate(VncKey.j));
		assertEquals(Keymap.unicode2keysym('k'), vncKeyTranslater.translate(VncKey.k));
		assertEquals(Keymap.unicode2keysym('l'), vncKeyTranslater.translate(VncKey.l));
		assertEquals(Keymap.unicode2keysym('m'), vncKeyTranslater.translate(VncKey.m));
		assertEquals(Keymap.unicode2keysym('n'), vncKeyTranslater.translate(VncKey.n));
		assertEquals(Keymap.unicode2keysym('o'), vncKeyTranslater.translate(VncKey.o));
		assertEquals(Keymap.unicode2keysym('p'), vncKeyTranslater.translate(VncKey.p));
		assertEquals(Keymap.unicode2keysym('q'), vncKeyTranslater.translate(VncKey.q));
		assertEquals(Keymap.unicode2keysym('r'), vncKeyTranslater.translate(VncKey.r));
		assertEquals(Keymap.unicode2keysym('s'), vncKeyTranslater.translate(VncKey.s));
		assertEquals(Keymap.unicode2keysym('t'), vncKeyTranslater.translate(VncKey.t));
		assertEquals(Keymap.unicode2keysym('u'), vncKeyTranslater.translate(VncKey.u));
		assertEquals(Keymap.unicode2keysym('v'), vncKeyTranslater.translate(VncKey.v));
		assertEquals(Keymap.unicode2keysym('w'), vncKeyTranslater.translate(VncKey.w));
		assertEquals(Keymap.unicode2keysym('x'), vncKeyTranslater.translate(VncKey.x));
		assertEquals(Keymap.unicode2keysym('y'), vncKeyTranslater.translate(VncKey.y));
		assertEquals(Keymap.unicode2keysym('z'), vncKeyTranslater.translate(VncKey.z));
		// numbers
		assertEquals(Keymap.K_KP_0, vncKeyTranslater.translate(VncKey.K_0));
		assertEquals(Keymap.K_KP_1, vncKeyTranslater.translate(VncKey.K_1));
		assertEquals(Keymap.K_KP_2, vncKeyTranslater.translate(VncKey.K_2));
		assertEquals(Keymap.K_KP_3, vncKeyTranslater.translate(VncKey.K_3));
		assertEquals(Keymap.K_KP_4, vncKeyTranslater.translate(VncKey.K_4));
		assertEquals(Keymap.K_KP_5, vncKeyTranslater.translate(VncKey.K_5));
		assertEquals(Keymap.K_KP_6, vncKeyTranslater.translate(VncKey.K_6));
		assertEquals(Keymap.K_KP_7, vncKeyTranslater.translate(VncKey.K_7));
		assertEquals(Keymap.K_KP_8, vncKeyTranslater.translate(VncKey.K_8));
		assertEquals(Keymap.K_KP_9, vncKeyTranslater.translate(VncKey.K_9));
		// special
		assertEquals(Keymap.K_ENTER, vncKeyTranslater.translate(VncKey.K_ENTER));
	}

	@Test
	public void testComplete() throws Exception {
		final VncKeyTranslater vncKeyTranslater = new VncKeyTranslater();
		for (final VncKey vncKey : VncKey.values()) {
			assertTrue(vncKeyTranslater.translate(vncKey) > 0);
		}
	}
}
