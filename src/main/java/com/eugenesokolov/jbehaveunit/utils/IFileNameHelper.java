package com.eugenesokolov.jbehaveunit.utils;

public interface IFileNameHelper {

	/**
	 * Convert GroovyFilename.groovy -> Groovy_Filename 
	 */
	public String toStoryName(String groovyFileName);

	/**
	 * Convert Story_FileName.story -> StoryFilename
	 */
	public String toGroovyName(String storyFileName);

	public boolean isGroovyFile(String fileName);

	public boolean isStoryFile(String fileName);

}