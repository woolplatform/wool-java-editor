package eu.woolplatform.editor.elements;

import javax.swing.*;

public class TagButton extends JButton {

	private String tagName;
	private String tagValue;
	private boolean reserved;

	public TagButton() {
		super();
	}

	public TagButton(String tagName, String tagValue, boolean reserved) {
		super(tagName + " : " + tagValue);
		this.tagName = tagName;
		this.tagValue = tagValue;
		this.reserved = reserved;
	}

	// ----- Getters:

	public String getTagName() {
		return tagName;
	}

	public String getTagValue() {
		return tagValue;
	}


	// ----- Setters:

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
}
