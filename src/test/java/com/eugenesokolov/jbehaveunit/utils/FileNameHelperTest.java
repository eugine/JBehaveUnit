package com.eugenesokolov.jbehaveunit.utils;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FileNameHelperTest {

	private enum FileType {
		GROOVY,
		STORY
	}
	
	@Parameters
	public static Collection<Object[]> generateData() {
			return Arrays.asList(new Object[][] {
				//groovy -> story
				{"GroovyFileName.groovy", FileType.GROOVY, true, "Groovy_File_Name"},
				{"GroovyFilerejectedbyccpName.groovy", FileType.GROOVY, true, "Groovy_Filerejectedbyccp_Name"},
				{"GroovyFileName.GRoovy", FileType.GROOVY, false, null},
				
				//story -> groove
				{"Story_File_Name.story", FileType.STORY, true, "StoryFileName"},
				{"Story_FileRejectedByCCP_Name.story", FileType.STORY, true, "StoryFilerejectedbyccpName"}, 
				{"Story_File_Name.STORY", FileType.STORY, false, null}
			});
	}
	
	private String fileName;
	private FileType type;
	private boolean expectedCorrecTypeFile;
	private String expectedConvertedName;
	private FileNameHelper helper;
	

	public FileNameHelperTest(String fileName, FileType type,
			boolean expectedCorrecTypeFile, String expectedConvertedName) {
		this.fileName = fileName;
		this.type = type;
		this.expectedCorrecTypeFile = expectedCorrecTypeFile;
		this.expectedConvertedName = expectedConvertedName;
		this.helper = new FileNameHelper();
	}

	@Test
	public void test() {	
		if (type == FileType.GROOVY) {
			assertEquals(expectedCorrecTypeFile, helper.isGroovyFile(fileName));
			if (expectedCorrecTypeFile) {
				assertEquals(expectedConvertedName, helper.toStoryName(fileName));
			}
		} else {
			assertEquals(expectedCorrecTypeFile, helper.isStoryFile(fileName));
			if (expectedCorrecTypeFile) {
				assertEquals(expectedConvertedName, helper.toGroovyName(fileName));
			}
		}
	}

}
