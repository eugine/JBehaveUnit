package com.eugenesokolov.jbehaveunit.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	public static String[] splitByCamelCase(String camelCaseString) {
		if (camelCaseString == null) {
			return null;
		}
		if (camelCaseString.isEmpty()) {
			return new String[0];
		}
		final char[] c = camelCaseString.toCharArray();
		final List<String> list = new ArrayList<String>();
		int tokenStart = 0;
		int currentType = Character.getType(c[tokenStart]);
		for (int pos = tokenStart + 1; pos < c.length; pos++) {
			final int type = Character.getType(c[pos]);
			if (type == currentType) {
				continue;
			}
			if (type == Character.LOWERCASE_LETTER && currentType == Character.UPPERCASE_LETTER) {
				final int newTokenStart = pos - 1;
				if (newTokenStart != tokenStart) {
					list.add(new String(c, tokenStart, newTokenStart - tokenStart));
					tokenStart = newTokenStart;
				}
			} 
			currentType = type;
		}
		list.add(new String(c, tokenStart, c.length - tokenStart));
		return list.toArray(new String[list.size()]);
	}
	
	public static String capitalizeWord(String word) {
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}
	
	public static String joinWords(String [] words, String delim) {
		String loopDelim = "";
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(loopDelim);
			sb.append(word);
			loopDelim = delim;
		}
		return sb.toString();
	}
}
