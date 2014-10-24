package com.eugenesokolov.jbehaveunit.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testSplitByCamelCase() {
		String[] expected = {"Split", "By", "Camel", "Case"};
		assertArrayEquals(expected, StringUtils.splitByCamelCase("SplitByCamelCase"));
	}

	@Test
	public void testCapitalizeWord() {
		String expected = "World";
		assertEquals(expected, StringUtils.capitalizeWord("world"));
	}

	@Test
	public void testJoinWords() {
		String expected = "Some_Joint_Words";
		String [] input = {"Some", "Joint", "Words"};
		assertEquals(expected, StringUtils.joinWords(input,  "_"));
	}
}
