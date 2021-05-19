package eu.woolplatform.editor.elements;

import eu.woolplatform.editor.WoolEditorSettings;
import eu.woolplatform.editor.WoolEditorUITools;
import eu.woolplatform.editor.exceptions.DuplicateOptionalTagException;
import eu.woolplatform.editor.windows.editwoolnode.AddTagWindow;
import eu.woolplatform.editor.windows.editwoolnode.EditWoolNodeWindow;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TagsPanel extends JPanel {

	private EditWoolNodeWindow parent;
	private JPanel tagsHolderPanel;
	private List<TagButton> tagButtons;
	private boolean showReservedTags;
	private List<String> reservedTags = Arrays.asList(WoolEditorSettings.RESERVED_TAGS);

	public TagsPanel(EditWoolNodeWindow parent) {
		super();

		this.parent = parent;
		this.tagButtons = new ArrayList<TagButton>();
		this.showReservedTags = parent.getMainWindow().getWoolEditorSettings().getShowReservedTags();

		SpringLayout tagsPanelLayout = new SpringLayout();
		this.setLayout(tagsPanelLayout);

		JLabel tagsLabel = new JLabel("Tags:");
		WoolEditorUITools.setBold(tagsLabel);
		tagsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		tagsLabel.setPreferredSize(new Dimension(50,20));
		this.add(tagsLabel);
		tagsPanelLayout.putConstraint(SpringLayout.NORTH,tagsLabel,0,SpringLayout.NORTH,this);
		tagsPanelLayout.putConstraint(SpringLayout.WEST,tagsLabel,0,SpringLayout.WEST,this);

		// ----- Circle "Add Tag" Button
		CircleButton addTagButton = new CircleButton("+");
		addTagButton.setPreferredSize(new Dimension(30,30));
		addTagButton.setFont(new Font("Arial", Font.BOLD, 30));
		addTagButton.addActionListener(e->actionAddTag());
		this.add(addTagButton);
		tagsPanelLayout.putConstraint(SpringLayout.NORTH,addTagButton,0,SpringLayout.NORTH,this);
		tagsPanelLayout.putConstraint(SpringLayout.SOUTH,addTagButton,5,SpringLayout.SOUTH,this);
		tagsPanelLayout.putConstraint(SpringLayout.EAST,addTagButton,0,SpringLayout.EAST,this);

		tagsHolderPanel = new JPanel();
		JScrollPane scrollPane = new JScrollPane(tagsHolderPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		tagsPanelLayout.putConstraint(SpringLayout.NORTH,scrollPane,5,SpringLayout.NORTH,this);
		tagsPanelLayout.putConstraint(SpringLayout.WEST,scrollPane,5,SpringLayout.EAST,tagsLabel);
		tagsPanelLayout.putConstraint(SpringLayout.EAST,scrollPane,-5,SpringLayout.WEST,addTagButton);
		tagsPanelLayout.putConstraint(SpringLayout.SOUTH,scrollPane,-5,SpringLayout.SOUTH,this);
		this.add(scrollPane);

		populateTags();
	}

	/**
	 * Action Performed when the "Add Tag" button is pressed.
	 */
	private void actionAddTag() {
		new AddTagWindow(this);
	}

	public void addNewTag(String name, String value) throws DuplicateOptionalTagException {
		if(reservedTags.contains(name.toLowerCase())) {
			throw new DuplicateOptionalTagException("The tag name '"+name+"' is reserved for use in the WOOL Editor, please choose a different tag name.");
		} else {
			for(TagButton tagButton : tagButtons) {
				if(tagButton.getTagName().equalsIgnoreCase(name)) {
					throw new DuplicateOptionalTagException("The tag name '"+name+"' is already in use, please choose a different tag name.");
				}
			}
		}

		TagButton tagButton = new TagButton(name,value,false);
		tagsHolderPanel.add(tagButton);
		tagButtons.add(tagButton);
		tagsHolderPanel.repaint();
		tagsHolderPanel.revalidate();
		parent.setNodeModified();
	}

	private void populateTags() {
		// Add all existing optional tags to the tagsHolderPanel
		Map<String,String> optionalTags = parent.getWoolNodePanel().getWoolScriptNode().getHeader().getOptionalTags();
		optionalTags.forEach(this::populateTagsHelper);
		tagsHolderPanel.repaint();
		tagsHolderPanel.revalidate();
	}

	private void populateTagsHelper(String name, String value) {
		boolean isReservedTag = false;

		if(reservedTags.contains(name.toLowerCase())) {
			isReservedTag = true;
		}

		TagButton tagButton = new TagButton(name, value, isReservedTag);

		if(isReservedTag && !showReservedTags) {
			tagButton.setEnabled(false);
			tagButton.setBackground(Color.CYAN);
		}

		tagsHolderPanel.add(tagButton);
		tagButtons.add(tagButton);
	}

	/**
	 * Returns a Map of String-pairs representing the current set of optional
	 * tags present in this {@link TagsPanel}.
	 * @return
	 */
	public Map<String,String> getOptionalTagsMap() {
		Map<String,String> result = new HashMap<String,String>();
		for(TagButton tagButton : tagButtons) {
			result.put(tagButton.getTagName(),tagButton.getTagValue());
		}
		return result;
	}
}
