package eu.woolplatform.editor.windows.editwoolnode;


import eu.woolplatform.editor.MainWindow;
import eu.woolplatform.editor.WoolEditorSettings;
import eu.woolplatform.editor.WoolEditorUITools;
import eu.woolplatform.editor.elements.CircleButton;
import eu.woolplatform.editor.elements.ScriptEditorPanel;
import eu.woolplatform.editor.elements.TagsPanel;
import eu.woolplatform.editor.elements.WoolNodePanel;
import eu.woolplatform.editor.script.model.WoolScriptNode;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditWoolNodeWindow extends JDialog {

	private ScriptEditorPanel scriptEditorPanel;
	private MainWindow mainWindow;
	private WoolNodePanel woolNodePanel;

	private String speaker;
	private String title;
	private String script;

	private JTextField speakerTextField, titleTextField;
	private JTextArea scriptField;
	private JButton toggleTagsButton, saveAndCloseButton, discardButton;
	private TagsPanel tagsPanel;

	private boolean tagsShown = false;

	private boolean nodeModified = false;

	// ----- Settings
	private boolean enableScriptWrapping;
	private WoolEditorSettings.SettingShowTags showTags;

	public EditWoolNodeWindow(ScriptEditorPanel scriptEditorPanel, WoolNodePanel woolNodePanel) {
		super(scriptEditorPanel.getMainWindow());

		this.scriptEditorPanel = scriptEditorPanel;
		this.mainWindow = scriptEditorPanel.getMainWindow();
		this.woolNodePanel = woolNodePanel;
		this.speaker = woolNodePanel.getWoolScriptNode().getHeader().getSpeaker();
		this.title = woolNodePanel.getWoolScriptNode().getTitle();
		this.script = woolNodePanel.getWoolScriptNode().getBody().getScript();

		this.setTitle("Edit Node: "+title);

		// ----- Settings:
		enableScriptWrapping = mainWindow.getWoolEditorSettings().getEnableScriptWrapping();
		showTags = mainWindow.getWoolEditorSettings().getShowTags();

		initComponents();

		pack();

		setFrameSizeAndLocation(800,400);

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
		this.setMinimumSize(new Dimension(520,400));

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
				EditWoolNodeWindow.this.setVisible(false);
				EditWoolNodeWindow.this.dispose();
			}
		});

		// Make this window the only one in focus
		this.setModal(true);

		// Disable resize for this Wizard
		this.setResizable(true);

		JPanel mainPanel = new JPanel();
		this.add(mainPanel);

		SpringLayout layout = new SpringLayout();
		mainPanel.setLayout(layout);

		JLabel speakerLabel = new JLabel("Speaker:");
		WoolEditorUITools.setBold(speakerLabel);
		speakerLabel.setPreferredSize(new Dimension(60,20));
		speakerLabel.setMinimumSize(new Dimension(60,20));
		speakerLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		mainPanel.add(speakerLabel);

		layout.putConstraint(SpringLayout.NORTH, speakerLabel, 5, SpringLayout.NORTH, mainPanel);
		layout.putConstraint(SpringLayout.WEST, speakerLabel,5, SpringLayout.WEST,mainPanel);

		speakerTextField = new JTextField(speaker);
		speakerTextField.setPreferredSize(new Dimension(160,20));
		speakerTextField.setMinimumSize(new Dimension(80,20)); // TODO: Check min size
		mainPanel.add(speakerTextField);

		layout.putConstraint(SpringLayout.NORTH,speakerTextField,5,SpringLayout.NORTH,mainPanel);
		layout.putConstraint(SpringLayout.WEST,speakerTextField,5,SpringLayout.EAST,speakerLabel);

		// ----- Circle "Colour" Button
		CircleButton circleButton = new CircleButton("");
		circleButton.setPreferredSize(new Dimension(20,20));
		mainPanel.add(circleButton);
		layout.putConstraint(SpringLayout.NORTH,circleButton,5,SpringLayout.NORTH,mainPanel);
		layout.putConstraint(SpringLayout.EAST,circleButton,-5,SpringLayout.EAST,mainPanel);

		// ----- WOOL Node Title
		JLabel titleLabel = new JLabel("Title:");
		WoolEditorUITools.setBold(titleLabel);
		titleLabel.setPreferredSize(new Dimension(40,20));
		titleLabel.setMinimumSize(new Dimension(40,20));
		titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		mainPanel.add(titleLabel);

		layout.putConstraint(SpringLayout.NORTH,titleLabel,5,SpringLayout.NORTH,mainPanel);
		layout.putConstraint(SpringLayout.WEST,titleLabel,5,SpringLayout.EAST,speakerTextField);

		titleTextField = new JTextField(title);
		titleTextField.setPreferredSize(new Dimension(320,20));
		titleTextField.setMinimumSize(new Dimension(80,20)); // TODO: Check min size
		mainPanel.add(titleTextField);

		layout.putConstraint(SpringLayout.NORTH,titleTextField,5,SpringLayout.NORTH,mainPanel);
		layout.putConstraint(SpringLayout.WEST,titleTextField,5,SpringLayout.EAST,titleLabel);
		layout.putConstraint(SpringLayout.EAST,titleTextField,-5,SpringLayout.WEST,circleButton);

		// ----- "Tags", "Discard" and "Save & Close" Buttons

		toggleTagsButton = new JButton("Show Tags");
		toggleTagsButton.setPreferredSize(new Dimension(100,20));
		mainPanel.add(toggleTagsButton);

		layout.putConstraint(SpringLayout.SOUTH,toggleTagsButton,-5,SpringLayout.SOUTH,mainPanel);
		layout.putConstraint(SpringLayout.WEST,toggleTagsButton,5,SpringLayout.WEST,mainPanel);

		saveAndCloseButton = new JButton("Close");
		saveAndCloseButton.setPreferredSize(new Dimension(120,20));
		mainPanel.add(saveAndCloseButton);

		layout.putConstraint(SpringLayout.SOUTH,saveAndCloseButton,-5,SpringLayout.SOUTH,mainPanel);
		layout.putConstraint(SpringLayout.EAST,saveAndCloseButton,-5,SpringLayout.EAST,mainPanel);

		discardButton = new JButton("Discard");
		discardButton.setPreferredSize(new Dimension(120,20));
		discardButton.setEnabled(false);
		mainPanel.add(discardButton);

		layout.putConstraint(SpringLayout.SOUTH,discardButton,-5,SpringLayout.SOUTH,mainPanel);
		layout.putConstraint(SpringLayout.EAST,discardButton,-100,SpringLayout.WEST,saveAndCloseButton);

		// ----- Tags Panel:
		tagsPanel = new TagsPanel(this);
		tagsPanel.setPreferredSize(new Dimension(0,0));
		mainPanel.add(tagsPanel);

		layout.putConstraint(SpringLayout.SOUTH,tagsPanel,-5,SpringLayout.NORTH,toggleTagsButton);
		layout.putConstraint(SpringLayout.WEST,tagsPanel,5,SpringLayout.WEST,mainPanel);
		layout.putConstraint(SpringLayout.EAST,tagsPanel,-5,SpringLayout.EAST,mainPanel);

		// ----- Script Text Area

		scriptField = new JTextArea(script);
		Font font = new Font("Courier New", Font.PLAIN, 14);
		scriptField.setFont(font);
		scriptField.setLineWrap(enableScriptWrapping);
		JScrollPane scrollPane = new JScrollPane(scriptField);
		layout.putConstraint(SpringLayout.NORTH,scrollPane,5,SpringLayout.SOUTH,speakerLabel);
		layout.putConstraint(SpringLayout.WEST,scrollPane,5,SpringLayout.WEST,mainPanel);
		layout.putConstraint(SpringLayout.EAST,scrollPane,-5,SpringLayout.EAST,mainPanel);
		layout.putConstraint(SpringLayout.SOUTH,scrollPane,-5,SpringLayout.NORTH,tagsPanel);
		mainPanel.add(scrollPane);

		// Add listeners
		toggleTagsButton.addActionListener(e->toggleTags());
		saveAndCloseButton.addActionListener(e->saveAndClose());
		discardButton.addActionListener(e->discard());

		// Add a DocumentListener to the "Speaker" text field
		speakerTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				setNodeModified();
			}
			public void removeUpdate(DocumentEvent e) {
				setNodeModified();
			}
			public void insertUpdate(DocumentEvent e) {
				setNodeModified();
			}
		});

		// Add a DocumentListener to the "Title" text field
		titleTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				setNodeModified();
			}
			public void removeUpdate(DocumentEvent e) {
				setNodeModified();
			}
			public void insertUpdate(DocumentEvent e) {
				setNodeModified();
			}
		});

		// Add a DocumentListener to the "Script" text area
		scriptField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				setNodeModified();
			}
			public void removeUpdate(DocumentEvent e) {
				setNodeModified();
			}
			public void insertUpdate(DocumentEvent e) {
				setNodeModified();
			}
		});

	}

	private void toggleTags() {
		Dimension currentSize = this.getBounds().getSize();
		if(tagsShown) {
			this.setMinimumSize(new Dimension(520,400));
			this.setSize(new Dimension((int)currentSize.getWidth(),(int)currentSize.getHeight()-60));
			tagsPanel.setPreferredSize(new Dimension(0,0));
			this.repaint();
			tagsShown = false;
			toggleTagsButton.setText("Show Tags");
		} else {
			this.setMinimumSize(new Dimension(520,460));
			this.setSize(new Dimension((int)currentSize.getWidth(),(int)currentSize.getHeight()+60));
			tagsPanel.setPreferredSize(new Dimension(0,60));
			this.repaint();
			tagsShown = true;
			toggleTagsButton.setText("Hide Tags");
		}
	}

	private void saveAndClose() {
		WoolScriptNode node = woolNodePanel.getWoolScriptNode();
		node.getHeader().setTitle(titleTextField.getText());
		node.getHeader().setSpeaker(speakerTextField.getText());
		node.getBody().setScript(scriptField.getText());
		node.getHeader().setOptionalTags(tagsPanel.getOptionalTagsMap());
		node.setModified(true);
		this.dispose();
	}

	private void discard() {
		this.dispose();
	}

	public void setNodeModified() {
		if(!nodeModified) {
			this.setTitle(this.getTitle()+" *");
			discardButton.setEnabled(true);
			saveAndCloseButton.setText("Save & Close");
		}
		nodeModified = true;
	}

	// ----- Getters

	public WoolNodePanel getWoolNodePanel() {
		return woolNodePanel;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}
}
