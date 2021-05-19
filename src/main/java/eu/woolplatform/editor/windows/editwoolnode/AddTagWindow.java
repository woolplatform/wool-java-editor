package eu.woolplatform.editor.windows.editwoolnode;

import eu.woolplatform.editor.WoolEditorUITools;
import eu.woolplatform.editor.elements.TagsPanel;
import eu.woolplatform.editor.exceptions.DuplicateOptionalTagException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddTagWindow extends JDialog {

	private TagsPanel parent;
	private JTextField tagNameField, tagValueField;

	public AddTagWindow(TagsPanel parent) {
		super();
		this.parent = parent;
		this.setTitle("Add a tag");

		initComponents();
		pack();

		setFrameSizeAndLocation(300,120);

		pack();
		setVisible(true);
	}

	/**
	 * Sets the size of this frame to the given width and height, and centers
	 * the frame on the screen.
	 * @param frameWidth the requested frame width.
	 * @param frameHeight the requested frame height.
	 */
	private void setFrameSizeAndLocation(int frameWidth, int frameHeight) {

		// Set the JDialog default height and width
		this.setPreferredSize(new Dimension(frameWidth, frameHeight));

		// Set the JDialog minimum height and width
		this.setMinimumSize(new Dimension(frameWidth,frameHeight));

		// Get the screen size as a java dimension
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Set the location of this JFrame (center screen)
		this.setLocation((screenSize.width - frameWidth)/2,
				(screenSize.height-frameHeight)/2);
	}

	private void initComponents() {
		// Set window closing behavior
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				AddTagWindow.this.setVisible(false);
				AddTagWindow.this.dispose();
			}
		});

		// Make this window the only one in focus
		this.setModal(true);

		// Disable resize for this Wizard
		this.setResizable(false);

		JPanel mainPanel = new JPanel();
		this.add(mainPanel);

		SpringLayout layout = new SpringLayout();
		mainPanel.setLayout(layout);

		// ----- Tag Name:
		JLabel tagNameLabel = new JLabel("Tag name:");
		WoolEditorUITools.setBold(tagNameLabel);
		tagNameLabel.setPreferredSize(new Dimension(80,20));
		tagNameLabel.setMinimumSize(new Dimension(80,20));
		tagNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		mainPanel.add(tagNameLabel);

		layout.putConstraint(SpringLayout.NORTH, tagNameLabel, 5, SpringLayout.NORTH, mainPanel);
		layout.putConstraint(SpringLayout.WEST, tagNameLabel,5, SpringLayout.WEST,mainPanel);

		tagNameField = new JTextField();
		tagNameField.setPreferredSize(new Dimension(120,20));
		tagNameField.setMinimumSize(new Dimension(120,20));
		mainPanel.add(tagNameField);

		layout.putConstraint(SpringLayout.NORTH,tagNameField,5,SpringLayout.NORTH,mainPanel);
		layout.putConstraint(SpringLayout.WEST,tagNameField,5,SpringLayout.EAST,tagNameLabel);
		layout.putConstraint(SpringLayout.EAST,tagNameField,-5,SpringLayout.EAST,mainPanel);

		// ----- Tag Value:
		JLabel tagValueLabel = new JLabel("Tag value:");
		WoolEditorUITools.setBold(tagValueLabel);
		tagValueLabel.setPreferredSize(new Dimension(80,20));
		tagValueLabel.setMinimumSize(new Dimension(80,20));
		tagValueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		mainPanel.add(tagValueLabel);

		layout.putConstraint(SpringLayout.NORTH, tagValueLabel, 5, SpringLayout.SOUTH, tagNameLabel);
		layout.putConstraint(SpringLayout.WEST, tagValueLabel,5, SpringLayout.WEST,mainPanel);

		tagValueField = new JTextField();
		tagValueField.setPreferredSize(new Dimension(120,20));
		tagValueField.setMinimumSize(new Dimension(120,20));
		mainPanel.add(tagValueField);

		layout.putConstraint(SpringLayout.NORTH,tagValueField,5,SpringLayout.SOUTH,tagNameLabel);
		layout.putConstraint(SpringLayout.WEST,tagValueField,5,SpringLayout.EAST,tagValueLabel);
		layout.putConstraint(SpringLayout.EAST,tagValueField,-5,SpringLayout.EAST,mainPanel);

		// ----- Buttons:
		JButton saveButton = new JButton("Save & Close");
		mainPanel.add(saveButton);
		layout.putConstraint(SpringLayout.SOUTH,saveButton,-5,SpringLayout.SOUTH,mainPanel);
		layout.putConstraint(SpringLayout.EAST,saveButton,-5,SpringLayout.EAST,mainPanel);


		JButton cancelButton = new JButton("Cancel");
		mainPanel.add(cancelButton);
		layout.putConstraint(SpringLayout.SOUTH,cancelButton,-5,SpringLayout.SOUTH,mainPanel);
		layout.putConstraint(SpringLayout.EAST,cancelButton,-60,SpringLayout.WEST,saveButton);

		// ----- ActionListeners

		saveButton.addActionListener(e->actionSave());
		cancelButton.addActionListener(e->actionCancel());
	}

	private void actionSave() {
		try {
			parent.addNewTag(tagNameField.getText(),tagValueField.getText());
			this.dispose();
		} catch (DuplicateOptionalTagException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	private void actionCancel() {
		this.dispose();
	}
}
