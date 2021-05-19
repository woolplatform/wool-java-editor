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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Object representation of a WOOL script file. A {@link WoolScript} has a name and an
 * (unordered) list of {@link WoolScriptNode}s. One of these {@link WoolScriptNode}s should have as title "Start".
 *
 * @author Harm op den Akker (Roessingh Research and Development)
 * @author Harm op den Akker (Innovation Sprint)
 */
public class WoolScript implements PropertyChangeListener {
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	public static final String DIALOGUE_NAME = "dialogueName";
	public static final String WOOL_SCRIPT_FILE = "woolScriptFile";
	public static final String IS_MODIFIED = "isModified";
	public static final String NODES = "nodes";

	private String dialogueName;
	private File woolScriptFile;
	private boolean isModified = false;
	private final Map<String,WoolScriptNode> nodes = new LinkedHashMap<>(); // map from lower-case node titles to nodes
	private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();

	// ----- Constructors:

	/**
	 * Creates an empty instance of a {@link WoolScript}.
	 */
	public WoolScript() { }

	/**
	 * Creates an instance of a {@link WoolScript} with a given {@code dialogueName}.
	 *
	 * @param dialogueName the name of this {@link WoolScript}.
	 */
	public WoolScript(String dialogueName) {
		this.dialogueName = dialogueName;
	}

	/**
	 * Creates an instance of a {@link WoolScript}, instantiated with the contents of the given {@code other}
	 * {@link WoolScript}.
	 *
	 * @param other the {@link WoolScript} with which to instantiate this {@link WoolScript}
	 */
	public WoolScript(WoolScript other) {
		dialogueName = other.dialogueName;
		for (String key : other.nodes.keySet()) {
			WoolScriptNode newNode = new WoolScriptNode(other.nodes.get(key));
			newNode.addPropertyChangeListener(this);
			nodes.put(key, newNode);
		}
	}

	// ----- Retrieving:

	/**
	 * Returns the original File location of the corresponding {@link WoolScript}.
	 * @return the original File location of the corresponding {@link WoolScript}.
	 */
	public File getWoolScriptFile() {
		return woolScriptFile;
	}

	/**
	 * Returns the name of this {@link WoolScript}.
	 * @return the name of this {@link WoolScript}.
	 */
	public String getDialogueName() {
		return this.dialogueName;
	}

	/**
	 * Returns the nodes as an unmodifiable list.
	 * @return the nodes as an unmodifiable list
	 */
	public List<WoolScriptNode> getNodes() {
		return List.copyOf(nodes.values());
	}

	/**
	 * Returns true if this {@link WoolScript} has unsaved modifications, false otherwise.
	 * @return true if this {@link WoolScript} has unsaved modifications, false otherwise.
	 */
	public boolean isModified() {
		return isModified;
	}

	/**
	 * Returns the starting {@link WoolScriptNode} for this {@link WoolScript}.
	 * @return the starting {@link WoolScriptNode} for this {@link WoolScript}.
	 */
	public WoolScriptNode getStartNode() {
		return nodes.get("start");
	}

	public boolean nodeExists(String nodeId) {
		return nodes.containsKey(nodeId.toLowerCase());
	}

	/**
	 * Returns the {@link WoolScriptNode} with the given identifier or title.
	 * @param nodeId the node ID
	 * @return the node
	 */
	public WoolScriptNode getNodeById(String nodeId) {
		return nodes.get(nodeId.toLowerCase());
	}

	/**
	 * Returns the total number of nodes in this {@link WoolScript}.
	 *
	 * @return the total number of nodes in this {@link WoolScript}.
	 */
	public int getNodeCount() {
		return nodes.size();
	}

	// ----- Updating:

	/**
	 * Sets the name of this {@link WoolScript}.
	 * @param dialogueName the name of this {@link WoolScript}.
	 */
	public void setDialogueName(String dialogueName) {
		String oldValue = this.dialogueName;
		this.dialogueName = dialogueName;
		this.pcs.firePropertyChange(DIALOGUE_NAME, oldValue, dialogueName);
	}

	public void setWoolScriptFile(File woolScriptFile) {
		File oldValue = this.woolScriptFile;
		this.woolScriptFile = woolScriptFile;
		this.pcs.firePropertyChange(WOOL_SCRIPT_FILE,oldValue,woolScriptFile);
	}

	public void setModified(boolean isModified) {
		boolean oldValue = this.isModified;
		this.isModified = isModified;
		this.pcs.firePropertyChange(IS_MODIFIED,oldValue,isModified);
	}

	public void addNode(WoolScriptNode node) {
		List<WoolScriptNode> oldValue = getNodes();
		nodes.put(node.getTitle().toLowerCase(), node);
		node.addPropertyChangeListener(this);
		this.pcs.firePropertyChange(NODES,oldValue,getNodes());
	}

	// ----- Functions:

	/**
	 * Returns a human readable multi-line summary string, representing the contents of this {@link WoolScript}.
	 */
	public String toString() {
		String summaryString = "";

		summaryString += "Dialogue Name: "+getDialogueName()+"\n";
		summaryString += "File location: "+woolScriptFile.getAbsolutePath()+"\n";
		summaryString += "Number of Nodes: "+getNodeCount()+"\n";

		for (String key : nodes.keySet()) {
			WoolScriptNode node = nodes.get(key);
			summaryString += node.toString();
		}

		return summaryString;
	}

	private void checkModified() {
		boolean hasModifiedNodes = false;
		for(WoolScriptNode node : getNodes()) {
			if(node.isModified()) {
				hasModifiedNodes = true;
				break;
			}
		}
		setModified(hasModifiedNodes);
	}

	public void saveToFile() throws IOException {
		FileWriter fileWriter = new FileWriter(woolScriptFile);
		for(WoolScriptNode node : getNodes()) {
			node.writeNode(fileWriter);
			node.setModified(false);
		}
		fileWriter.close();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(WoolScriptNode.IS_MODIFIED)) {
			this.checkModified();
		}
	}
}
