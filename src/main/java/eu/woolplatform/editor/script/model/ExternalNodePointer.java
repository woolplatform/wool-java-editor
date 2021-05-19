package eu.woolplatform.editor.script.model;

public class ExternalNodePointer extends NodePointer {

	private String targetScript;

	// ----- Constructors:

	public ExternalNodePointer(String originNode, String targetNode, String targetScript) {
		super(originNode, targetNode);
	}

	// ----- Getters:

	public String getTargetScript() {
		return targetScript;
	}

	// ----- Setters:

	public void setTargetScript(String targetScript) {
		this.targetScript = targetScript;
	}

}
