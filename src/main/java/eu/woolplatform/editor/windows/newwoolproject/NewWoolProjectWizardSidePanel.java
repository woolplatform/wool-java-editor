package eu.woolplatform.editor.windows.newwoolproject;

import javax.swing.*;
import java.awt.*;

public class NewWoolProjectWizardSidePanel extends JPanel {

	public NewWoolProjectWizardSidePanel(int step) {
		super();
		SpringLayout sidePanelLayout = new SpringLayout();
		this.setLayout(sidePanelLayout);
		this.setPreferredSize(new Dimension(150,400));

		// Add the "WOOL Wizard" Image
		ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/main/resources/wool-wizard.png").getImage().getScaledInstance(140, 192, Image.SCALE_DEFAULT));
		JLabel woolWizardLabel = new JLabel();
		woolWizardLabel.setIcon(imageIcon);
		this.add(woolWizardLabel);

		sidePanelLayout.putConstraint(SpringLayout.SOUTH,woolWizardLabel,-10,SpringLayout.SOUTH,this);
		sidePanelLayout.putConstraint(SpringLayout.WEST,woolWizardLabel,5,SpringLayout.WEST,this);
		sidePanelLayout.putConstraint(SpringLayout.EAST,woolWizardLabel,-5,SpringLayout.EAST,this);

		JLabel stepOneLabel = new JLabel("1. Basics");
		sidePanelLayout.putConstraint(SpringLayout.NORTH,stepOneLabel,15,SpringLayout.NORTH,this);
		sidePanelLayout.putConstraint(SpringLayout.WEST,stepOneLabel,25,SpringLayout.WEST,this);
		this.add(stepOneLabel);

		JLabel stepTwoLabel = new JLabel("2. Languages");
		sidePanelLayout.putConstraint(SpringLayout.NORTH,stepTwoLabel,5,SpringLayout.SOUTH,stepOneLabel);
		sidePanelLayout.putConstraint(SpringLayout.WEST,stepTwoLabel,25,SpringLayout.WEST,this);
		this.add(stepTwoLabel);

		JLabel stepThreeLabel = new JLabel("3. Extras");
		sidePanelLayout.putConstraint(SpringLayout.NORTH,stepThreeLabel,5,SpringLayout.SOUTH,stepTwoLabel);
		sidePanelLayout.putConstraint(SpringLayout.WEST,stepThreeLabel,25,SpringLayout.WEST,this);
		this.add(stepThreeLabel);

		Font f = stepOneLabel.getFont();

		if(step == 1) {
			stepOneLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		} else if(step == 2) {
			stepTwoLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		} else if(step == 3) {
			stepThreeLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
		}

	}

}
