package eu.woolplatform.editor.script.model;

public abstract class NodePointer {

	private String originNode;
	private String targetNode;

	public NodePointer(String originNode, String targetNode) {
		this.originNode = originNode;
		this.targetNode = targetNode;
	}

	// ----- Getters:

	public String getOriginNode() {
		return originNode;
	}

	public String getTargetNode() {
		return targetNode;
	}

	// ----- Setters:

	public void setOriginNode(String originNode) {
		this.originNode = originNode;
	}

	public void setTargetNode(String targetNode) {
		this.targetNode = targetNode;
	}
}
