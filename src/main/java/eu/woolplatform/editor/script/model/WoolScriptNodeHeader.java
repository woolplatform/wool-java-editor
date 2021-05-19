/*
 * Copyright 2019-2021 WOOL Foundation.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package eu.woolplatform.editor.script.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class WoolScriptNodeHeader {

	private String title;
	private String speaker;
	private Map<String,String> optionalTags;
	private int x;
	private int y;

	// ---------- Constructors:

	public WoolScriptNodeHeader() {
		optionalTags = new LinkedHashMap<>();
	}

	public WoolScriptNodeHeader(String title) {
		this.title = title;
		optionalTags = new LinkedHashMap<>();
	}

	public WoolScriptNodeHeader(String title, Map<String,String> optionalTags) {
		this.title = title;
		this.optionalTags = optionalTags;
	}

	public WoolScriptNodeHeader(WoolScriptNodeHeader other) {
		this.title = other.title;
		this.speaker = other.speaker;
		this.optionalTags = new LinkedHashMap<>(other.optionalTags);
	}

	// ---------- Getters:

	public String getTitle() {
		return title;
	}

	public String getSpeaker() {
		return this.speaker;
	}

	public Map<String,String> getOptionalTags() {
		return optionalTags;
	}

	/**
	 * Returns the value of the optional tag with the given {@code tagName} or {@code null} if no
	 * such tag exists.
	 * @param tagName
	 * @return
	 */
	public String getOptionalTag(String tagName) {
		return optionalTags.get(tagName);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	// ---------- Setters:

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

	public void setOptionalTags(Map<String,String> optionalTags) {
		this.optionalTags = optionalTags;
	}

	public void setPosition(int x, int y) {
		optionalTags.put("position",x+","+y);
		this.x = x;
		this.y = y;
	}

	// ---------- Utility:

	public void addOptionalTag(String key, String value) {
		optionalTags.put(key,value);
	}

	/**
	 * Validates the optional "position" tag information attached to this {@link WoolScriptNode}.
	 *
	 * @return true if a valid position is set for this node, false otherwise.
	 */
	public void validatePosition() {
		String positionValue = optionalTags.get("position");
		if(positionValue == null) {
			setPosition(0,0);
		} else {
			String[] positions = positionValue.split(",");
			if(positions.length == 2) {
				int posX = 0, posY = 0;
				try {
					posX = Integer.parseInt(positions[0]);
					posY = Integer.parseInt(positions[1]);
				} catch(NumberFormatException e) {
					System.out.println("Error formatting position tag ("+positionValue+") for node, position set to 0,0.");
				}
				setPosition(posX,posY);
			}
		}
	}

	public String toString() {
		String newline = System.getProperty("line.separator");
		StringBuilder result = new StringBuilder();
		result.append("title: " + title);
		if (speaker != null)
			result.append(newline + "speaker: " + speaker);
		for (String key : optionalTags.keySet()) {
			String value = optionalTags.get(key);
			result.append(newline + key + ": " + value);
		}
		return result.toString();
	}

	public void writeHeader(FileWriter fileWriter) throws IOException {
		fileWriter.write("title: "+title+System.getProperty("line.separator"));
		fileWriter.write("speaker: "+speaker+System.getProperty("line.separator"));
		for(String key : optionalTags.keySet()) {
			String value = optionalTags.get(key);
			fileWriter.write(key+": "+value+System.getProperty("line.separator"));
		}
	}

}
