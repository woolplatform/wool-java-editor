package eu.woolplatform.editor.windows.newwoolproject;

import eu.woolplatform.editor.*;
import eu.woolplatform.editor.language.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * TODO: Create attribution for language resource (https://github.com/bbqsrc/iso639-databases)
 */
public class NewWoolProjectWizardLanguages extends JPanel {

	private JRadioButton languageSetSimplifiedButton, languageSetExtendedButton, languageSetCustomButton;
	private JComboBox<Language> defaultLanguageList;
	private JCheckBox autonymSwitcher;
	private JTextField customLanguageField, customLanguageNameField;
	private JLabel autonymSwitchLabel,
			customLanguageHelpLabel,
			customLanguageWarningIconLabel,
			customLanguageRecognizedIconLabel,
			customLanguageMessageLabel,
			customLanguageNameLabel;

	public NewWoolProjectWizardLanguages() {
		super();

		SpringLayout mainPanelLayout = new SpringLayout();
		this.setLayout(mainPanelLayout);

		// Create the side panel for this pane
		JPanel sidePanel = new NewWoolProjectWizardSidePanel(2);
		mainPanelLayout.putConstraint(SpringLayout.NORTH, sidePanel, 5, SpringLayout.NORTH,this);
		mainPanelLayout.putConstraint(SpringLayout.WEST, sidePanel, 5, SpringLayout.WEST,this);
		mainPanelLayout.putConstraint(SpringLayout.SOUTH, sidePanel, 5, SpringLayout.SOUTH,this);
		this.add(sidePanel);

		// Create the content panel and set SpringLayout
		JPanel contentPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		contentPanel.setLayout(layout);
		contentPanel.setPreferredSize(new Dimension(635,400));

		/*
		 * Explanation TextArea
		 */

		String explanationTextString = "Next, you can indicate which language will be used by default in this project. Select “simplified” for a short list of common languages. Choose “extended” for a more extensive list. Alternatively, choose “Custom” to fill in your own language ISO code.";

		// Create the explanation text field
		JLabel explanationText = new JLabel("<html>"+explanationTextString+"</html>");
		contentPanel.add(explanationText);

		// Snap the explanation text field to north, west, and east
		layout.putConstraint(SpringLayout.NORTH, explanationText, 5, SpringLayout.NORTH,contentPanel);
		layout.putConstraint(SpringLayout.EAST, explanationText, -5, SpringLayout.EAST,contentPanel);
		layout.putConstraint(SpringLayout.WEST, explanationText, 5, SpringLayout.WEST,contentPanel);

		/*
		 * Language Set Label + Radio Buttons
		 */

		JLabel languageSetLabel = new JLabel("Language set:");
		WoolEditorUITools.setBold(languageSetLabel);
		languageSetLabel.setPreferredSize(new Dimension(160,20));
		languageSetLabel.setMinimumSize(new Dimension(160,20));
		languageSetLabel.setMaximumSize(new Dimension(160,20));
		languageSetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		layout.putConstraint(SpringLayout.NORTH,languageSetLabel,20,SpringLayout.SOUTH,explanationText);
		layout.putConstraint(SpringLayout.WEST,languageSetLabel,0,SpringLayout.WEST,contentPanel);
		contentPanel.add(languageSetLabel);

		languageSetSimplifiedButton = new JRadioButton("Simplified");
		languageSetSimplifiedButton.setMnemonic(KeyEvent.VK_S);
		languageSetSimplifiedButton.setActionCommand("simplified");
		languageSetSimplifiedButton.setSelected(true);

		languageSetExtendedButton = new JRadioButton("Extended");
		languageSetExtendedButton.setMnemonic(KeyEvent.VK_E);
		languageSetExtendedButton.setActionCommand("extended");

		languageSetCustomButton = new JRadioButton("Custom");
		languageSetCustomButton.setMnemonic(KeyEvent.VK_C);
		languageSetCustomButton.setActionCommand("custom");

		ButtonGroup languageSetButtonGroup = new ButtonGroup();
		languageSetButtonGroup.add(languageSetSimplifiedButton);
		languageSetButtonGroup.add(languageSetExtendedButton);
		languageSetButtonGroup.add(languageSetCustomButton);

		layout.putConstraint(SpringLayout.NORTH,languageSetSimplifiedButton,0,SpringLayout.NORTH,languageSetLabel);
		layout.putConstraint(SpringLayout.WEST,languageSetSimplifiedButton,5,SpringLayout.EAST,languageSetLabel);
		contentPanel.add(languageSetSimplifiedButton);

		layout.putConstraint(SpringLayout.NORTH,languageSetExtendedButton,2,SpringLayout.SOUTH,languageSetSimplifiedButton);
		layout.putConstraint(SpringLayout.WEST,languageSetExtendedButton,5,SpringLayout.EAST,languageSetLabel);
		contentPanel.add(languageSetExtendedButton);

		layout.putConstraint(SpringLayout.NORTH,languageSetCustomButton,2,SpringLayout.SOUTH,languageSetExtendedButton);
		layout.putConstraint(SpringLayout.WEST,languageSetCustomButton,5,SpringLayout.EAST,languageSetLabel);
		contentPanel.add(languageSetCustomButton);

		JLabel defaultLanguageLabel = new JLabel("Default Language:");
		WoolEditorUITools.setBold(defaultLanguageLabel);
		defaultLanguageLabel.setPreferredSize(new Dimension(160,20));
		defaultLanguageLabel.setMinimumSize(new Dimension(160,20));
		defaultLanguageLabel.setMaximumSize(new Dimension(160,20));
		defaultLanguageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		layout.putConstraint(SpringLayout.NORTH,defaultLanguageLabel,20,SpringLayout.SOUTH,languageSetCustomButton);
		layout.putConstraint(SpringLayout.WEST,defaultLanguageLabel,0,SpringLayout.WEST,contentPanel);
		contentPanel.add(defaultLanguageLabel);

		defaultLanguageList = new JComboBox<Language>();
		layout.putConstraint(SpringLayout.NORTH,defaultLanguageList,0,SpringLayout.NORTH,defaultLanguageLabel);
		layout.putConstraint(SpringLayout.WEST,defaultLanguageList,5,SpringLayout.EAST,defaultLanguageLabel);
		layout.putConstraint(SpringLayout.EAST,defaultLanguageList,-20,SpringLayout.EAST,contentPanel);
		contentPanel.add(defaultLanguageList);

		autonymSwitcher = new JCheckBox();
		autonymSwitcher.setSelected(true);
		layout.putConstraint(SpringLayout.SOUTH,autonymSwitcher,-5,SpringLayout.NORTH,defaultLanguageList);
		layout.putConstraint(SpringLayout.EAST,autonymSwitcher,-20,SpringLayout.EAST,contentPanel);
		contentPanel.add(autonymSwitcher);

		autonymSwitchLabel = new JLabel("Use English language names");
		WoolEditorUITools.setItalic(autonymSwitchLabel);
		layout.putConstraint(SpringLayout.EAST,autonymSwitchLabel,0,SpringLayout.WEST,autonymSwitcher);
		layout.putConstraint(SpringLayout.NORTH,autonymSwitchLabel,3,SpringLayout.NORTH,autonymSwitcher);
		contentPanel.add(autonymSwitchLabel);

		customLanguageField = new JTextField();
		customLanguageField.setPreferredSize(new Dimension(100,22));
		customLanguageField.setMinimumSize(new Dimension(100,22));
		customLanguageField.setMaximumSize(new Dimension(100,22));
		layout.putConstraint(SpringLayout.NORTH,customLanguageField,0,SpringLayout.NORTH,defaultLanguageLabel);
		layout.putConstraint(SpringLayout.WEST,customLanguageField,5,SpringLayout.EAST,defaultLanguageLabel);
		customLanguageField.setVisible(false);
		contentPanel.add(customLanguageField);

		customLanguageHelpLabel = new JLabel("(ISO Code)");
		layout.putConstraint(SpringLayout.NORTH,customLanguageHelpLabel,2,SpringLayout.NORTH,customLanguageField);
		layout.putConstraint(SpringLayout.WEST,customLanguageHelpLabel,5,SpringLayout.EAST,customLanguageField);
		customLanguageHelpLabel.setVisible(false);
		contentPanel.add(customLanguageHelpLabel);

		ImageIcon customLanguageWarningIcon = new ImageIcon("src/main/resources/icons/fa/exclamation-triangle-solid.png");
		customLanguageWarningIconLabel = new JLabel();
		customLanguageWarningIconLabel.setIcon(customLanguageWarningIcon);
		layout.putConstraint(SpringLayout.NORTH,customLanguageWarningIconLabel,10,SpringLayout.SOUTH,customLanguageField);
		layout.putConstraint(SpringLayout.WEST,customLanguageWarningIconLabel,10,SpringLayout.WEST,customLanguageField);
		customLanguageWarningIconLabel.setVisible(false);
		contentPanel.add(customLanguageWarningIconLabel);

		ImageIcon customLanguageRecognizedIcon = new ImageIcon("src/main/resources/icons/fa/check-circle-solid.png");
		customLanguageRecognizedIconLabel = new JLabel();
		customLanguageRecognizedIconLabel.setIcon(customLanguageRecognizedIcon);
		layout.putConstraint(SpringLayout.NORTH,customLanguageRecognizedIconLabel,10,SpringLayout.SOUTH,customLanguageField);
		layout.putConstraint(SpringLayout.WEST,customLanguageRecognizedIconLabel,10,SpringLayout.WEST,customLanguageField);
		customLanguageRecognizedIconLabel.setVisible(false);
		contentPanel.add(customLanguageRecognizedIconLabel);

		customLanguageMessageLabel = new JLabel();
		layout.putConstraint(SpringLayout.NORTH,customLanguageMessageLabel,2,SpringLayout.NORTH,customLanguageRecognizedIconLabel);
		layout.putConstraint(SpringLayout.WEST,customLanguageMessageLabel,10,SpringLayout.EAST,customLanguageRecognizedIconLabel);
		customLanguageMessageLabel.setVisible(false);
		contentPanel.add(customLanguageMessageLabel);

		customLanguageNameLabel = new JLabel("Language Name:");
		WoolEditorUITools.setBold(customLanguageNameLabel);
		customLanguageNameLabel.setPreferredSize(new Dimension(160,20));
		customLanguageNameLabel.setMinimumSize(new Dimension(160,20));
		customLanguageNameLabel.setMaximumSize(new Dimension(160,20));
		customLanguageNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		layout.putConstraint(SpringLayout.NORTH, customLanguageNameLabel,20,SpringLayout.SOUTH,customLanguageMessageLabel);
		layout.putConstraint(SpringLayout.WEST, customLanguageNameLabel,0,SpringLayout.WEST,contentPanel);
		customLanguageNameLabel.setVisible(false);
		contentPanel.add(customLanguageNameLabel);

		customLanguageNameField = new JTextField();
		layout.putConstraint(SpringLayout.NORTH,customLanguageNameField,0,SpringLayout.NORTH, customLanguageNameLabel);
		layout.putConstraint(SpringLayout.WEST,customLanguageNameField,5,SpringLayout.EAST, customLanguageNameLabel);
		layout.putConstraint(SpringLayout.EAST,customLanguageNameField,-20,SpringLayout.EAST,contentPanel);
		customLanguageNameField.setVisible(false);
		contentPanel.add(customLanguageNameField);

		// Add contentPanel to mainPanel
		mainPanelLayout.putConstraint(SpringLayout.NORTH, contentPanel, 5, SpringLayout.NORTH,this);
		mainPanelLayout.putConstraint(SpringLayout.WEST, contentPanel, 5, SpringLayout.EAST,sidePanel);
		mainPanelLayout.putConstraint(SpringLayout.SOUTH, contentPanel, 5, SpringLayout.SOUTH,this);
		this.add(contentPanel);

		// Swing magic stuff
		this.setPreferredSize(layout.preferredLayoutSize(contentPanel));
		this.validate();
	}

	public JRadioButton getLanguageSetSimplifiedButton() { return languageSetSimplifiedButton; }
	public JRadioButton getLanguageSetExtendedButton() { return languageSetExtendedButton; }
	public JRadioButton getLanguageSetCustomButton() { return languageSetCustomButton; }
	public JComboBox<Language> getDefaultLanguageList() { return defaultLanguageList; }
	public JTextField getCustomLanguageField() { return customLanguageField; }
	public JCheckBox getAutonymSwitcher() { return autonymSwitcher; }
	public JLabel getAutonymSwitchLabel() { return autonymSwitchLabel; }
	public JLabel getCustomLanguageHelpLabel() { return customLanguageHelpLabel; }
	public JLabel getCustomLanguageWarningIconLabel() { return customLanguageWarningIconLabel; }
	public JLabel getCustomLanguageRecognizedIconLabel() { return customLanguageRecognizedIconLabel; }
	public JLabel getCustomLanguageMessageLabel() { return customLanguageMessageLabel; }
	public JLabel getCustomLanguageNameLabel() { return customLanguageNameLabel; }
	public JTextField getCustomLanguageNameField() { return customLanguageNameField; }
}