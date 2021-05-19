package eu.woolplatform.editor.windows.newwoolproject;

import eu.woolplatform.editor.WoolEditorUITools;

import javax.swing.*;
import java.awt.*;

public class NewWoolProjectWizardBasics extends JPanel {

	private JLabel projectLocationHintLabel;
	private JTextField projectNameField, projectFolderField, projectLocationField;
	private JButton browseLocationButton;
	private JTextArea projectLocationHint;

	public NewWoolProjectWizardBasics() {
		super();
		SpringLayout mainPanelLayout = new SpringLayout();
		this.setLayout(mainPanelLayout);

		// Create the side panel for this pane
		JPanel sidePanel = new NewWoolProjectWizardSidePanel(1);
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

		String explanationTextString = "Welcome to the New WOOL Project Wizard. Please provide a name for your WOOL Project, and set the directory path where your WOOL Project will be created.";

		// Create the explanation text field
		JLabel explanationText = new JLabel("<html>"+explanationTextString+"</html>");
		contentPanel.add(explanationText);

		// Snap the explanation text field to north, west, and east
		layout.putConstraint(SpringLayout.NORTH, explanationText, 5, SpringLayout.NORTH,contentPanel);
		layout.putConstraint(SpringLayout.EAST, explanationText, -5, SpringLayout.EAST,contentPanel);
		layout.putConstraint(SpringLayout.WEST, explanationText, 5, SpringLayout.WEST,contentPanel);

		/*
		 * Project Name Label + TextField
		 */

		// Create a box container for the "project name fields"
		JPanel projectNameBox = new JPanel();
		projectNameBox.setLayout(new BoxLayout(projectNameBox, BoxLayout.LINE_AXIS));
		contentPanel.add(projectNameBox);

		// Add the bold-face label
		JLabel projectNameLabel = new JLabel("Project Name:");
		WoolEditorUITools.setBold(projectNameLabel);
		projectNameLabel.setPreferredSize(new Dimension(160,20));
		projectNameLabel.setMinimumSize(new Dimension(160,20));
		projectNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		projectNameBox.add(projectNameLabel);

		// Add a 5 pixel margin
		projectNameBox.add(Box.createRigidArea(new Dimension(5,0)));

		// Add a text field
		projectNameField = new JTextField();
		projectNameField.setBorder(BorderFactory.createLineBorder(Color.decode("#33808f")));
		projectNameBox.add(projectNameField);

		// Snap the project name box to the bottom of the explanation text
		layout.putConstraint(SpringLayout.NORTH, projectNameBox, 30, SpringLayout.SOUTH, explanationText);
		layout.putConstraint(SpringLayout.WEST, projectNameBox,5, SpringLayout.WEST,contentPanel);
		layout.putConstraint(SpringLayout.EAST, projectNameBox,-5, SpringLayout.EAST,contentPanel);

		/*
		 * Project Folder:
		 */

		// Create a box container for the "project folder fields"
		JPanel projectFolderBox = new JPanel();
		projectFolderBox.setLayout(new BoxLayout(projectFolderBox, BoxLayout.LINE_AXIS));
		contentPanel.add(projectFolderBox);

		// Add the bold-face label
		JLabel projectFolderLabel = new JLabel("Project Folder:");
		WoolEditorUITools.setBold(projectFolderLabel);
		projectFolderLabel.setPreferredSize(new Dimension(160,20));
		projectFolderLabel.setMinimumSize(new Dimension(160,20));
		projectFolderLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		projectFolderBox.add(projectFolderLabel);

		// Add a 5 pixel margin
		projectFolderBox.add(Box.createRigidArea(new Dimension(5,0)));

		// Add a text field
		projectFolderField = new JTextField();
		projectFolderField.setBorder(BorderFactory.createLineBorder(Color.decode("#33808f")));
		projectFolderBox.add(projectFolderField);

		// Snap the project folder box to the bottom of the project name box
		layout.putConstraint(SpringLayout.NORTH, projectFolderBox, 10, SpringLayout.SOUTH, projectNameBox);
		layout.putConstraint(SpringLayout.WEST, projectFolderBox,5, SpringLayout.WEST,contentPanel);
		layout.putConstraint(SpringLayout.EAST, projectFolderBox,-5, SpringLayout.EAST,contentPanel);

		/*
		 * Project Location Label + TextField + Button
		 */

		// Create a box container for the "project location fields"
		JPanel projectLocationBox = new JPanel();
		projectLocationBox.setLayout(new BoxLayout(projectLocationBox, BoxLayout.LINE_AXIS));
		contentPanel.add(projectLocationBox);

		// Add the bold-face label
		JLabel projectLocationLabel = new JLabel("Project Location:");
		WoolEditorUITools.setBold(projectLocationLabel);
		projectLocationLabel.setPreferredSize(new Dimension(160,20));
		projectLocationLabel.setMinimumSize(new Dimension(160,20));
		projectLocationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		projectLocationBox.add(projectLocationLabel);

		// Add a 5 pixel margin
		projectLocationBox.add(Box.createRigidArea(new Dimension(5,0)));

		// Add a text field
		projectLocationField = new JTextField();
		projectLocationField.setBorder(BorderFactory.createLineBorder(Color.decode("#33808f")));
		projectLocationBox.add(projectLocationField);

		// Add a 5 pixel margin
		projectLocationBox.add(Box.createRigidArea(new Dimension(5,0)));

		// Add a "Browse" button
		browseLocationButton = new JButton("Browse");
		browseLocationButton.setPreferredSize(new Dimension(100,20));
		projectLocationBox.add(browseLocationButton);

		// Snap the project location box to the bottom of the explanation text
		layout.putConstraint(SpringLayout.NORTH, projectLocationBox, 10, SpringLayout.SOUTH, projectFolderBox);
		layout.putConstraint(SpringLayout.WEST, projectLocationBox,5, SpringLayout.WEST,contentPanel);
		layout.putConstraint(SpringLayout.EAST, projectLocationBox,-5, SpringLayout.EAST,contentPanel);

		/*
		 * Project location hint label
		 */

		JPanel projectLocationHintLabelBox = new JPanel();
		projectLocationHintLabelBox.setLayout(new BoxLayout(projectLocationHintLabelBox, BoxLayout.LINE_AXIS));
		contentPanel.add(projectLocationHintLabelBox);

		projectLocationHintLabel = new JLabel("Your project will be stored under:");
		projectLocationHintLabel.setFont(new Font("Sans-Serif", Font.BOLD, 12));
		projectLocationHintLabel.setVisible(false);
		projectLocationHintLabelBox.add(projectLocationHintLabel);

		// Snap the project location box to the bottom of the explanation text
		layout.putConstraint(SpringLayout.NORTH, projectLocationHintLabelBox, 20, SpringLayout.SOUTH, projectLocationBox);
		layout.putConstraint(SpringLayout.WEST, projectLocationHintLabelBox,25, SpringLayout.WEST,contentPanel);
		layout.putConstraint(SpringLayout.EAST, projectLocationHintLabelBox,-5, SpringLayout.EAST,contentPanel);

		/*
		 * Project location hint
		 */

		JPanel projectLocationHintBox = new JPanel();
		projectLocationHintBox.setLayout(new BoxLayout(projectLocationHintBox, BoxLayout.LINE_AXIS));
		contentPanel.add(projectLocationHintBox);

		projectLocationHint = new JTextArea();
		projectLocationHint.setEditable(false);
		projectLocationHint.setLineWrap(true);
		projectLocationHint.setOpaque(false);
		projectLocationHint.setWrapStyleWord(false);
		projectLocationHint.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		projectLocationHint.setVisible(false);
		projectLocationHintBox.add(projectLocationHint);

		// Snap the project location hint box to the bottom of the explanation text
		layout.putConstraint(SpringLayout.NORTH, projectLocationHintBox, 5, SpringLayout.SOUTH, projectLocationHintLabelBox);
		layout.putConstraint(SpringLayout.WEST, projectLocationHintBox,25, SpringLayout.WEST,contentPanel);
		layout.putConstraint(SpringLayout.EAST, projectLocationHintBox,-5, SpringLayout.EAST,contentPanel);

		// Add contentPanel to mainPanel
		mainPanelLayout.putConstraint(SpringLayout.NORTH, contentPanel, 5, SpringLayout.NORTH,this);
		mainPanelLayout.putConstraint(SpringLayout.WEST, contentPanel, 5, SpringLayout.EAST,sidePanel);
		mainPanelLayout.putConstraint(SpringLayout.SOUTH, contentPanel, 5, SpringLayout.SOUTH,this);
		this.add(contentPanel);

		// Swing magic stuff
		this.setPreferredSize(layout.preferredLayoutSize(contentPanel));
		this.validate();
	}

	public JTextField getProjectNameField() {
		return projectNameField;
	}

	public JTextField getProjectFolderField() {
		return projectFolderField;
	}

	public JTextField getProjectLocationField() {
		return projectLocationField;
	}

	public JLabel getProjectLocationHintLabel() {
		return projectLocationHintLabel;
	}

	public JTextArea getProjectLocationHint() {
		return projectLocationHint;
	}

	public JButton getBrowseLocationButton() {
		return browseLocationButton;
	}

}
