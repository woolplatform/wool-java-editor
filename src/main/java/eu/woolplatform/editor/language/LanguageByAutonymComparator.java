package eu.woolplatform.editor.language;

import java.util.Comparator;

public class LanguageByAutonymComparator implements Comparator<Language> {

	@Override
	public int compare(Language o1, Language o2) {
		String value1 = null;
		if(o1.getAutonym() != null) value1 = o1.getAutonym();
		else value1 = o1.getName();

		String value2 = null;
		if(o2.getAutonym() != null) value2 = o2.getAutonym();
		else value2 = o2.getName();

		return CharSequence.compare(value1,value2);
	}
}
