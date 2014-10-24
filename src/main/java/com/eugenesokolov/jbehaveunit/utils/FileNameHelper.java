package com.eugenesokolov.jbehaveunit.utils;

public class FileNameHelper implements IFileNameHelper {
	
	public static final String STORY_EXTENSION = ".story";
	public static final String GROOVY_EXTENSION = ".groovy";

	/**
	 * Convert GroovyFilename.groovy -> Groovy_Filename 
	 */
	@Override
	public String toStoryName(String groovyFileName) {
		String name = groovyFileName.substring(0, groovyFileName.length() - GROOVY_EXTENSION.length());
		return StringUtils.joinWords(StringUtils.splitByCamelCase(name), "_");
	}

	/**
	 * Convert Story_FileName.story -> StoryFilename
	 */
	@Override
	public String toGroovyName(String storyFileName) {
		String name = storyFileName.substring(0, storyFileName.length() - STORY_EXTENSION.length());

		String[] words = name.split("_");
		StringBuilder sb = new StringBuilder();
		for (String word : words) {
			sb.append(StringUtils.capitalizeWord(word.toLowerCase()));
		}
		return sb.toString();
	}

	@Override
	public boolean isGroovyFile(String fileName) {
		return fileName.endsWith(GROOVY_EXTENSION);
	}

	@Override
	public boolean isStoryFile(String fileName) {
		return fileName.endsWith(STORY_EXTENSION);
	}
}
