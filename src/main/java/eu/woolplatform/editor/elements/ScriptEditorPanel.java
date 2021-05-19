package eu.woolplatform.editor.elements;

import eu.woolplatform.editor.MainWindow;
import eu.woolplatform.editor.script.model.WoolScript;
import eu.woolplatform.editor.script.model.WoolScriptNode;
import eu.woolplatform.editor.script.model.WoolScriptNodeBody;
import eu.woolplatform.editor.script.model.WoolScriptNodeHeader;
import org.piccolo2d.PCanvas;
import org.piccolo2d.PLayer;
import org.piccolo2d.PNode;
import org.piccolo2d.event.*;
import org.piccolo2d.extras.event.PNotification;
import org.piccolo2d.extras.event.PNotificationCenter;
import org.piccolo2d.extras.event.PSelectionEventHandler;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.util.PBounds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScriptEditorPanel extends PCanvas implements PropertyChangeListener {

	private WoolScript woolScript;
	private MainWindow mainWindow;
	private List<WoolNodePanel> nodePanels;
	private List<WoolNodePanel> selectedNodePanels;
	private PSelectionEventHandler selectionEventHandler;
	private PLayer nodeLayer;
	private PLayer connectorLayer;
	private WoolNodePanel startNodePanel;

	private int nextNodeNumber;

	public ScriptEditorPanel(WoolScript woolScript, MainWindow mainWindow) {
		this.woolScript = woolScript;
		this.mainWindow = mainWindow;
		this.nodePanels = new ArrayList<WoolNodePanel>();
		this.selectedNodePanels = new ArrayList<WoolNodePanel>();

		// Register ourselves to listen to changes in the WoolScript
		woolScript.addPropertyChangeListener(this);

		nodeLayer = this.getLayer();
		connectorLayer = new PLayer();
		this.getRoot().addChild(connectorLayer);
		getCamera().addLayer(0,connectorLayer);

		for(WoolScriptNode node : woolScript.getNodes()) {
			WoolNodePanel woolNodePanel = new WoolNodePanel(node, this);

			// TODO: Remove this maybe ... some playing around...
			if(node.getTitle().equals("Start")) startNodePanel = woolNodePanel;

			// Listen for when the node is being moved (to update connectors)
			woolNodePanel.addPropertyChangeListener(PNode.PROPERTY_TRANSFORM,
					new PropertyChangeListener() {
						public void propertyChange(final PropertyChangeEvent arg0) {
							woolNodePanel.setPositionChanged();
							updateConnectorsRelatedTo(woolNodePanel);
						}
					});

			// Add double-click event listener to nodes (to edit node)
			woolNodePanel.addInputEventListener(new EditNodeEventHandler(this,woolNodePanel));

			nodeLayer.addChild(woolNodePanel);
			nodePanels.add(woolNodePanel);
		}

		for(WoolNodePanel nodePanel : nodePanels) {
			nodePanel.getWoolScriptNode().shallowParse();
			nodePanel.initConnectors();
			nodePanel.updateConnectors();
		}

		// Set a different zoom event handler
		this.removeInputEventListener(this.getZoomEventHandler());
		final PMouseWheelZoomEventHandler mouseWheelZoomEventHandler = new PMouseWheelZoomEventHandler();
		mouseWheelZoomEventHandler.zoomAboutMouse();
		mouseWheelZoomEventHandler.setScaleFactor(mouseWheelZoomEventHandler.getScaleFactor() * -0.2d);
		this.addInputEventListener(mouseWheelZoomEventHandler);

		// Add a right-click button event handler to create new nodes
		CreateNewNodeEventHandler createNewNodeEventHandler = new CreateNewNodeEventHandler(this);
		createNewNodeEventHandler.setEventFilter(new PInputEventFilter(InputEvent.BUTTON3_MASK));
		this.addInputEventListener(createNewNodeEventHandler);

		// Have the pan event handler only work while ALT is held down
		PPanEventHandler panEventHandler = this.getPanEventHandler();
		PInputEventFilter panInputEventFilter = new PInputEventFilter();
		panInputEventFilter.setAndMask(InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK);
		panEventHandler.setEventFilter(panInputEventFilter);

		// Add a selection event handler
		selectionEventHandler = new PSelectionEventHandler(nodeLayer,nodeLayer);
		PInputEventFilter eventFilter = new PInputEventFilter();
		eventFilter.setNotMask(InputEvent.ALT_MASK);
		selectionEventHandler.setEventFilter(eventFilter);
		this.addInputEventListener(selectionEventHandler);
		this.getRoot().getDefaultInputManager().setKeyboardFocus(selectionEventHandler);
		PNotificationCenter.defaultCenter().addListener(this, "selectionChanged",
				PSelectionEventHandler.SELECTION_CHANGED_NOTIFICATION, selectionEventHandler);
	}

	public void centerOnStartNode() {
		nodeLayer.validateFullPaint();
		connectorLayer.validateFullPaint();


		// TODO: Camera experimentation, automatically center on the Start node?
		this.getCamera().validateFullPaint();

		System.out.println("Node Layer Width: "+nodeLayer.getBounds().getWidth());
		System.out.println("Node Layer Height: "+nodeLayer.getBounds().getHeight());
		System.out.println("This.getWidth: "+this.getWidth());
		System.out.println("This.getHeight: "+this.getHeight());

		//Rectangle2D initialViewPort = new Rectangle(50,50,100,100);
		PBounds currentViewBounds = getCamera().getViewBounds();
		System.out.println("Current camera x,y,w,h = "+currentViewBounds.getX()+","+currentViewBounds.getY()+","+currentViewBounds.getWidth()+","+currentViewBounds.getHeight());

		System.out.println("Start Node Panel x,y,w,h = "+startNodePanel.getX()+","+startNodePanel.getY()+","+startNodePanel.getWidth()+","+startNodePanel.getHeight());

		getCamera().animateViewToCenterBounds(new Rectangle.Double(startNodePanel.getX(),startNodePanel.getY(),startNodePanel.getWidth(),startNodePanel.getHeight()),true,4000);
		//getCamera().animateToBounds(startNodePanel.getX(),startNodePanel.getY(),startNodePanel.getWidth(),startNodePanel.getHeight(),2000);
		getCamera().animateViewToPanToBounds(new Rectangle.Double(startNodePanel.getX(),startNodePanel.getY(),startNodePanel.getWidth(),startNodePanel.getHeight()),2000);
		//getCamera().animateViewToCenterBounds(startNodePanel.getGlobalFullBounds(), true, 1000);
	}

	public void updateConnectorsRelatedTo(WoolNodePanel alteredPanel) {
		List<WoolNodeConnector> affectedConnectors = new ArrayList<WoolNodeConnector>();

		// Go through all WoolNodePanels
		for(WoolNodePanel panel : nodePanels) {
			// Find all connectors related to the altered panel
			affectedConnectors.addAll(panel.getRelatedConnectors(alteredPanel));
		}

		// Update all affected connectors
		for(WoolNodeConnector connector : affectedConnectors) {
			connector.updateConnector();
		}
	}

	public void selectionChanged(final PNotification notification) {
		Collection<WoolNodePanel> selectedNodes = selectionEventHandler.getSelection();
		List<WoolNodePanel> newSelectedNodePanels = new ArrayList<WoolNodePanel>();
		for(WoolNodePanel wnp : selectedNodes) {
			if(!selectedNodePanels.contains(wnp)) {
				wnp.setSelected(true);
			}
			newSelectedNodePanels.add(wnp);
		}
		for(WoolNodePanel wnp : selectedNodePanels) {
			if(!newSelectedNodePanels.contains(wnp)) {
				wnp.setSelected(false);
			}
		}
		selectedNodePanels = newSelectedNodePanels;
	}

	public void createNewWoolNode(Point2D location) {
		System.out.println("Create new WOOL Node at location: "+location.getX()+","+location.getY());
		WoolScriptNode woolScriptNode = new WoolScriptNode();
		WoolScriptNodeHeader header = new WoolScriptNodeHeader();
		header.setSpeaker("");
		header.setTitle("Node"+nextNodeNumber);
		nextNodeNumber++;
		header.addOptionalTag("position",""+(int)location.getX()+","+(int)location.getY());
		header.validatePosition();
		woolScriptNode.setHeader(header);
		woolScriptNode.setBody(new WoolScriptNodeBody(""));
		WoolNodePanel wnp = new WoolNodePanel(woolScriptNode, this);
		nodeLayer.addChild(wnp);
		nodePanels.add(wnp);
	}

	public WoolNodePanel getWoolNodePanel(String nodeTitle) {
		for(WoolNodePanel wnp : nodePanels) {
			if(wnp.getWoolScriptNode().getTitle().equals(nodeTitle)) {
				return wnp;
			}
		}
		return null;
	}

	// ----- Getters:

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public PLayer getNodeLayer() {
		return nodeLayer;
	}

	public PLayer getConnectorLayer() {
		return connectorLayer;
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(WoolScript.IS_MODIFIED)) {
			boolean modified = ((Boolean)event.getNewValue()).booleanValue();

			String tabTitle = woolScript.getDialogueName();
			if (modified)
				tabTitle += "*";

			JTabbedPane tabbedPane = (JTabbedPane) SwingUtilities.getAncestorOfClass(JTabbedPane.class, this);
			for (int i = 0; i < tabbedPane.getTabCount(); i++) {
				if (SwingUtilities.isDescendingFrom(this, tabbedPane.getComponentAt(i))) {
					tabbedPane.setTitleAt(i, tabTitle);
					break;
				}
			}
		}
	}
}
