package eu.woolplatform.editor.elements;

import eu.woolplatform.editor.MainWindow;
import eu.woolplatform.editor.WoolEditorUITools;
import eu.woolplatform.editor.script.model.ActiveWoolProject;
import eu.woolplatform.wool.model.WoolProjectMetaData;
import eu.woolplatform.wool.model.language.WoolLanguageSet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SideBar extends JPanel {

	public static final int PREFERRED_WIDTH = 200;

	private ActiveWoolProject activeWoolProject;
	private MainWindow mainWindow;
	private ArrayList<SideBarLanguageSetPanel> languageSetPanels;

	public SideBar(ActiveWoolProject activeWoolProject, MainWindow mainWindow) {
		super();

		this.activeWoolProject = activeWoolProject;
		this.mainWindow = mainWindow;

		this.languageSetPanels = new ArrayList<SideBarLanguageSetPanel>();

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		// The project name label
		JLabel projectNameLabel = new JLabel(activeWoolProject.getProjectName());
		WoolEditorUITools.setTitleFont(projectNameLabel);
		projectNameLabel.setMinimumSize(new Dimension(200,30));
		this.add(projectNameLabel);
		layout.putConstraint(SpringLayout.NORTH, projectNameLabel, 5, SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST, projectNameLabel, 5, SpringLayout.WEST,this);

		// A line separator
		JSeparator lineSeparator = new JSeparator();
		this.add(lineSeparator);
		layout.putConstraint(SpringLayout.NORTH, lineSeparator, 5, SpringLayout.SOUTH,projectNameLabel);
		layout.putConstraint(SpringLayout.WEST, lineSeparator, 5, SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.EAST, lineSeparator, -5, SpringLayout.EAST,this);

		// A series of panels for each language set
		int languageSetPanelCount = 0;
		for(WoolLanguageSet languageSet : activeWoolProject.getWoolProjectMetaData().getWoolLanguageMap().getLanguageSets()) {
			SideBarLanguageSetPanel languageSetPanel = new SideBarLanguageSetPanel(this, languageSet);
			languageSetPanel.setVisible(true);
			this.add(languageSetPanel);
			languageSetPanels.add(languageSetPanel);
			if(languageSetPanelCount == 0) {
				layout.putConstraint(SpringLayout.NORTH, languageSetPanel, 5, SpringLayout.SOUTH,lineSeparator);
			} else {
				layout.putConstraint(SpringLayout.NORTH,languageSetPanel,10,SpringLayout.SOUTH,languageSetPanels.get(languageSetPanelCount-1));
			}
			layout.putConstraint(SpringLayout.WEST, languageSetPanel, 1, SpringLayout.WEST,this);
			layout.putConstraint(SpringLayout.EAST, languageSetPanel, -1, SpringLayout.EAST,this);
			languageSetPanelCount++;
		}

		calculateAndSetPreferredSize();
	}

	public void calculateAndSetPreferredSize() {
		// Dynamically set the preferred size for the side bar
		int preferredHeight = 50;
		for(SideBarLanguageSetPanel languageSetPanel : languageSetPanels) {
			preferredHeight += languageSetPanel.getPreferredSize().getHeight() + 10;
		}

		this.setPreferredSize(new Dimension(SideBar.PREFERRED_WIDTH,preferredHeight));
		revalidate();

		revalidate();
		repaint();
	}

	public ActiveWoolProject getActiveWoolProject() {
		return activeWoolProject;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

}
