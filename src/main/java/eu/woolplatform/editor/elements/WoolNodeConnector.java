package eu.woolplatform.editor.elements;

import org.piccolo2d.extras.nodes.PComposite;
import org.piccolo2d.nodes.PPath;

import java.awt.geom.Point2D;

public class WoolNodeConnector {

	private WoolNodePanel origin;
	private WoolNodePanel target;
	private PPath edge;
	private PPath arrowHead;
	private PComposite arrow;

	public WoolNodeConnector(WoolNodePanel origin, WoolNodePanel target) {
		this.origin = origin;
		this.target = target;
		arrow = new PComposite();
		// TODO: Add an arrow-head to the connectors to indicate the link direction.
		edge = PPath.createLine(0,0,0,0);
	}

	public void updateConnector() {
		Point2D start = origin.getFullBoundsReference().getCenter2D();
		Point2D end = target.getFullBoundsReference().getCenter2D();
		edge.reset();
		edge.moveTo((float)start.getX(), (float)start.getY());
		edge.lineTo((float)end.getX(), (float)end.getY());
		arrow.addChild(edge);
	}

	public WoolNodePanel getOrigin() {
		return origin;
	}

	public WoolNodePanel getTarget() {
		return target;
	}

	public PPath getEdge() {
		return edge;
	}

	public PComposite getArrow() {
		return arrow;
	}

}
