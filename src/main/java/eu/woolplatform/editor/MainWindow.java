package eu.woolplatform.editor;

import eu.woolplatform.editor.elements.SideBar;
import eu.woolplatform.editor.elements.ScriptEditor;
import eu.woolplatform.editor.script.model.ActiveWoolProject;
import eu.woolplatform.editor.windows.newwoolproject.NewWoolProjectWizard;
import eu.woolplatform.wool.model.WoolProjectMetaData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class MainWindow extends JFrame implements PropertyChangeListener {

	private ActiveWoolProject activeWoolProject;
	private JSplitPane mainSplitPane;
	private SideBar sideBar;
	private ScriptEditor scriptEditor;
	private Version applicationVersion;
	private WoolEditorSettings woolEditorSettings;
	private JMenuItem saveWoolProjectMenuItem;

	public MainWindow(ActiveWoolProject activeWoolProject) {
		super();

		// Register the MainWindow to listen to changes in the ActiveWoolProject
		activeWoolProject.addPropertyChangeListener(this);

		// Read in the application version from .xml file
		try {
			this.applicationVersion = Version.readFromResource("version.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Read in the application settings from .xml file
		try {
			this.woolEditorSettings = WoolEditorSettings.readFromResource("settings.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setTitle("WOOL Editor v"+applicationVersion+" - "+activeWoolProject.getProjectName());

		// Information about the "currently loaded" wool project
		this.activeWoolProject = activeWoolProject;

		// Set a SpringLayout for this JFrame's contentPane
		SpringLayout mainWindowLayout = new SpringLayout();
		this.getContentPane().setLayout(mainWindowLayout);

		// Set the Frame's dimensions (2/3/screen and centered)
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int mainWindowWidth = screenSize.width * 2 / 3;
		int mainWindowHeight = screenSize.height * 2 / 3;
		this.setPreferredSize(new Dimension(mainWindowWidth, mainWindowHeight));
		this.setLocation((screenSize.width - mainWindowWidth)/2,
				(screenSize.height-mainWindowHeight)/2);

		// Default close operation
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				MainWindow.this.setVisible(false);
				MainWindow.this.dispose();
				System.exit(0);
			}
		});

		// Setup the top menu bar
		createMenuBar();

		// Create the "side bar" and "script editor" views in a Split Pane
		sideBar = new SideBar(activeWoolProject, this);

		JScrollPane sideBarScrollPane = new JScrollPane(sideBar,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scriptEditor = new ScriptEditor(this);
		scriptEditor.setMinimumSize(new Dimension(200,100));
		scriptEditor.setPreferredSize((new Dimension(200,100)));

		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sideBarScrollPane, scriptEditor);
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setDividerLocation(240);
		mainSplitPane.setResizeWeight(0.5);

		mainWindowLayout.putConstraint(SpringLayout.NORTH, mainSplitPane, 5, SpringLayout.NORTH,this.getContentPane());
		mainWindowLayout.putConstraint(SpringLayout.WEST, mainSplitPane, 5, SpringLayout.WEST,this.getContentPane());
		mainWindowLayout.putConstraint(SpringLayout.EAST, mainSplitPane,-5,SpringLayout.EAST,this.getContentPane());
		mainWindowLayout.putConstraint(SpringLayout.SOUTH, mainSplitPane, -5, SpringLayout.SOUTH,this.getContentPane());

		this.getContentPane().add(mainSplitPane);

		pack();
	}

	private void createMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		ImageIcon exitIcon = new ImageIcon(new ImageIcon("src/main/resources" +
				"/window-close-solid.png").getImage().getScaledInstance(16, 16,
				Image.SCALE_DEFAULT));

		ImageIcon newWoolProjectIcon = new ImageIcon(new ImageIcon("src/main" +
				"/resources/project-diagram-solid.png").getImage().getScaledInstance(16,
				16, Image.SCALE_DEFAULT));

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem newWoolProjectMenuItem = new JMenuItem("New WOOL Project...",newWoolProjectIcon);
		newWoolProjectMenuItem.setMnemonic(KeyEvent.VK_N);
		newWoolProjectMenuItem.setToolTipText("Create a new WOOL Project...");
		newWoolProjectMenuItem.addActionListener(
				e -> newWoolProject());

		JMenuItem closeWoolProjectMenuItem = new JMenuItem("Close WOOL Project...",newWoolProjectIcon);
		closeWoolProjectMenuItem.setMnemonic(KeyEvent.VK_C);
		closeWoolProjectMenuItem.setToolTipText("Close the current WOOL Project...");
		closeWoolProjectMenuItem.setEnabled(true);
		closeWoolProjectMenuItem.addActionListener(
				e -> closeWoolProject());

		saveWoolProjectMenuItem = new JMenuItem("Save All",newWoolProjectIcon);
		saveWoolProjectMenuItem.setMnemonic(KeyEvent.VK_S);
		saveWoolProjectMenuItem.setToolTipText("Save all changes to the current WOOL Project...");
		saveWoolProjectMenuItem.setEnabled(false);
		saveWoolProjectMenuItem.addActionListener(
				e -> saveWoolProject());

		JMenuItem exitMenuItem = new JMenuItem("Exit", exitIcon);
		exitMenuItem.setMnemonic(KeyEvent.VK_E);
		exitMenuItem.setToolTipText("Exit application");
		exitMenuItem.addActionListener((event) -> System.exit(0));

		// Add menu items in the correct order
		fileMenu.add(newWoolProjectMenuItem);
		fileMenu.add(closeWoolProjectMenuItem);
		fileMenu.add(saveWoolProjectMenuItem);
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);

		setJMenuBar(menuBar);
	}

	private void newWoolProject() {
		NewWoolProjectWizard view = new NewWoolProjectWizard(this);
	}

	private void closeWoolProject() {
		if(activeWoolProject.isModified()) {
			Object[] options = {"Cancel",
					"Discard changes and close",
					"Save all changes and close"};
			int n = JOptionPane.showOptionDialog(this,
					"The current WOOL project has unsaved changes, would you like to save all changes before closing?",
					"Confirm closing WOOL Project",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					options,
					options[0]);
			if(n == 0) System.out.println("Option 0 chosen: Cancel");
			if(n == 1) System.out.println("Option 1 chosen: Discard & Close");
			if(n == 2) System.out.println("Option 2 chosen: Save & Close");
		} else {
			System.out.println("Active WOOL Project has no modifications, closing project...");
			this.dispose();
			SwingUtilities.invokeLater(() -> {
				final LaunchWindow window = new LaunchWindow();
				window.setVisible(true);
			});
		}
	}

	private void saveWoolProject() {
		try {
			activeWoolProject.saveAllActiveWoolScripts();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ----- Getters:

	public SideBar getSideBar() {
		return sideBar;
	}

	public ScriptEditor getScriptEditor() {
		return scriptEditor;
	}

	public WoolEditorSettings getWoolEditorSettings() {
		return woolEditorSettings;
	}

	public ActiveWoolProject getActiveWoolProject() {
		return activeWoolProject;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(ActiveWoolProject.IS_MODIFIED)) {
			boolean modified = ((Boolean)event.getNewValue()).booleanValue();
			if(modified) {
				saveWoolProjectMenuItem.setEnabled(true);
			}
		}
	}
}