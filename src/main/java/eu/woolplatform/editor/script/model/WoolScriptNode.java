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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link WoolScriptNode} represents a single step in a {@link WoolScript} definition.
 *
 * @author Harm op den Akker (Roessingh Research and Development)
 * @author Harm op den Akker (Innovation Sprint)
 */
public class WoolScriptNode {

	public static final String IS_MODIFIED = "isModified";

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	private boolean isModified = false;
	private WoolScriptNodeHeader header;
	private WoolScriptNodeBody body;
	private List<NodePointer> referencedNodes;

	// ---------- Constructors:

	/**
	 * Creates an instance of an empty {@link WoolScriptNode}.
	 */
	public WoolScriptNode() {
		referencedNodes = new ArrayList<NodePointer>();
	}

	/**
	 * Creates an instance of a {@link WoolScriptNode} with the given {@code header}.
	 *
	 * @param header the {@link WoolScriptNodeHeader} for this {@link WoolScriptNode}
	 */
	public WoolScriptNode(WoolScriptNodeHeader header) {
		this.header = header;
		this.referencedNodes = new ArrayList<NodePointer>();
	}

	/**
	 * Creates an instance of a {@link WoolScriptNode} with the given {@code header} and {@code body}.
	 *
	 * @param header the {@link WoolScriptNodeHeader} for this {@link WoolScriptNode}
	 * @param body the {@link WoolScriptNodeBody} for this {@link WoolScriptNode}
	 */
	public WoolScriptNode(WoolScriptNodeHeader header, WoolScriptNodeBody body) {
		this.header = header;
		this.body = body;
		this.referencedNodes = new ArrayList<NodePointer>();
	}

	/**
	 * Creates an instance of a {@link WoolScriptNode} instantiated with the contents from the given {@code other}
	 * {@link WoolScriptNode}.
	 *
	 * @param other the {@link WoolScriptNode} from which to copy its contents into this {@link WoolScriptNode}
	 */
	public WoolScriptNode(WoolScriptNode other) {
		header = new WoolScriptNodeHeader(other.header);
		body = new WoolScriptNodeBody(other.body);
		referencedNodes = other.getReferencedNodes();
	}

	// ---------- Retrieval:

	/**
	 * Returns the {@link WoolScriptNodeHeader} of this {@link WoolScriptNode}.
	 *
	 * @return the {@link WoolScriptNodeHeader} of this {@link WoolScriptNode}.
	 */
	public WoolScriptNodeHeader getHeader() {
		return header;
	}

	/**
	 * Returns the {@link WoolScriptNodeBody} of this {@link WoolScriptNode}.
	 *
	 * @return the {@link WoolScriptNodeBody} of this {@link WoolScriptNode}.
	 */
	public WoolScriptNodeBody getBody() {
		return body;
	}

	public List<NodePointer> getReferencedNodes() {
		return referencedNodes;
	}

	public boolean isModified() {
		return isModified;
	}

	// ---------- Updating;

	/**
	 * Sets the {@link WoolScriptNodeHeader} for this {@link WoolScriptNode}.
	 *
	 * @param header the {@link WoolScriptNodeHeader} for this {@link WoolScriptNode}.
	 */
	public void setHeader(WoolScriptNodeHeader header) {
		this.header = header;
	}

	/**
	 * Sets the {@link WoolScriptNodeBody} for this {@link WoolScriptNode}.
	 *
	 * @param body the {@link WoolScriptNodeBody} for this {@link WoolScriptNode}.
	 */
	public void setBody(WoolScriptNodeBody body) {
		this.body = body;
	}

	public void setReferencedNodes(List<NodePointer> referencedNodes) {
		this.referencedNodes = referencedNodes;
	}

	public void setModified(boolean isModified) {
		boolean oldValue = this.isModified;
		this.isModified = isModified;
		this.pcs.firePropertyChange(IS_MODIFIED,oldValue,isModified);
	}

	// ----- Functions:

	public void addReferencedNode(NodePointer nodePointer) {
		referencedNodes.add(nodePointer);
	}

	/**
	 * Returns the title of this {@link WoolScriptNode} as defined in its
	 * corresponding {@link WoolScriptNodeHeader}. Returns the same as {@code
	 * this.getHeader().getTitle()} or {@code null} if no {@link WoolScriptNodeHeader}
	 * has been set, or its title attribute is {@code null}.
	 *
	 * @return the title of this {@link WoolScriptNode} as defined in its
	 * corresponding {@link WoolScriptNodeHeader}.
	 */
	public String getTitle() {
		if(header != null)
			return header.getTitle();
		else return null;
	}

	/**
	 * Performs a shallow parsing over the {@code script} String of this {@link WoolScriptNodeBody}.
	 */
	public void shallowParse() {
		String script = body.getScript();
		List<String> replyOptions = new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\[\\[.*?\\]\\]");
		Matcher matcher = pattern.matcher(script);
		while(matcher.find()) {
			String replyOption = script.substring(matcher.start(),matcher.end());
			replyOptions.add(replyOption);
		}

		for(String replyOption : replyOptions) {
			String replyOptionContent = replyOption.substring(2,replyOption.length()-2);
			String[] replyTokens = replyOptionContent.split("\\|");
			String referenceString = "";
			if(replyTokens.length == 1) {
				referenceString = replyTokens[0];
			} else if(replyTokens.length > 1) {
				referenceString = replyTokens[1];
			}

			if(referenceString.contains(".")) {
				String[] referenceTokens = referenceString.split("\\.");
				ExternalNodePointer pointer = new ExternalNodePointer(getTitle(),referenceTokens[1],referenceTokens[0]);
				addReferencedNode(pointer);
			} else {
				InternalNodePointer pointer = new InternalNodePointer(getTitle(),referenceString);
				addReferencedNode(pointer);
			}
		}
	}

	@Override
	public String toString() {
		String newline = System.getProperty("line.separator");
		String result = "";
		result += header.toString();
		result += newline + "---" + newline;
		result += body.toString();
		result += "===" + newline;
		return result;
	}

	public void writeNode(FileWriter fileWriter) throws IOException {
		header.writeHeader(fileWriter);
		fileWriter.write("---"+System.getProperty("line.separator"));
		body.writeBody(fileWriter);
		fileWriter.write("==="+System.getProperty("line.separator"));
	}
}
