package eu.woolplatform.editor;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class WoolEditorSettings {

	public static final String[] RESERVED_TAGS = {"position","colorid"};
	private SettingShowTags showTags;
	private boolean enableScriptWrapping;
	private boolean showReservedTags;

	// ----- Constructors

	public WoolEditorSettings() { }

	// ----- Getters

	public boolean getEnableScriptWrapping() {
		return enableScriptWrapping;
	}

	public SettingShowTags getShowTags() {
		return showTags;
	}

	public boolean getShowReservedTags() {
		return showReservedTags;
	}

	// ----- Setters

	public void setEnableScriptWrapping(boolean enableScriptWrapping) {
		this.enableScriptWrapping = enableScriptWrapping;
	}

	public void setShowTags(SettingShowTags showTags) {
		this.showTags = showTags;
	}

	public void setShowReservedTags(boolean showReservedTags) {
		this.showReservedTags = showReservedTags;
	}

	// ----- XML Functions

	public static WoolEditorSettings readFromResource(String fileName) throws IOException {
		XmlMapper xmlMapper = new XmlMapper();

		String xmlContent;
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		try (InputStream is = classLoader.getResourceAsStream(fileName)) {
			if (is == null) return null;
			try (InputStreamReader isr = new InputStreamReader(is);
				 BufferedReader reader = new BufferedReader(isr)) {
				xmlContent = reader.lines().collect(Collectors.joining(System.lineSeparator()));
			}
		}

		// deserialize from the XML into a LanguageSet object
		return xmlMapper.readValue(xmlContent, WoolEditorSettings.class);
	}

	// ----- Enums

	public enum SettingShowTags {
		ALWAYS,
		NEVER,
		REMEMBER
	}
}


