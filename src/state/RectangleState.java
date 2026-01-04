package state;

import controller.Controller2D;
import model.Point;
import model.Rectangle;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class RectangleState extends DrawingState{

    private Rectangle rectangle = new Rectangle();
    protected Point previewPoint = null;

    public RectangleState(Controller2D ctrl) {
        super(ctrl);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if(rectangle.getSize() > 0 && !ctrl.getPolygons().isEmpty()) {
            ctrl.getPolygons().removeLast();
        }


        if(rectangle.getSize() >= 3) {
            rectangle = new Rectangle();
            previewPoint = null;
            ctrl.getPolygons().add(rectangle);
            ctrl.drawScene();
            return;
        }

        rectangle.addPoint(new Point(previewPoint.getX(), previewPoint.getY()));

        ctrl.getPolygons().add(rectangle);
        ctrl.drawScene();
    }

    @Override
    public void onMouseMoved(MouseEvent e) {


        if(rectangle.getSize() < 3) {
            previewPoint = new Point(e.getX(), e.getY());
        }
        ctrl.drawScene();
    }

    @Override
    public void drawPreview() {
        if(previewPoint == null) {
            return;
        }
        //Vykreslení náhledu základny
        if (rectangle.getSize() == 1 ) {
            Point last = rectangle.getLastPoint();
            Point snapped = rectangle.getXSnappedPoint(last, previewPoint);
            ctrl.getLineRasterizer().rasterize(last, snapped);
        }
        //Vykreslení náhledu obdelníka
        if (rectangle.getSize() == 2) {
            Rectangle previewRectangle = new Rectangle(rectangle);
            previewRectangle.addPoint(previewPoint);
            ctrl.getPolygonRasterizer().rasterize(previewRectangle);
        }
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
//        if (e.getKeyCode() == KeyEvent.VK_ENTER && polygon.getSize() >= 3) {
//            ctrl.getPolygons().add(polygon);
//            polygon = new Polygon();
//            previewPoint = null;
//            ctrl.drawScene();
//        }
    }

}
