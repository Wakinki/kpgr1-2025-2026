package state;

import controller.Controller2D;
import model.Point;
import model.Polygon;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class RectangleState extends PolygonState{


    public RectangleState(Controller2D ctrl) {
        super(ctrl);
    }

    @Override
    public void onMousePressed(MouseEvent e) {

        switch (polygon.getSize()){
            case 0: {
                polygon.addPoint(new Point(previewPoint.getX(), previewPoint.getY()));
                break;
            }
            case 1: {
                int[] snapped = ctrl.getSnappedPoint(polygon.getFirstPoint().getX(), polygon.getFirstPoint().getY(), previewPoint.getX(), previewPoint.getY());
                polygon.addPoint(new Point(snapped[0], snapped[1]));

                break;
            }

            default: {
                polygon.addPoint(new Point(polygon.getLastPoint().getX(), previewPoint.getY()));
                polygon.addPoint(new Point(polygon.getFirstPoint().getX(), previewPoint.getY()));


                polygon = new Polygon();
                previewPoint = null;
            }
        }




        ctrl.getPolygons().add(polygon);
        ctrl.drawScene();
    }

    @Override
    public void onMouseMoved(MouseEvent e) {

//        if(polygon.getSize() < 3) {
//
//        }
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
//        if (e.getKeyCode() == KeyEvent.VK_ENTER && polygon.getSize() >= 3) {
//            ctrl.getPolygons().add(polygon);
//            polygon = new Polygon();
//            previewPoint = null;
//            ctrl.drawScene();
//        }
    }

}
