package eu.woolplatform.editor.script.model;

import eu.woolplatform.wool.model.WoolProjectMetaData;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActiveWoolProject implements PropertyChangeListener {

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	public static final String IS_MODIFIED = "isModified";

	private WoolProjectMetaData woolProjectMetaData = null;
	private List<WoolScript> activeWoolScripts = new ArrayList<WoolScript>();
	private boolean isModified = false;

	// ----- Constructors

	public ActiveWoolProject(WoolProjectMetaData woolProjectMetaData) {
		this.woolProjectMetaData = woolProjectMetaData;
	}

	// ----- Retrieval

	public String getProjectName() {
		return woolProjectMetaData.getName();
	}

	public WoolProjectMetaData getWoolProjectMetaData() {
		return woolProjectMetaData;
	}

	public List<WoolScript> getActiveWoolScripts() {
		return activeWoolScripts;
	}

	public boolean isModified() {
		return isModified;
	}

	// ----- Updating

	public void addActiveWoolScript(WoolScript woolScript) {
		activeWoolScripts.add(woolScript);
		woolScript.addPropertyChangeListener(this);
	}

	public void removeActiveWoolScript(WoolScript woolScript) {
		woolScript.removePropertyChangeListener(this);
		activeWoolScripts.remove(woolScript);
	}

	private void setModified(boolean isModified) {
		boolean oldValue = this.isModified;
		this.isModified = isModified;
		this.pcs.firePropertyChange(IS_MODIFIED,oldValue,isModified);
		System.out.println("Active WOOL Project has modification: "+isModified);
	}

	// ----- Functions

	public void checkModified() {
		boolean hasModifiedWoolScripts = false;
		for(WoolScript woolScript : activeWoolScripts) {
			if(woolScript.isModified()) {
				hasModifiedWoolScripts = true;
				break;
			}
		}
		setModified(hasModifiedWoolScripts);
	}

	public void saveAllActiveWoolScripts() throws IOException {
		System.out.println("Saving all loaded .wool scripts with unsaved changes.");
		for(WoolScript woolScript : activeWoolScripts) {
			if(woolScript.isModified()) {
				System.out.println("Saving .wool script '"+woolScript.getDialogueName()+"' to file '"+woolScript.getWoolScriptFile()+"'.");
				woolScript.saveToFile();
			}
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(WoolScript.IS_MODIFIED)) {
			this.checkModified();
		}
	}
}
