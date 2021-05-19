package eu.woolplatform.editor.elements;

import eu.woolplatform.editor.script.model.InternalNodePointer;
import eu.woolplatform.editor.script.model.NodePointer;
import eu.woolplatform.editor.script.model.WoolScriptNode;
import org.piccolo2d.PNode;
import org.piccolo2d.extras.nodes.PComposite;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class WoolNodePanel extends PComposite {

	private static final int NODE_WIDTH = 200;
	private static final int NODE_HEIGHT = 200;

	private WoolScriptNode node;
	private ScriptEditorPanel scriptEditorPanel;
	private boolean selected;

	private PNode frame;
	private PNode headerSeparator;
	private PText nodeTitle;
	private List<WoolNodeConnector> connectors;

	public WoolNodePanel(WoolScriptNode node, ScriptEditorPanel scriptEditorPanel) {
		super();
		this.node = node;
		this.scriptEditorPanel = scriptEditorPanel;
		this.connectors = new ArrayList<WoolNodeConnector>();

		init();
	}

	public void init() {
		frame = PPath.createRectangle(node.getHeader().getX(), node.getHeader().getY(), NODE_WIDTH, NODE_HEIGHT);
		frame.setPaint(Color.LIGHT_GRAY);

		headerSeparator = PPath.createLine(0,30,NODE_WIDTH,30);
		translateToFrame(headerSeparator);
		headerSeparator.setPaint(Color.BLACK);

		nodeTitle = new PText(node.getHeader().getTitle());
		nodeTitle.setFont(new Font("Roboto Condensed", Font.BOLD, 18));
		nodeTitle.setBounds(5,5,NODE_WIDTH,30);
		translateToFrame(nodeTitle);

		this.addChild(frame);
		this.addChild(headerSeparator);
		this.addChild(nodeTitle);

		this.setBounds(this.node.getHeader().getX(),
				this.node.getHeader().getY(),
				getNodeWidth(),
				getNodeHeight());
		this.setPaint(Color.BLUE);
	}

	public void initConnectors() {
		List<NodePointer> referencedNodes = node.getReferencedNodes();

		for(NodePointer pointer : referencedNodes) {
			if(pointer instanceof InternalNodePointer) {
				WoolNodePanel referencedPanel = scriptEditorPanel.getWoolNodePanel(pointer.getTargetNode());
				if(referencedPanel != null) {
					WoolNodeConnector connector = new WoolNodeConnector(this,referencedPanel);
					scriptEditorPanel.getConnectorLayer().addChild(connector.getArrow());
					connectors.add(connector);
				}
			}
		}
	}

	private void translateToFrame(PNode node) {
		node.translate(this.node.getHeader().getX(), this.node.getHeader().getY());
	}

	public int getNodeWidth() { return NODE_WIDTH; }
	public int getNodeHeight() { return NODE_HEIGHT; }
	public WoolScriptNode getWoolScriptNode() { return node; }
	public ScriptEditorPanel getScriptEditorPanel() { return scriptEditorPanel; }
	public List<WoolNodeConnector> getConnectors() { return connectors; }

	public void setSelected(boolean selected) {
		this.selected = selected;
		if(selected) {
			frame.setPaint(Color.GREEN);
		} else {
			frame.setPaint(Color.LIGHT_GRAY);
		}
	}

	public void setPositionChanged() {
		node.getHeader().setPosition((int)this.getGlobalBounds().getX(),(int)this.getGlobalBounds().getY());
		node.setModified(true);
	}

	public void updateConnectors() {
		for(WoolNodeConnector connector : connectors) {
			connector.updateConnector();
		}
	}

	public List<WoolNodeConnector> getRelatedConnectors(WoolNodePanel relatedPanel) {
		List<WoolNodeConnector> result = new ArrayList<WoolNodeConnector>();
		for(WoolNodeConnector connector : connectors) {
			if(connector.getOrigin().equals(relatedPanel)) result.add(connector);
			else if(connector.getTarget().equals(relatedPanel)) result.add(connector);
		}
		return result;
	}

}
