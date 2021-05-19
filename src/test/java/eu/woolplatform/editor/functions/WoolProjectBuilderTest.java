package eu.woolplatform.editor.functions;

import eu.woolplatform.editor.language.Language;
import eu.woolplatform.wool.exception.WoolDuplicateLanguageCodeException;
import eu.woolplatform.wool.exception.WoolUnknownLanguageCodeException;
import eu.woolplatform.wool.model.WoolProjectMetaData;
import eu.woolplatform.wool.model.language.WoolLanguageSet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WoolProjectBuilderTest {

	/**
	 * Test the creation of a new WOOL Project under the location as given as the first
	 * programme argument
	 * @param args 0 - the base path where the new WOOL project should be created.
	 */
	public static void main(String[] args) {

		String projectName = "Generated Test Project";

		String projectFolderPath = args[0];

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
		LocalDateTime now = LocalDateTime.now();
		String projectFolderName = dtf.format(now);

		String projectDescription = "Test description.\n\nWith multiple lines.";
		String projectVersion = "1.0.0";
		Language defaultLanguage = new Language("nld","Dutch","nl","Nederlands");

		WoolProjectMetaData metaData = WoolProjectFunctions.generateWoolProjectMetaData(projectName, projectFolderPath, projectFolderName, projectDescription, projectVersion, defaultLanguage);

		// Add some translation languages for the default "nl" language
		WoolLanguageSet woolLanguageSet = null;
		try {
			woolLanguageSet = metaData.getLanguageSetForSourceLanguage("nl");
		} catch (WoolUnknownLanguageCodeException e) {
			e.printStackTrace();
		}

		try {
			metaData.addTranslationLanguage("English","en",woolLanguageSet);
		} catch (WoolDuplicateLanguageCodeException e) {
			e.printStackTrace();
		}

		try {
			metaData.addTranslationLanguage("German (Austria)","de-at",woolLanguageSet);
		} catch (WoolDuplicateLanguageCodeException e) {
			e.printStackTrace();
		}

		WoolLanguageSet newLanguageSet = null;
		try {
			newLanguageSet = metaData.addSourceLanguage("Portuguese","pt");
		} catch (WoolDuplicateLanguageCodeException e) {
			e.printStackTrace();
		}

		try {
			metaData.addTranslationLanguage("Spanish (Mexico)","es-mx",newLanguageSet);
		} catch (WoolDuplicateLanguageCodeException e) {
			e.printStackTrace();
		}

		try {
			metaData.addTranslationLanguage("Xyzonian Made-up Dialect","xyz",newLanguageSet);
		} catch (WoolDuplicateLanguageCodeException e) {
			e.printStackTrace();
		}

		try {
			WoolProjectFunctions.saveNewLocalWoolProject(metaData);
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
			ioException.printStackTrace();
		}

	}
}
