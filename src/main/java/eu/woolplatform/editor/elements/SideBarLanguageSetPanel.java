package eu.woolplatform.editor.elements;

import eu.woolplatform.wool.model.language.WoolLanguage;
import eu.woolplatform.wool.model.language.WoolLanguageSet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SideBarLanguageSetPanel extends JLayeredPane {

	private SideBar parent;
	private WoolLanguageSet languageSet;
	private List<SideBarLanguagePanel> languagePanels;
	private List<JButton> expandLanguageButtons;
	private SpringLayout layout;
	private JSeparator verticalLine;

	public SideBarLanguageSetPanel(SideBar parent, WoolLanguageSet languageSet) {
		super();

		this.parent = parent;
		this.languageSet = languageSet;
		this.languagePanels = new ArrayList<SideBarLanguagePanel>();
		this.expandLanguageButtons = new ArrayList<JButton>();

		layout = new SpringLayout();
		this.setLayout(layout);

		// There will always be at least one language panel (for the source language).
		WoolLanguage sourceLanguage = languageSet.getSourceLanguage();
		SideBarLanguagePanel sourceLanguagePanel = new SideBarLanguagePanel(this, sourceLanguage, true);
		sourceLanguagePanel.setVisible(true);
		languagePanels.add(sourceLanguagePanel);
		this.add(sourceLanguagePanel);
		layout.putConstraint(SpringLayout.NORTH, sourceLanguagePanel, 0, SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST, sourceLanguagePanel, 20, SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.EAST, sourceLanguagePanel, -5, SpringLayout.EAST,this);

		// After this, create a series of language panels for the translation languages
		List<WoolLanguage> translationLanguages = languageSet.getTranslationLanguages();

		if(translationLanguages != null && translationLanguages.size() > 0) {
			int languagePanelCount = 1;
			for(WoolLanguage language : translationLanguages) {
				SideBarLanguagePanel languagePanel = new SideBarLanguagePanel(this, language, false);
				languagePanel.setVisible(true);
				this.add(languagePanel);
				languagePanels.add(languagePanel);
				if(languagePanelCount == 1) {
					layout.putConstraint(SpringLayout.NORTH, languagePanel, 5, SpringLayout.SOUTH,sourceLanguagePanel);
				} else {
					layout.putConstraint(SpringLayout.NORTH,languagePanel,5,SpringLayout.SOUTH,languagePanels.get(languagePanelCount-1));
				}
				layout.putConstraint(SpringLayout.WEST, languagePanel, 20, SpringLayout.WEST,this);
				layout.putConstraint(SpringLayout.EAST, languagePanel, -5, SpringLayout.EAST,this);
				languagePanelCount++;
			}
		}

		calculateAndSetPreferredSize();

		// Add the vertical "connector bar"
		verticalLine = new JSeparator(JSeparator.VERTICAL);
		verticalLine.setPreferredSize(new Dimension(10, (int) this.getPreferredSize().getHeight()));
		this.add(verticalLine);

		layout.putConstraint(SpringLayout.WEST,verticalLine,4,SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.NORTH,verticalLine,20,SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.SOUTH,verticalLine, (-1 * (int)languagePanels.get(languagePanels.size()-1).getPreferredSize().getHeight()) + 25,SpringLayout.SOUTH,this);

		JButton expandLanguageButton;

		int cumulativeLanguagePanelHeight = 0;
		for(SideBarLanguagePanel languagePanel : languagePanels) {
			expandLanguageButton = new JButton("+");
			expandLanguageButton.setOpaque(true);
			expandLanguageButton.setPreferredSize(new Dimension(14,14));
			this.add(expandLanguageButton);
			this.setComponentZOrder(expandLanguageButton,1);
			expandLanguageButtons.add(expandLanguageButton);

			layout.putConstraint(SpringLayout.WEST,expandLanguageButton,-1,SpringLayout.WEST,verticalLine);
			layout.putConstraint(SpringLayout.NORTH,expandLanguageButton,-5+cumulativeLanguagePanelHeight,SpringLayout.NORTH,verticalLine);

			cumulativeLanguagePanelHeight += languagePanel.getPreferredSize().getHeight()+5;
		}

	}

	public void calculateAndSetPreferredSize() {
		// Set preferred size for this container dynamically
		int preferredHeight = -5;
		for(SideBarLanguagePanel languagePanel : languagePanels) {
			preferredHeight += languagePanel.getPreferredSize().getHeight();
			preferredHeight += 5; // to compensate for the padding
		}

		this.setPreferredSize(new Dimension(SideBar.PREFERRED_WIDTH, preferredHeight));


		if(expandLanguageButtons.size() != 0) {
			int cumulativeLanguagePanelHeight = 0;
			int i = 0;
			for (SideBarLanguagePanel languagePanel : languagePanels) {
				JButton expandLanguageButton = expandLanguageButtons.get(i);

				layout.removeLayoutComponent(expandLanguageButton);

				layout.putConstraint(SpringLayout.WEST, expandLanguageButton, -1, SpringLayout.WEST, verticalLine);
				layout.putConstraint(SpringLayout.NORTH, expandLanguageButton, -5 + cumulativeLanguagePanelHeight, SpringLayout.NORTH, verticalLine);

				cumulativeLanguagePanelHeight += languagePanel.getPreferredSize().getHeight() + 5;
				i++;
			}
		}

		revalidate();
		repaint();
		parent.calculateAndSetPreferredSize();
	}

	public SideBar getParent() {
		return parent;
	}

}
