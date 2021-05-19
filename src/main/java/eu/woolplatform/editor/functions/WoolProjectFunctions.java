package eu.woolplatform.editor.functions;

import eu.woolplatform.editor.language.Language;
import eu.woolplatform.editor.script.model.WoolScript;
import eu.woolplatform.editor.script.parser.WoolScriptParser;
import eu.woolplatform.editor.script.parser.WoolScriptParserResult;
import eu.woolplatform.utils.exception.ParseException;
import eu.woolplatform.utils.xml.SimpleSAXHandler;
import eu.woolplatform.utils.xml.SimpleSAXParser;
import eu.woolplatform.utils.xml.XMLWriter;
import eu.woolplatform.wool.model.WoolProjectMetaData;
import eu.woolplatform.wool.model.language.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * A collection of static functions to create new WOOL Projects.
 */
public class WoolProjectFunctions {

	private static final String WOOL_PROJECT_METADATA_FILENAME = "wool-project.xml";
	public static final String WOOL_SCRIPT_EXTENSION = ".wool";

	/**
	 * Generates a {@link WoolProjectMetaData} from the given parameters.
	 * @param projectName the descriptive name of the project.
	 * @param projectRoot the root folder in which the project folder should be created.
	 * @param projectFolderName the name of the project folder.
	 * @param projectDescription a textual description of the project.
	 * @param projectVersion a version number for the project.
	 * @param defaultLanguage the default {@link Language} for this project.
	 */
	public static WoolProjectMetaData generateWoolProjectMetaData(String projectName,
												 String projectRoot,
												 String projectFolderName,
												 String projectDescription,
												 String projectVersion,
												 Language defaultLanguage) {
		String projectBasePath;
		if(projectRoot.endsWith(File.separator)) {
			projectBasePath = projectRoot + projectFolderName;
		} else {
			projectBasePath = projectRoot + File.separator + projectFolderName;
		}
		return generateWoolProjectMetaData(projectName,projectBasePath,projectDescription,projectVersion,defaultLanguage);
	}

	/**
	 * Generates a {@link WoolProjectMetaData} from the given parameters.
	 * @param projectName the descriptive name of the project.
	 * @param projectBasePath the base path of the project (root folder + project folder name).
	 * @param projectDescription a textual description of the project.
	 * @param projectVersion a version number for the project.
	 * @param defaultLanguage the default {@link Language} for this project.
	 * @return a {@link WoolProjectMetaData} object, initialized with the given parameters.
	 */
	public static WoolProjectMetaData generateWoolProjectMetaData(String projectName,
											String projectBasePath,
											String projectDescription,
											String projectVersion,
											Language defaultLanguage) {

		// Construct the WoolLanguageMap
		String defaultLanguageName = defaultLanguage.getName();
		String defaultLanguageCode;
		if(defaultLanguage.getCodeISO1() != null) defaultLanguageCode = defaultLanguage.getCodeISO1();
		else defaultLanguageCode = defaultLanguage.getCodeISO3();

		WoolLanguage sourceLanguage = new WoolLanguage(defaultLanguageName,defaultLanguageCode);
		WoolLanguageSet woolLanguageSet = new WoolLanguageSet(sourceLanguage);
		WoolLanguageMap woolLanguageMap = new WoolLanguageMap();
		woolLanguageMap.addLanguageSet(woolLanguageSet);

		// Create and return WoolProjectMetaData instance
		return new WoolProjectMetaData(projectName,
				projectBasePath,
				projectDescription,
				projectVersion,
				woolLanguageMap);
	}

