package state;

import controller.Controller2D;
import model.Point;
import model.Polygon;
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


            previewPoint = new Point(e.getX(), e.getY());


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
            int[] snapped = rectangle.getXSnappedPoint(last.getX(), last.getY(), previewPoint.getX(), previewPoint.getY());
            ctrl.getLineRasterizer().rasterize(last.getX(), last.getY(), snapped[0], snapped[1]);
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
