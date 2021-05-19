package eu.woolplatform.editor.windows.newwoolproject;

import eu.woolplatform.editor.MainWindow;
import eu.woolplatform.editor.functions.WoolProjectFunctions;
import eu.woolplatform.editor.language.Language;
import eu.woolplatform.editor.language.LanguageCellRenderer;
import eu.woolplatform.editor.language.LanguageFilter;
import eu.woolplatform.editor.language.LanguageSet;
import eu.woolplatform.editor.script.model.ActiveWoolProject;
import eu.woolplatform.wool.model.WoolProject;
import eu.woolplatform.wool.model.WoolProjectMetaData;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class NewWoolProjectWizardController {

	private final WoolProject model;
	private final NewWoolProjectWizard view;
	private final NewWoolProjectWizardBasics pageBasics;
	private final NewWoolProjectWizardLanguages pageLanguages;
	private final NewWoolProjectWizardExtras pageExtras;

	private String projectName = "";
	private String projectFolder = "";
	private String projectLocation = "";
	private String projectFullPathLabelText = "";

	private boolean projectNameOkay = false;
	private boolean projectFolderOkay = false;
	private boolean projectLocationOkay = false;

	private int currentStep;

	private LanguageSet languageSetAll;
	private LanguageSet languageSetSimplified;
	private LanguageSet languageSetExtended;
	private String currentLanguageSet;
	private boolean useAutonyms;

	private Language selectedDefaultLanguage = null;

	public NewWoolProjectWizardController(WoolProject model, NewWoolProjectWizard view) {
		this.model = model;
		this.view = view;
		this.pageBasics = view.getPageBasics();
		this.pageLanguages = view.getPageLanguages();
		this.pageExtras = view.getPageExtras();
		this.currentStep = 1;

		// Read the language definitions from resource files.
		try {
			languageSetAll = LanguageSet.readFromTSV("iso639-autonyms.tsv");
			languageSetSimplified = languageSetAll.getFilteredLanguageSet(LanguageFilter.readFromResource("language-filter-simplified.xml"));
			languageSetExtended = languageSetAll.getFilteredLanguageSet(LanguageFilter.readFromResource("language-filter-extended.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		setLanguageSetSimplified();
		useAutonyms = false;
		pageLanguages.getDefaultLanguageList().setRenderer(new LanguageCellRenderer(useAutonyms));

		view.getBackButton().setEnabled(false);
		view.getNextButton().setEnabled(false);
	}

	public void initController() {

		// ----- Action Listeners related to Page 1: Basics

		// Add a DocumentListener to the project name field
		pageBasics.getProjectNameField().getDocument().addDocumentListener(new
		DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				validateProjectName();
			}
			public void removeUpdate(DocumentEvent e) {
				validateProjectName();
			}
			public void insertUpdate(DocumentEvent e) {
				validateProjectName();
			}
		});

		// Add a DocumentListener to the project folder field
		pageBasics.getProjectFolderField().getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				validateProjectFolder();
			}
			public void removeUpdate(DocumentEvent e) {
				validateProjectFolder();
			}
			public void insertUpdate(DocumentEvent e) {
				validateProjectFolder();
			}
		});

		// Add a DocumentListener to the project location field
		pageBasics.getProjectLocationField().getDocument().addDocumentListener(new DocumentListener() {
			 public void changedUpdate(DocumentEvent e) {
				 validateProjectLocation();
			 }
			 public void removeUpdate(DocumentEvent e) {
				 validateProjectLocation();
			 }
			 public void insertUpdate(DocumentEvent e) {
				 validateProjectLocation();
			 }
		 });

		pageBasics.getBrowseLocationButton().addActionListener(e->browseLocation());

		// ----- Action Listeners related to Page 2: Languages

		pageLanguages.getLanguageSetSimplifiedButton().addActionListener(e->setLanguageSetSimplified());
		pageLanguages.getLanguageSetExtendedButton().addActionListener(e->setLanguageSetExtended());
		pageLanguages.getLanguageSetCustomButton().addActionListener(e->setLanguageSetCustom());
		pageLanguages.getAutonymSwitcher().addActionListener(e->toggleAutonyms());
		pageLanguages.getDefaultLanguageList().addActionListener(e->selectLanguage());

		// Add a DocumentListener to the custom language code field
		pageLanguages.getCustomLanguageField().getDocument().addDocumentListener(new DocumentListener() {
			   public void changedUpdate(DocumentEvent e) {
				   validateCustomLanguageField();
			   }
			   public void removeUpdate(DocumentEvent e) {
				   validateCustomLanguageField();
			   }
			   public void insertUpdate(DocumentEvent e) {
				   validateCustomLanguageField();
			   }
		   });

		// Add a DocumentListener to the custom language name field
		pageLanguages.getCustomLanguageNameField().getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				validateCustomLanguageNameField();
			}
			public void removeUpdate(DocumentEvent e) {
				validateCustomLanguageNameField();
			}
			public void insertUpdate(DocumentEvent e) {
				validateCustomLanguageNameField();
			}
		});

		// ----- Action Listeners related to the bottom control bar

		view.getNextButton().addActionListener(e->nextStep());
		view.getBackButton().addActionListener(e->previousStep());
		view.getCancelButton().addActionListener(e->cancel());
	}

	private void nextStep() {
		JPanel cardPanel = view.getCardPanel();
		CardLayout cardLayout = (CardLayout) cardPanel.getLayout();

		if(currentStep == 1) {
			cardLayout.show(cardPanel, NewWoolProjectWizard.WIZARD_PAGE_TWO);
			validateStepTwoForm();
			view.getBackButton().setEnabled(true);
			currentStep = 2;
		} else if(currentStep == 2) {
			cardLayout.show(cardPanel, NewWoolProjectWizard.WIZARD_PAGE_THREE);
			view.getNextButton().setText("Finish");
			currentStep = 3;
		} else if(currentStep == 3) {
			createNewWoolProject();
		}
	}

	private void previousStep() {
		JPanel cardPanel = view.getCardPanel();
		CardLayout cardLayout = (CardLayout) cardPanel.getLayout();

		if(currentStep == 2) {
			cardLayout.show(cardPanel, NewWoolProjectWizard.WIZARD_PAGE_ONE);
			validateStepOneForm();
			view.getBackButton().setEnabled(false);
			currentStep = 1;
		} else if(currentStep == 3) {
			cardLayout.show(cardPanel, NewWoolProjectWizard.WIZARD_PAGE_TWO);
			view.getNextButton().setText("Next");
			currentStep = 2;
		}
	}

	private void cancel() {
		view.dispose();
	}

	private void browseLocation() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a directory for your WOOL project: ");
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isDirectory()) {
				System.out.println("You selected the directory: " + jfc.getSelectedFile());
				pageBasics.getProjectLocationField().setText(jfc.getSelectedFile().toString());
			}
		}
	}

	private void validateProjectName() {
		projectName = pageBasics.getProjectNameField().getText();
		projectFolder = projectName.toLowerCase().replace(' ','-');
		projectFolder = projectFolder.replaceAll("[^a-zA-Z0-9\\-]+","");
		pageBasics.getProjectFolderField().setText(projectFolder);

		projectNameOkay = !projectName.equals("");
		validateStepOneForm();
	}

	private void validateProjectFolder() {
		projectFolder = pageBasics.getProjectFolderField().getText();

		// If no replacements needed, it's okay
		if(projectFolder.replaceAll("[^a-zA-Z0-9\\-]+","").equals(projectFolder)) {
			projectFolderOkay = true;
			pageBasics.getProjectFolderField().setBorder(BorderFactory.createLineBorder(Color.decode("#33808f")));
		} else {
			projectFolderOkay = false;
			pageBasics.getProjectFolderField().setBorder(BorderFactory.createLineBorder(Color.decode("#e34b30")));
		}

		validateStepOneForm();
	}

	private void validateProjectLocation() {
		projectLocation = pageBasics.getProjectLocationField().getText();

		if(projectLocation.equals("")) { // Location is not okay, but let's not paint the textfield red
			projectLocationOkay = false;
			pageBasics.getProjectLocationField().setBorder(BorderFactory.createLineBorder(Color.decode("#33808f")));

		} else if(new File(projectLocation).exists()) {
			projectLocationOkay = true;
			pageBasics.getProjectLocationField().setBorder(BorderFactory.createLineBorder(Color.decode("#33808f")));

		} else {
			pageBasics.getProjectLocationField().setBorder(BorderFactory.createLineBorder(Color.decode("#e34b30")));
			projectLocationOkay = false;
		}

		validateStepOneForm();
	}

	private void validateStepOneForm() {
		if(projectNameOkay && projectFolderOkay && projectLocationOkay) {
			view.getNextButton().setEnabled(true);
			pageBasics.getProjectLocationHintLabel().setVisible(true);

			if(projectLocation.endsWith(File.separator)) {
				projectFullPathLabelText = projectLocation + projectFolder + File.separator;
			} else {
				projectFullPathLabelText = projectLocation + File.separator + projectFolder + File.separator;
			}

			pageBasics.getProjectLocationHint().setText(projectFullPathLabelText);
			pageBasics.getProjectLocationHint().setVisible(true);

		} else {
			view.getNextButton().setEnabled(false);
			pageBasics.getProjectLocationHintLabel().setVisible(false);
			pageBasics.getProjectLocationHint().setVisible(false);
		}
	}

	private void validateStepTwoForm() {
		if(selectedDefaultLanguage == null) {
			view.getNextButton().setEnabled(false);
		} else {
			view.getNextButton().setEnabled(true);
		}
	}

	private void validateStepThreeForm() {
		// Everything ok :)
		//view.getNextButton().setText("Finish");

	}

	private void setLanguageSetSimplified() {
		if(!LanguageSet.SIMPLIFIED.equals(this.currentLanguageSet)) {
			toggleCustomLanguageFieldVisibility(false);
			DefaultComboBoxModel model = new DefaultComboBoxModel(languageSetSimplified.getSortedArray(useAutonyms));
			pageLanguages.getDefaultLanguageList().setModel(model);
			pageLanguages.getDefaultLanguageList().insertItemAt(new Language(null,"<Select from simplified set...>",null,null),0);
			if(this.currentLanguageSet == null) {
				// First time initialisation, set default language to 'en'
				pageLanguages.getDefaultLanguageList().setSelectedIndex(3);
				selectedDefaultLanguage = (Language)pageLanguages.getDefaultLanguageList().getSelectedItem();
			} else {
				pageLanguages.getDefaultLanguageList().setSelectedIndex(0);
				selectedDefaultLanguage = null;
			}
			this.currentLanguageSet = LanguageSet.SIMPLIFIED;
		}
		validateStepTwoForm();
	}

	private void setLanguageSetExtended() {
		if(!LanguageSet.EXTENDED.equals(this.currentLanguageSet)) {
			toggleCustomLanguageFieldVisibility(false);
			DefaultComboBoxModel model = new DefaultComboBoxModel(languageSetExtended.getSortedArray(useAutonyms));
			pageLanguages.getDefaultLanguageList().setModel(model);
			pageLanguages.getDefaultLanguageList().insertItemAt(new Language(null,"<Select from extended set...>",null,null),0);
			pageLanguages.getDefaultLanguageList().setSelectedIndex(0);
			selectedDefaultLanguage = null;
			this.currentLanguageSet = LanguageSet.EXTENDED;
		}
		validateStepTwoForm();
	}

	private void setLanguageSetCustom() {
		if(!LanguageSet.CUSTOM.equals(this.currentLanguageSet)) {
			selectedDefaultLanguage = null;
			toggleCustomLanguageFieldVisibility(true);
			this.currentLanguageSet = LanguageSet.CUSTOM;
		}
		validateStepTwoForm();
	}

	private void toggleCustomLanguageFieldVisibility(boolean show) {
		if(show) {
			pageLanguages.getDefaultLanguageList().setVisible(false);
			pageLanguages.getAutonymSwitchLabel().setVisible(false);
			pageLanguages.getAutonymSwitcher().setVisible(false);
			pageLanguages.getCustomLanguageField().setVisible(true);
			pageLanguages.getCustomLanguageHelpLabel().setVisible(true);
		} else {
			pageLanguages.getDefaultLanguageList().setVisible(true);
			pageLanguages.getAutonymSwitchLabel().setVisible(true);
			pageLanguages.getAutonymSwitcher().setVisible(true);
			pageLanguages.getCustomLanguageField().setVisible(false);
			pageLanguages.getCustomLanguageField().setText("");
			pageLanguages.getCustomLanguageNameField().setText("");
		}
	}

	private void toggleAutonyms() {
		if(useAutonyms) {
			useAutonyms = false;
		} else {
			useAutonyms = true;
		}
		LanguageCellRenderer renderer = (LanguageCellRenderer)pageLanguages.getDefaultLanguageList().getRenderer();
		renderer.setUseAutonyms(useAutonyms);

		Language selectedLanguage = (Language)pageLanguages.getDefaultLanguageList().getSelectedItem();

		DefaultComboBoxModel model = null;
		if(this.currentLanguageSet.equals(LanguageSet.EXTENDED)) {
			model= new DefaultComboBoxModel(languageSetExtended.getSortedArray(useAutonyms));
			pageLanguages.getDefaultLanguageList().setModel(model);
			pageLanguages.getDefaultLanguageList().insertItemAt(new Language(null,"<Select from extended set...>",null,null),0);
		} else if(this.currentLanguageSet.equals(LanguageSet.SIMPLIFIED)) {
			model= new DefaultComboBoxModel(languageSetSimplified.getSortedArray(useAutonyms));
			pageLanguages.getDefaultLanguageList().setModel(model);
			pageLanguages.getDefaultLanguageList().insertItemAt(new Language(null,"<Select from simplified set...>",null,null),0);
		}

		pageLanguages.getDefaultLanguageList().setSelectedItem(selectedLanguage);
		pageLanguages.getDefaultLanguageList().repaint();
	}

	private void selectLanguage() {
		if(pageLanguages.getDefaultLanguageList().getSelectedIndex() != 0) {
			selectedDefaultLanguage = (Language) pageLanguages.getDefaultLanguageList().getSelectedItem();
		} else {
			selectedDefaultLanguage = null;
		}
		validateStepTwoForm();
	}

	private void validateCustomLanguageField() {
		String entry = pageLanguages.getCustomLanguageField().getText();
		if(entry.length() > 1) {
			selectedDefaultLanguage = languageSetAll.findMatch(entry);
			if(selectedDefaultLanguage != null) {
				String message = selectedDefaultLanguage.getName();
				if(selectedDefaultLanguage.getAutonym() != null) {
					message+=" ("+selectedDefaultLanguage.getAutonym()+")";
				}
				pageLanguages.getCustomLanguageMessageLabel().setText(message);
				pageLanguages.getCustomLanguageMessageLabel().setVisible(true);
				pageLanguages.getCustomLanguageWarningIconLabel().setVisible(false);
				pageLanguages.getCustomLanguageRecognizedIconLabel().setVisible(true);
				pageLanguages.getCustomLanguageNameLabel().setVisible(false);
				pageLanguages.getCustomLanguageNameField().setVisible(false);
			} else {
				pageLanguages.getCustomLanguageRecognizedIconLabel().setVisible(false);
				pageLanguages.getCustomLanguageWarningIconLabel().setVisible(true);
				pageLanguages.getCustomLanguageMessageLabel().setText("Warning: '"+entry+"' is not a recognized ISO code.");
				pageLanguages.getCustomLanguageMessageLabel().setVisible(true);
				pageLanguages.getCustomLanguageNameLabel().setVisible(true);
				pageLanguages.getCustomLanguageNameField().setVisible(true);
			}
		} else {
			selectedDefaultLanguage = null;
			pageLanguages.getCustomLanguageRecognizedIconLabel().setVisible(false);
			pageLanguages.getCustomLanguageWarningIconLabel().setVisible(false);
			pageLanguages.getCustomLanguageMessageLabel().setVisible(false);
			pageLanguages.getCustomLanguageNameLabel().setVisible(false);
			pageLanguages.getCustomLanguageNameField().setVisible(false);
		}
		validateStepTwoForm();
	}

	private void validateCustomLanguageNameField() {
		if(pageLanguages.getCustomLanguageNameField().getText().equals("")) {
			selectedDefaultLanguage = null;
		} else {
			selectedDefaultLanguage = new Language(pageLanguages.getCustomLanguageField().getText(),pageLanguages.getCustomLanguageNameField().getText(),null,null);
		}
		validateStepTwoForm();
	}

	private void createNewWoolProject() {

		WoolProjectMetaData metaData = WoolProjectFunctions.generateWoolProjectMetaData(projectName,
				projectLocation,
				projectFolder,
				pageExtras.getDescriptionField().getText(),
				pageExtras.getVersionField().getText(),
				selectedDefaultLanguage);

		try {
			WoolProjectFunctions.saveNewLocalWoolProject(metaData);
		} catch (IOException ioException) {
			System.err.println(ioException.getMessage());
			ioException.printStackTrace();
		}

		SwingUtilities.invokeLater(() -> {
			MainWindow window = new MainWindow(new ActiveWoolProject(metaData));
			window.setVisible(true);
		});

		view.getOwner().dispose();
		view.dispose();
	}
}