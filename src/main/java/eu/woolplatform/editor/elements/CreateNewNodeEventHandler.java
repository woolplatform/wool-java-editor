package eu.woolplatform.editor.elements;

import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;


public class CreateNewNodeEventHandler extends PBasicInputEventHandler {

	private ScriptEditorPanel scriptEditorPanel;

	public CreateNewNodeEventHandler(ScriptEditorPanel scriptEditorPanel) {
		this.scriptEditorPanel = scriptEditorPanel;
	}

	public void mousePressed(PInputEvent e) {
		super.mousePressed(e);
		scriptEditorPanel.createNewWoolNode(e.getPosition());
	}

	public void mouseReleased(PInputEvent e) {
		super.mouseReleased(e);
	}

}