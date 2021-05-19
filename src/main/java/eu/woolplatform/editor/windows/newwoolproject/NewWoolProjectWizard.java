package eu.woolplatform.editor.windows.newwoolproject;

import eu.woolplatform.wool.model.WoolProject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NewWoolProjectWizard extends JDialog {

	private WoolProject model;
	private NewWoolProjectWizardController controller;
	private NewWoolProjectWizardBasics pageBasics;
	private NewWoolProjectWizardLanguages pageLanguages;
	private NewWoolProjectWizardExtras pageExtras;

	protected static final String WIZARD_PAGE_ONE = "Basics";
	protected static final String WIZARD_PAGE_TWO = "Languages";
	protected static final String WIZARD_PAGE_THREE = "Extras";

	private JButton backButton, nextButton, cancelButton;
	private JPanel cardPanel;

	public NewWoolProjectWizard(JFrame owner) {
		super(owner);

		initComponents();

		pack();

		setFrameSizeAndLocation(800,400);

		WoolProject model = new WoolProject();
		NewWoolProjectWizardController controller = new NewWoolProjectWizardController(model,this);
		controller.initController();

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
				NewWoolProjectWizard.this.setVisible(false);
				NewWoolProjectWizard.this.dispose();
			}
		});

		// Make this window the only one in focus
		this.setModal(true);

		// Disable resize for this Wizard
		this.setResizable(false);

		JPanel buttonPanel = new JPanel();
		Box buttonBox = new Box(BoxLayout.X_AXIS);

		cardPanel = new JPanel();
		cardPanel.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));

		CardLayout cardLayout = new CardLayout();
		cardPanel.setLayout(cardLayout);
		backButton = new JButton("Back");
		nextButton = new JButton("Next");
		cancelButton = new JButton("Cancel");

		buttonPanel.setLayout(new BorderLayout());
		buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

		buttonBox.setBorder(new EmptyBorder(new Insets(0, 0, 5, 0)));
		buttonBox.add(backButton);
		buttonBox.add(Box.createHorizontalStrut(10));
		buttonBox.add(nextButton);
		buttonBox.add(Box.createHorizontalStrut(30));
		buttonBox.add(cancelButton);
		buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);
		this.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
		this.getContentPane().add(cardPanel, java.awt.BorderLayout.CENTER);

		// Add first page "Basics"
		pageBasics = new NewWoolProjectWizardBasics();
		cardPanel.add(pageBasics, WIZARD_PAGE_ONE);

		// Add second page "Languages"
		pageLanguages = new NewWoolProjectWizardLanguages();
		cardPanel.add(pageLanguages, WIZARD_PAGE_TWO);

		// Add third page "Extras"
		pageExtras = new NewWoolProjectWizardExtras();
		cardPanel.add(pageExtras, WIZARD_PAGE_THREE);

		cardPanel.validate();
	}

	public JPanel getCardPanel() {
		return cardPanel;
	}

	public JButton getBackButton() {
		return backButton;
	}

	public JButton getNextButton() {
		return nextButton;
	}

	public JButton getCancelButton() {
		return cancelButton;
	}

	public NewWoolProjectWizardBasics getPageBasics() {
		return pageBasics;
	}

	public NewWoolProjectWizardLanguages getPageLanguages() {
		return pageLanguages;
	}

	public NewWoolProjectWizardExtras getPageExtras() {
		return pageExtras;
	}

}
