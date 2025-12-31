package state;

import controller.Controller2D;
import model.Point;
import model.Polygon;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;


public class PolygonState extends DrawingState {

    protected Polygon polygon = new Polygon();
    protected Point previewPoint = null;

    public PolygonState(Controller2D ctrl) {
        super(ctrl);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        polygon.addPoint(new Point(previewPoint.getX(), previewPoint.getY()));
        ctrl.getPolygons().add(polygon);
        ctrl.drawScene();
    }

    @Override
    public void onMouseMoved(MouseEvent e) {
        previewPoint = new Point(e.getX(), e.getY());
        ctrl.drawScene();
    }

    @Override
    public void drawPreview() {
        if (polygon.getSize() > 0 && previewPoint != null) {
            Point last = polygon.getLastPoint();
            if (ctrl.isShiftDown()){
                int[] snapped = ctrl.getSnappedPoint(last.getX(), last.getY(), previewPoint.getX(), previewPoint.getY());
                previewPoint = new Point(snapped[0], snapped[1]);
            }
            ctrl.getLineRasterizer().rasterize(last.getX(), last.getY(), previewPoint.getX(), previewPoint.getY());
        }
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && polygon.getSize() >= 3) {
            ctrl.getPolygons().add(polygon);
            polygon = new Polygon();
            previewPoint = null;
            ctrl.drawScene();
        }
    }

    @Override public void onMouseReleased(MouseEvent e) {}
    @Override public void onMouseDragged(MouseEvent e) {}
    @Override public void onKeyReleased(KeyEvent e) {}

    @Override public void onEnterState() {}
    @Override public void onExitState() { polygon = new Polygon(); previewPoint = null; }
}