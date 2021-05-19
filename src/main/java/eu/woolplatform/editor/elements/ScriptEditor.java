package eu.woolplatform.editor.elements;

import eu.woolplatform.editor.MainWindow;
import eu.woolplatform.editor.script.model.WoolScript;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ScriptEditor extends JPanel {

	private MainWindow mainWindow;
	private JTabbedPane tabbedPane;
	private SpringLayout layout;

	public ScriptEditor(MainWindow mainWindow) {
		super();

		// The "parent" MainWindow that this ScriptEditor belongs to.
		this.mainWindow = mainWindow;

		layout = new SpringLayout();
		this.setLayout(layout);

		tabbedPane = new JTabbedPane();
		this.add(tabbedPane);
		layout.putConstraint(SpringLayout.NORTH, tabbedPane, 5, SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST, tabbedPane, 5, SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.EAST, tabbedPane, 0, SpringLayout.EAST,this);
		layout.putConstraint(SpringLayout.SOUTH, tabbedPane, 5, SpringLayout.SOUTH,this);
	}

	public void openDialogueScript(WoolScript woolScript) {
		mainWindow.getActiveWoolProject().addActiveWoolScript(woolScript);
		ScriptEditorPanel scriptEditorPanel = new ScriptEditorPanel(woolScript,mainWindow);
		tabbedPane.addTab(woolScript.getDialogueName(), null, scriptEditorPanel, woolScript.getDialogueName());
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount()-1);
		//scriptEditorPanel.centerOnStartNode();
	}

	// ----- Getters:

	public MainWindow getMainWindow() {
		return mainWindow;
	}
}
