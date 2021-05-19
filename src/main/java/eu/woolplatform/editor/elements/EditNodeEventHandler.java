package eu.woolplatform.editor.elements;

import eu.woolplatform.editor.windows.editwoolnode.EditWoolNodeWindow;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;

import java.awt.event.MouseEvent;

public class EditNodeEventHandler extends PBasicInputEventHandler {

	private ScriptEditorPanel scriptEditorPanel;
	private WoolNodePanel woolNodePanel;

	public EditNodeEventHandler(ScriptEditorPanel scriptEditorPanel, WoolNodePanel woolNodePanel) {
		this.scriptEditorPanel = scriptEditorPanel;
		this.woolNodePanel = woolNodePanel;
	}

	public void mousePressed(PInputEvent e) {
		super.mousePressed(e);
		if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
			EditWoolNodeWindow editWoolNodeWindow = new EditWoolNodeWindow(scriptEditorPanel,woolNodePanel);
		}
	}

	public void mouseReleased(PInputEvent e) {
		super.mouseReleased(e);
	}
}
