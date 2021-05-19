package eu.woolplatform.editor;

import eu.woolplatform.editor.functions.WoolProjectFunctions;
import eu.woolplatform.editor.script.model.ActiveWoolProject;
import eu.woolplatform.editor.windows.newwoolproject.NewWoolProjectWizard;
import eu.woolplatform.utils.exception.ParseException;
import eu.woolplatform.wool.model.WoolProjectMetaData;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * The LaunchWindow is opened when no existing wool project has been configured to
 * open automatically. It allows users to create a new wool project, open an existing
 * project or enter some configuration.
 */
public class LaunchWindow extends JFrame {

	private JButton exitButton;
	private JButton openWoolProjectButton;
	private JButton newWoolProjectButton;
	private Version applicationVersion;

	/**
	 * Creates an instance of a {@code LaunchWindow}
	 */
	public LaunchWindow() {
		super("Welcome to the WOOL Editor");

		try {
			this.applicationVersion = Version.readFromResource("version.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setResizable(false);

		SpringLayout springLayout = new SpringLayout();
		this.setLayout(springLayout);

		// get the screen size as a java dimension
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// get 2/3 of the height, and 2/3 of the width
		int mainWindowWidth = 500;
		int mainWindowHeight = 500;

		// set the jframe height and width
		this.setPreferredSize(new Dimension(mainWindowWidth, mainWindowHeight));

		// Set the location of this JFrame (center screen)
		this.setLocation((screenSize.width - mainWindowWidth)/2,
				(screenSize.height-mainWindowHeight)/2);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent
											  e) {
				LaunchWindow.this.setVisible(false);
				LaunchWindow.this.dispose();
				System.exit(0);
			}
		});

		// Add the "WOOL Wizard" Image
		ImageIcon woolLogoImageIcon = new ImageIcon(new ImageIcon("src/main/resources/wool-logo.png").getImage().getScaledInstance(233, 131, Image.SCALE_DEFAULT));
		JLabel woolLogoLabel = new JLabel();
		woolLogoLabel.setIcon(woolLogoImageIcon);
		springLayout.putConstraint(SpringLayout.NORTH,woolLogoLabel,10,SpringLayout.NORTH,this);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, woolLogoLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		this.add(woolLogoLabel);

		// Add the "WOOL Editor" label
		JLabel woolEditorLabel = new JLabel("WOOL Editor");
		WoolEditorUITools.setBold(woolEditorLabel);
		springLayout.putConstraint(SpringLayout.NORTH,woolEditorLabel,10,SpringLayout.SOUTH,woolLogoLabel);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, woolEditorLabel,0,SpringLayout.HORIZONTAL_CENTER,this);
		this.add(woolEditorLabel);

		// Add the "Version" label
		JLabel versionLabel = new JLabel("Version "+applicationVersion);
		springLayout.putConstraint(SpringLayout.NORTH,versionLabel,10,SpringLayout.SOUTH,woolEditorLabel);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, versionLabel,0,SpringLayout.HORIZONTAL_CENTER,this);
		this.add(versionLabel);

		// Add the "Create new WOOL Project..." button
		newWoolProjectButton = new JButton("Create new WOOL Project...");
		newWoolProjectButton.setMargin(new Insets(5,5,5,5));
		springLayout.putConstraint(SpringLayout.NORTH,newWoolProjectButton,20,SpringLayout.SOUTH,versionLabel);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, newWoolProjectButton,0,SpringLayout.HORIZONTAL_CENTER,this);
		this.add(newWoolProjectButton);

		// Add the "Open WOOL Project..." button
		openWoolProjectButton = new JButton("Open WOOL Project...");
		openWoolProjectButton.setMargin(new Insets(5,5,5,5));
		springLayout.putConstraint(SpringLayout.NORTH,openWoolProjectButton,10,SpringLayout.SOUTH,newWoolProjectButton);
		springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, openWoolProjectButton,0,SpringLayout.HORIZONTAL_CENTER,this);
		this.add(openWoolProjectButton);

		// Add the "Exit" button
		exitButton = new JButton("Exit");
		exitButton.setMargin(new Insets(5,5,5,5));
		springLayout.putConstraint(SpringLayout.SOUTH,exitButton,-30,SpringLayout.SOUTH,this);
		springLayout.putConstraint(SpringLayout.EAST,exitButton,-5,SpringLayout.EAST,this);
		this.add(exitButton);

		pack();
		addActionListeners();
	}

	/**
	 * Attaches action listeners to the various button in this {@code JFrame}.
	 */
	private void addActionListeners() {
		exitButton.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					actionExit();
				}
			}
		);

		newWoolProjectButton.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					actionNewWoolProject();
				}
			}
		);

		openWoolProjectButton.addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					actionOpenWoolProject();
				}
			}
		);
	}

	/**
	 * Called when the "Exit" button is pressed.
	 */
	private void actionExit() {
		LaunchWindow.this.setVisible(false);
		LaunchWindow.this.dispose();
		System.exit(0);
	}

	/**
	 * Called when the "Create new WOOL Project..." button is pressed.
	 */
	private void actionNewWoolProject() {
		NewWoolProjectWizard view = new NewWoolProjectWizard(this);
	}

	/**
	 * Called when the "Open WOOL Project..." button is pressed.
	 */
	private void actionOpenWoolProject() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Open WOOL Project Folder...");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("WOOL Project MetaData Files", "xml", "XML");
		jfc.setFileFilter(filter);

		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isFile()) {
				File woolProjectMetaDataFile = jfc.getSelectedFile();
				System.out.println("You selected the file: " + jfc.getSelectedFile());
				try {
					WoolProjectMetaData metaData = WoolProjectFunctions.loadLocalWoolProject(woolProjectMetaDataFile);
					SwingUtilities.invokeLater(() -> {
						final MainWindow window = new MainWindow(new ActiveWoolProject(metaData));
						window.setVisible(true);
					});

					this.dispose();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
