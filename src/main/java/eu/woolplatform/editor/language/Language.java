package eu.woolplatform.editor.language;

/**
 * A {@link Language} defines a language as "supported" by WOOL given its
 * {@code name} (in English), {@code codeISO3} (ISO639-3 code) an optional
 * {@code autonym} (language name in the given language) and an optional
 * {@code codeISO1} (the ISO639-1 code, optionally including a region indication,
 * e.g. en-US).
 * @author Harm op den Akker
 */
public class Language implements Comparable<Language> {

	private String codeISO3;
	private String name;
	private String codeISO1;
	private String autonym;

	/**
	 * Creates an empty instance of a {@link Language}.
	 */
	public Language() { }

	public Language(String codeISO3, String name, String codeISO1, String autonym) {
		this.codeISO3 = codeISO3;
		this.name = name;
		if(!"".equals(codeISO1)) this.codeISO1 = codeISO1;
		if(!"".equals(autonym)) this.autonym = autonym;
	}

	// ----- Getters

	public String getCodeISO3() {
		return codeISO3;
	}

	public String getName() {
		return name;
	}

	public String getCodeISO1() {
		return codeISO1;
	}

	public String getAutonym() {
		return autonym;
	}

	// ----- Setters

	public void setCodeISO3(String codeISO3) {
		this.codeISO3 = codeISO3;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCodeISO1(String codeISO1) {
		this.codeISO1 = codeISO1;
	}

	public void setAutonym(String autonym) {
		this.autonym = autonym;
	}

	public String toString() {
		if(codeISO1 == null) {
			return name + " ["+codeISO3+"]";
		} else {
			return name + " ["+codeISO1+"]";
		}
	}

	public String toAutonymString() {
		String languageName = null;
		if(this.autonym == null) {
			languageName = name;
		} else {
			languageName = autonym;
		}

		if(codeISO1 == null) {
			return languageName + " ["+codeISO3+"]";
		} else {
			return languageName + " ["+codeISO1+"]";
		}
	}

	@Override
	public int compareTo(Language other) {
		return this.getName().compareTo(other.getName());
	}
}

