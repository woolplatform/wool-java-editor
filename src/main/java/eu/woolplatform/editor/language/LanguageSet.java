package eu.woolplatform.editor.language;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LanguageSet {

	public static final String SIMPLIFIED = "SIMPLIFIED";
	public static final String EXTENDED = "EXTENDED";
	public static final String CUSTOM = "CUSTOM";

	private List<Language> languages;

	public LanguageSet() {
		languages = new ArrayList<Language>();
	}

	/**
	 * Returns the list of {@link Language}s that make up this {@link LanguageSet}.
	 * @return the list of {@link Language}s that make up this {@link LanguageSet}.
	 */
	public List<Language> getLanguages() {
		return languages;
	}

	/**
	 * Adds the given {@code language} to this {@link LanguageSet}.
	 * @param language the {@link Language} to add to this {@link LanguageSet}.
	 */
	public void addLanguage(Language language) {
		this.languages.add(language);
	}

	public Language[] getSortedArray(boolean useAutonyms) {
		Language[] result = new Language[languages.size()];
		if(useAutonyms) {
			Collections.sort(languages,new LanguageByAutonymComparator());
		} else {
			Collections.sort(languages,new LanguageByNameComparator());
		}
		result = languages.toArray(result);
		return result;
	}

	/**
	 * Returns a {@link LanguageSet} as filtered by the given {@link LanguageFilter}.
	 * @param languageFilter the set of languages to filter this {@link LanguageSet} on.
	 * @return a {@link LanguageSet} as filtered by the given {@link LanguageFilter}.
	 */
	public LanguageSet getFilteredLanguageSet(LanguageFilter languageFilter) {
		LanguageSet filteredSet = new LanguageSet();
		for(Language language : languages) {
			if(languageFilter.keep(language.getCodeISO3(),language.getCodeISO1())) {
				filteredSet.addLanguage(language);
			}
		}
		return filteredSet;
	}

	public static LanguageSet readFromTSV(String fileName) throws IOException {
		LanguageSet languageSet = new LanguageSet();
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		try (InputStream is = classLoader.getResourceAsStream(fileName)) {
			if (is == null) return null;
			try (InputStreamReader isr = new InputStreamReader(is);
				 BufferedReader reader = new BufferedReader(isr)) {
				if(reader.readLine() != null) {
					String line;
					while ((line = reader.readLine()) != null) {
						String[] elements = line.split("\\t");
						languageSet.addLanguage(new Language(elements[0],elements[2],elements[1],elements[3]));
					}
				} else {
					throw new IOException("Language resource file is empty.");
				}
			}
		}
		return languageSet;
	}

	/**
	 * Returns a {@link Language} from this {@link LanguageSet} based on the given {@code code},
	 * which could be an iso3 or iso1 or iso1+region code. Returns {@code null} if no match is found.
	 * @param code
	 * @return
	 */
	public Language findMatch(String code) {
		if(code == null) return null;
		else if(code.length() == 3) {
			List<Language> candidates = new ArrayList<Language>();
			for(Language l : languages) {
				if(l.getCodeISO3().equals(code)) candidates.add(l);
			}
			if(candidates.size() == 0) return null;
			else {
				// In case of multiple matches, prefer a language without any ISO1 code
				for(Language lc : candidates) {
					if(lc.getCodeISO1() == null) {
						return lc;
					}
				}

				// Secondary, prefer a shorter ISO1 code
				for(Language lc : candidates) {
					if(lc.getCodeISO1().length() == 2) {
						return lc;
					}
				}

				// If still no candidate selected, simply return the first one in the list
				return candidates.get(0);
			}
		} else if(code.length() == 2 || ((code.length() == 5) && (code.charAt(2) == '-'))) {
			for(Language l : languages) {
				if(l.getCodeISO1() != null)
					if(l.getCodeISO1().equals(code)) return l;
			}
		}

		return null;
	}
}
