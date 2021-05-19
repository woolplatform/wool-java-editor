package eu.woolplatform.editor.elements;

import eu.woolplatform.editor.WoolEditorUITools;
import eu.woolplatform.editor.functions.WoolProjectFunctions;
import eu.woolplatform.editor.script.model.WoolScript;
import eu.woolplatform.editor.script.parser.WoolScriptParserResult;
import eu.woolplatform.wool.model.language.WoolLanguage;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class SideBarLanguagePanel extends JPanel {

	private SideBarLanguageSetPanel parent;
	private WoolLanguage language;
	private boolean isSourceLanguage;
	private JPanel headerPanel;
	private JTree fileTree;

	public SideBarLanguagePanel(SideBarLanguageSetPanel parent, WoolLanguage language, boolean isSourceLanguage) {
		super();
		this.parent = parent;
		this.language = language;
		this.isSourceLanguage = isSourceLanguage;

		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);

		// Create the header panel (language name, configuration button and type label)
		headerPanel = new JPanel();
		this.add(headerPanel);
		SpringLayout headerPanelLayout = new SpringLayout();
		headerPanel.setLayout(headerPanelLayout);
		headerPanel.setMinimumSize(new Dimension(200,40));
		headerPanel.setPreferredSize(new Dimension(200,40));
		headerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		layout.putConstraint(SpringLayout.NORTH, headerPanel, 0, SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST, headerPanel, 0, SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.EAST, headerPanel, 0, SpringLayout.EAST,this);

		// Create the language name label and add to the header panel
		JLabel nameLabel = new JLabel(language.getName());
		WoolEditorUITools.setSmallTitleFont(nameLabel);
		nameLabel.setOpaque(true);
		headerPanel.add(nameLabel);
		WoolEditorUITools.setBold(nameLabel);
		headerPanelLayout.putConstraint(SpringLayout.NORTH, nameLabel, 5, SpringLayout.NORTH,headerPanel);
		headerPanelLayout.putConstraint(SpringLayout.SOUTH, nameLabel, -5, SpringLayout.SOUTH,headerPanel);
		headerPanelLayout.putConstraint(SpringLayout.WEST, nameLabel, 25, SpringLayout.WEST,headerPanel);

		// TODO: Don't use a button for the label that indicates whether this is a "Source" or "Translation" language panel.
		// Create the type label (temporarily as a button)
		String typeLabelString = null;
		if(isSourceLanguage) typeLabelString = "S";
		else typeLabelString = "T";
		JButton typeButton = new JButton(typeLabelString);
		headerPanel.add(typeButton);
		typeButton.setPreferredSize(new Dimension(24,0));
		typeButton.setMinimumSize(new Dimension(24,0));
		headerPanelLayout.putConstraint(SpringLayout.NORTH,typeButton,5,SpringLayout.NORTH,headerPanel);
		headerPanelLayout.putConstraint(SpringLayout.EAST,typeButton,-5,SpringLayout.EAST,headerPanel);
		headerPanelLayout.putConstraint(SpringLayout.SOUTH,typeButton,-5,SpringLayout.SOUTH,headerPanel);

		JButton settingsButton = new JButton("C");
		headerPanel.add(settingsButton);
		settingsButton.setPreferredSize(new Dimension(24,0));
		settingsButton.setMinimumSize(new Dimension(24,0));
		headerPanelLayout.putConstraint(SpringLayout.NORTH,settingsButton,5,SpringLayout.NORTH,headerPanel);
		headerPanelLayout.putConstraint(SpringLayout.EAST,settingsButton,-5,SpringLayout.WEST,typeButton);
		headerPanelLayout.putConstraint(SpringLayout.SOUTH,settingsButton,-5,SpringLayout.SOUTH,headerPanel);

		String directoryPath = parent.getParent().getActiveWoolProject().getWoolProjectMetaData().getBasePath();
		if(directoryPath.endsWith(File.separator)) {
			directoryPath += language.getCode();
		} else {
			directoryPath += File.separator + language.getCode();
		}
		File directoryRoot = new File(directoryPath);

		// Create the root node of the tree
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(directoryRoot);

		for(File f : directoryRoot.listFiles()) {
			System.out.println("listFiles() entry for language'"+language.getCode()+"':"+f.getAbsolutePath());
		}

		createNodes(top,directoryRoot);

		fileTree = new JTree(top);
		fileTree.setBorder(BorderFactory.createLineBorder(Color.black));
		fileTree.setCellRenderer(new FileTreeCellRenderer());

		// Add a mouse listener to detect double-click events.
		MouseListener ml = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int selRow = fileTree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = fileTree.getPathForLocation(e.getX(), e.getY());
				if(selRow != -1) {
					if(e.getClickCount() == 2) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
						File file = (File)(node.getUserObject());
						if(file.isFile() && file.getName().endsWith(WoolProjectFunctions.WOOL_SCRIPT_EXTENSION)) {
							WoolScriptParserResult woolScriptParserResult = null;
							try {
								woolScriptParserResult = WoolProjectFunctions.loadWoolScript(file);
							} catch (IOException ioException) {
								ioException.printStackTrace();
							}
							parent.getParent().getMainWindow().getScriptEditor().openDialogueScript(woolScriptParserResult.getWoolScript());
						}
					}
				}
			}
		};
		fileTree.addMouseListener(ml);

		this.add(fileTree);
		layout.putConstraint(SpringLayout.NORTH,fileTree,2,SpringLayout.SOUTH,headerPanel);
		layout.putConstraint(SpringLayout.EAST,fileTree,0,SpringLayout.EAST,this);
		layout.putConstraint(SpringLayout.WEST,fileTree,0,SpringLayout.WEST,this);

		TreeExpansionListener treeExpansionListener = new TreeExpansionListener() {

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				calculateAndSetPreferredSize();
			}

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				calculateAndSetPreferredSize();
			}

		};

		fileTree.addTreeExpansionListener(treeExpansionListener);

		calculateAndSetPreferredSize();

		revalidate();
	}

	public JPanel getHeaderPanel() {
		return headerPanel;
	}

	private void calculateAndSetPreferredSize() {
		int preferredHeight = 2; // padding between header and file tree
		preferredHeight += headerPanel.getPreferredSize().getHeight();
		preferredHeight += fileTree.getPreferredSize().getHeight();

		this.setPreferredSize(new Dimension(SideBar.PREFERRED_WIDTH, preferredHeight));
		revalidate();
		repaint();
		parent.calculateAndSetPreferredSize();
	}

	private void createNodes(DefaultMutableTreeNode root, File folder) {
		for (final File fileEntry : getSortedFileList(folder)) {
			if (fileEntry.isDirectory()) {
				DefaultMutableTreeNode treeFolderEntry = new DefaultMutableTreeNode(fileEntry,true);
				root.add(treeFolderEntry);
				createNodes(treeFolderEntry,fileEntry);
			} else {
				if(isSourceLanguage && fileEntry.getName().endsWith(".wool") || !isSourceLanguage && fileEntry.getName().endsWith(".json")) {
					DefaultMutableTreeNode treeFileEntry = new DefaultMutableTreeNode(fileEntry,false);
					root.add(treeFileEntry);
				}
			}
		}
	}

	/**
	 * Returns a sorted list of direct subfiles/directories from the given File.
	 * @param file
	 * @return
	 */
	private File[] getSortedFileList(File file) {
		File[] fileList = file.listFiles();

		Arrays.sort(fileList, new Comparator<File>() {
			public int compare(File o1, File o2) {
				if(o1.isDirectory()) {
					if(o2.isDirectory()) {
						return o1.compareTo(o2);
					} else {
						return -1;
					}
				} else {
					if(o2.isDirectory()) {
						return (int)1;
					} else {
						return o1.compareTo(o2);
					}
				}
			}
		});

		return fileList;
	}

	private class FileTreeCellRenderer extends DefaultTreeCellRenderer {

		public Component getTreeCellRendererComponent(JTree tree,
													  Object value, boolean sel, boolean expanded, boolean leaf,
													  int row, boolean hasFocus) {
			JLabel renderer = (JLabel)super.getTreeCellRendererComponent(
					tree, value, sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			File currentFileEntry = (File)node.getUserObject();
			renderer.setText(currentFileEntry.getName());
			if (currentFileEntry.isDirectory()) {
				if (expanded) {
					renderer.setIcon(openIcon);
				}
				else {
					renderer.setIcon(closedIcon);
				}
			}
			else {
				renderer.setIcon(leafIcon);
			}
			return renderer;
		}
	}

}
