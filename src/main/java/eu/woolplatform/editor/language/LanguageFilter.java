package eu.woolplatform.editor.language;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import eu.woolplatform.editor.WoolEditorUITools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "LanguageFilter")
public class LanguageFilter {

	@JacksonXmlProperty(localName = "Languages")
	private List<Language> languages = new ArrayList<>();

	// ----- Constructors

	public LanguageFilter(List<Language> languages) {
		this.languages = languages;
	}

	public LanguageFilter() { }

	// ----- Getters

	public List<Language> getLanguages() {
		return languages;
	}

	// ----- Setters

	public void setLanguages(List<Language> languages) {
		this.languages = languages;
	}

	// ----- Methods

	/**
	 * Returns true if a Language with given codeISO3 and codeISO1 should be kept in the list
	 * according to this {@link LanguageFilter}.
	 * @param codeISO3
	 * @param codeISO1
	 * @return
	 */
	public boolean keep(String codeISO3, String codeISO1) {
		for(Language l : languages) {
			if(codeISO3.equals(l.getCodeISO3())) {
				if(l.getCodeISO1() == null) return true;
				else if(l.getCodeISO1().equals(codeISO1)) return true;
			}
		}
		return false;
	}

	public static LanguageFilter readFromResource(String fileName) throws IOException {
		XmlMapper xmlMapper = new XmlMapper();

		// read file and put contents into the string
		String readContent = WoolEditorUITools.getResourceFileAsString(fileName);

		// deserialize from the XML into a LanguageSet object
		return xmlMapper.readValue(readContent, LanguageFilter.class);
	}

}
