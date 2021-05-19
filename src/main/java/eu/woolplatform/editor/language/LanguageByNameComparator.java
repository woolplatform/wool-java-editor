package eu.woolplatform.editor.language;

import java.util.Comparator;

public class LanguageByNameComparator implements Comparator<Language> {

	@Override
	public int compare(Language o1, Language o2) {
		return CharSequence.compare(o1.getName(),o2.getName());
	}
}
