package eu.woolplatform.editor.language;

import javax.swing.*;
import java.awt.*;

public class LanguageCellRenderer extends DefaultListCellRenderer {

	private boolean useAutonyms;

	public LanguageCellRenderer(boolean useAutonyms) {
		this.useAutonyms = useAutonyms;
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		String valueRepresentation = null;
		if (value instanceof Language) {
			Language language = (Language)value;
			if(useAutonyms) {
				if(language.getAutonym() != null) {
					valueRepresentation = language.getAutonym();
				} else {
					valueRepresentation = language.getName();
				}
			} else {
				valueRepresentation = ((Language)value).getName();
			}

			String codeRepresentation = "";
			if(language.getCodeISO1() != null) {
				codeRepresentation = language.getCodeISO1();
			} else if(language.getCodeISO3() != null) {
				codeRepresentation = language.getCodeISO3();
			}

			if(!codeRepresentation.equals("")) {
				valueRepresentation += " ["+codeRepresentation+"]";
			}
		}
		super.getListCellRendererComponent(list, valueRepresentation, index, isSelected, cellHasFocus);
		return this;
	}

	public void setUseAutonyms(boolean useAutonyms) {
		this.useAutonyms = useAutonyms;
	}

}
