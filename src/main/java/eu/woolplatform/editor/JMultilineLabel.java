package eu.woolplatform.editor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Re-usable UI element for creating "labels" that can span across multiple
 * lines.
 */
public class JMultilineLabel extends JTextArea {
	private static final long serialVersionUID = 1L;
	public JMultilineLabel(String text){
		super(text);
		//super(text);
		//setEditable(false);
		//setCursor(null);
		//setOpaque(false);
		//setFocusable(false);
		setFont(UIManager.getFont("Label.font"));
		setWrapStyleWord(true);
		setLineWrap(true);
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setAlignmentY(JLabel.CENTER_ALIGNMENT);
		setBackground(Color.BLUE);
	}
}