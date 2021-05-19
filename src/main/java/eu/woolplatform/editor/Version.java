package eu.woolplatform.editor;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Version {

	private int major;
	private int minor;
	private int patch;

	public Version(int major, int minor, int patch) {
		this.major = major;
		this.minor = minor;
		this.patch = patch;
	}

	public Version() { }

	public int getMajor() {
		return major;
	}

	public int getMinor() {
		return minor;
	}

	public int getPatch() {
		return patch;
	}

	public String toString() {
		return ""+major+"."+minor+"."+patch;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public void setPatch(int patch) {
		this.patch = patch;
	}

	public static Version readFromResource(String fileName) throws IOException {
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
		return xmlMapper.readValue(xmlContent, Version.class);
	}

}