	/**
	 *
	 * TODO: When creating new start.wool scripts, generate them in the correct language (for select cases).
	 * TODO: When creating new translation files, generate correct language translations (for select cases).
	 *
	 * Generates the files for a new wool project and saves them to disk, using the specifications
	 * from the given {@link WoolProjectMetaData}.
	 * @param metaData the meta data for the wool project to write.
	 * @throws IOException in case of a write error.
	 */
	public static void saveNewLocalWoolProject(WoolProjectMetaData metaData) throws IOException {

		// Create the directory at the given location
		Path path = Paths.get(metaData.getBasePath());
		try {
			Files.createDirectory(path);
		} catch (IOException e) {
			System.err.println("Error creating directory at "+metaData.getBasePath());
			throw e;
		}

		XMLWriter xmlWriter = new XMLWriter(new FileWriter(metaData.getBasePath()+File.separator+WOOL_PROJECT_METADATA_FILENAME));
		metaData.writeXML(xmlWriter);

		// Create the language folders inside the new wool project folder
		WoolLanguageMap languageMap = metaData.getWoolLanguageMap();

		// For all the language sets that are defined in this metadata...
		for(WoolLanguageSet languageSet : languageMap.getLanguageSets()) {
			if(languageSet.getSourceLanguage() != null) {

				// If there is a source language defined, create a directory for it...
				String sourceLanguageCode = languageSet.getSourceLanguage().getCode();
				try {
					Files.createDirectory(Paths.get(metaData.getBasePath()+File.separator+sourceLanguageCode));
				} catch (IOException e) {
					System.err.println("Error creating language directory at " + metaData.getBasePath() + File.separator + sourceLanguageCode);
					throw e;
				}

				// And for each source language, create a "default" .wool script...
				try {
					File defaultWoolFile = new File(metaData.getBasePath()+File.separator+sourceLanguageCode+File.separator+"start.wool");
					if (defaultWoolFile.createNewFile()) {
						String newLine = System.getProperty("line.separator");
						System.out.println("File created: " + defaultWoolFile.getName());
						FileWriter myWriter = new FileWriter(defaultWoolFile);
						myWriter.write("title: Start"+newLine);
						myWriter.write("speaker: default"+newLine);
						myWriter.write("position: 0,0"+newLine);
						myWriter.write("colorID: 1"+newLine);
						myWriter.write("---"+newLine);
						myWriter.write("Your first node."+newLine+newLine);
						myWriter.write("[[Great!|Start]]");
						myWriter.close();
						System.out.println("Successfully wrote to the file.");
					} else {
						System.out.println("File already exists.");
					}
				} catch (IOException e) {
					System.out.println("An error occurred.");
					throw e;
				}

				// If there are any translation languages defined...
				if(languageSet.getTranslationLanguages() != null) {
					List<WoolLanguage> translationLanguages = languageSet.getTranslationLanguages();

					// For each defined translation language...
					for(WoolLanguage translationLanguage : translationLanguages) {
						String translationLanguageCode = translationLanguage.getCode();

						// Create a directory based off the language code...
						try {
							Files.createDirectory(Paths.get(metaData.getBasePath()+File.separator+translationLanguageCode));
						} catch (IOException e) {
							System.err.println("Error creating language directory at " + metaData.getBasePath() + File.separator + translationLanguageCode);
							throw e;
						}

						// And for each translation language, create a "default" .json translation file with mock translations...
						try {
							File defaultTranslationFile = new File(metaData.getBasePath()+File.separator+translationLanguageCode+File.separator+"start.json");
							if (defaultTranslationFile.createNewFile()) {
								System.out.println("File created: " + defaultTranslationFile.getName());
								FileWriter myWriter = new FileWriter(defaultTranslationFile);
								myWriter.write("{\n");
								myWriter.write("  \"Your first node.\": \"Your first node. ["+translationLanguageCode+"]\",\n");
								myWriter.write("  \"Great!\": \"Great! ["+translationLanguageCode+"]\"\n");
								myWriter.write("}\n");
								myWriter.close();
								System.out.println("Successfully wrote to the file.");
							} else {
								System.out.println("File already exists.");
							}
						} catch (IOException e) {
							System.out.println("An error occurred.");
							throw e;
						}
					}
				}
			}
		}
	}

	/**
	 * Loads a {@link WoolProjectMetaData} object from the given .xml file.
	 * @param metaDataFile
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 */
	public static WoolProjectMetaData loadLocalWoolProject(File metaDataFile) throws IOException, ParseException {
		SimpleSAXHandler<WoolProjectMetaData> xmlHandler = WoolProjectMetaData.getXMLHandler();
		SimpleSAXParser<WoolProjectMetaData> parser = new SimpleSAXParser<WoolProjectMetaData>(xmlHandler);
		WoolProjectMetaData result = null;
		result = parser.parse(metaDataFile);
		result.setBasePath(metaDataFile.getParent());
		return result;
	}

	/**
	 * Loads a .wool script from file and returns the contents of the file as a {@link WoolScriptParserResult} object,
	 * which contains a shallow parsing of the wool script.
	 * @param file the file from which to load the .wool script.
	 * @return the {@link WoolScriptParserResult} containing the loaded .wool script.
	 */
	public static WoolScriptParserResult loadWoolScript(File file) throws IOException {
		WoolScriptParser parser = new WoolScriptParser(file);
		return parser.readDialogue();
	}
}
