package eu.woolplatform.editor.windows.newwoolproject;

import eu.woolplatform.editor.WoolEditorUITools;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class NewWoolProjectWizardExtras extends JPanel {

	private JTextArea descriptionField;
	private JTextField versionField;

	public NewWoolProjectWizardExtras() {
		super();

		SpringLayout mainPanelLayout = new SpringLayout();
		this.setLayout(mainPanelLayout);

		// Create the side panel for this pane
		JPanel sidePanel = new NewWoolProjectWizardSidePanel(3);
		mainPanelLayout.putConstraint(SpringLayout.NORTH, sidePanel, 5, SpringLayout.NORTH, this);
		mainPanelLayout.putConstraint(SpringLayout.WEST, sidePanel, 5, SpringLayout.WEST, this);
		mainPanelLayout.putConstraint(SpringLayout.SOUTH, sidePanel, 5, SpringLayout.SOUTH, this);
		this.add(sidePanel);

		// Create the content panel and set SpringLayout
		JPanel contentPanel = new JPanel();
		SpringLayout layout = new SpringLayout();
		contentPanel.setLayout(layout);
		contentPanel.setPreferredSize(new Dimension(635, 400));

		//
		/*
		 * Explanation TextArea
		 */

		String explanationTextString = "Next, you can include a short description on what your WOOL Project will be about as well as a version number. You can skip these for now, and come back to them later if you wish.";

		// Create the explanation text field
		JLabel explanationText = new JLabel("<html>"+explanationTextString+"</html>");
		contentPanel.add(explanationText);

		// Snap the explanation text field to north, west, and east
		layout.putConstraint(SpringLayout.NORTH, explanationText, 5, SpringLayout.NORTH,contentPanel);
		layout.putConstraint(SpringLayout.EAST, explanationText, -5, SpringLayout.EAST,contentPanel);
		layout.putConstraint(SpringLayout.WEST, explanationText, 5, SpringLayout.WEST,contentPanel);

		/*
		 * Description Label + TextField
		 */

		JLabel descriptionLabel = new JLabel("Description:");
		WoolEditorUITools.setBold(descriptionLabel);
		descriptionLabel.setPreferredSize(new Dimension(160,20));
		descriptionLabel.setMinimumSize(new Dimension(160,20));
		descriptionLabel.setMaximumSize(new Dimension(160,20));
		descriptionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		layout.putConstraint(SpringLayout.NORTH,descriptionLabel,20,SpringLayout.SOUTH,explanationText);
		layout.putConstraint(SpringLayout.WEST,descriptionLabel,0,SpringLayout.WEST,contentPanel);
		contentPanel.add(descriptionLabel);

		descriptionField = new JTextArea();
		descriptionField.setRows(6);
		//Border border = BorderFactory.createLineBorder(Color.BLACK);
		//descriptionField.setBorder(BorderFactory.createCompoundBorder(border,
				//BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		JScrollPane scrollPane = new JScrollPane(descriptionField);
		layout.putConstraint(SpringLayout.NORTH,scrollPane,2,SpringLayout.NORTH,descriptionLabel);
		layout.putConstraint(SpringLayout.WEST,scrollPane,9,SpringLayout.EAST,descriptionLabel);
		layout.putConstraint(SpringLayout.EAST,scrollPane,-23,SpringLayout.EAST,contentPanel);
		contentPanel.add(scrollPane);

		JLabel versionLabel = new JLabel("Version:");
		WoolEditorUITools.setBold(versionLabel);
		versionLabel.setPreferredSize(new Dimension(160,20));
		versionLabel.setMinimumSize(new Dimension(160,20));
		versionLabel.setMaximumSize(new Dimension(160,20));
		versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		layout.putConstraint(SpringLayout.NORTH,versionLabel,20,SpringLayout.SOUTH,scrollPane);
		layout.putConstraint(SpringLayout.WEST,versionLabel,0,SpringLayout.WEST,contentPanel);
		contentPanel.add(versionLabel);

		versionField = new JTextField();
		layout.putConstraint(SpringLayout.NORTH,versionField,0,SpringLayout.NORTH,versionLabel);
		layout.putConstraint(SpringLayout.WEST,versionField,5,SpringLayout.EAST,versionLabel);
		layout.putConstraint(SpringLayout.EAST,versionField,-20,SpringLayout.EAST,contentPanel);
		contentPanel.add(versionField);

		// Add contentPanel to mainPanel
		mainPanelLayout.putConstraint(SpringLayout.NORTH, contentPanel, 5, SpringLayout.NORTH,this);
		mainPanelLayout.putConstraint(SpringLayout.WEST, contentPanel, 5, SpringLayout.EAST,sidePanel);
		mainPanelLayout.putConstraint(SpringLayout.SOUTH, contentPanel, 5, SpringLayout.SOUTH,this);
		this.add(contentPanel);

		// Swing magic stuff
		this.setPreferredSize(layout.preferredLayoutSize(contentPanel));
		this.validate();
	}

	public JTextArea getDescriptionField() { return descriptionField; }
	public JTextField getVersionField() { return versionField; }
}
