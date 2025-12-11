package state;

import controller.Controller2D;
import model.Line;
import model.Point;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class LineState implements DrawingState {
    private final Controller2D ctrl;
    private boolean isDrawing = false;
    private Point start;
    private Point current;

    public LineState(Controller2D ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        start = new Point(e.getX(), e.getY());
        current = start;
        isDrawing = true;
    }

    @Override
    public void onMouseReleased(MouseEvent e) {
        if (isDrawing) {
            ctrl.getLines().add(new Line(start, new Point(current.getX(), current.getY())));
        }

        isDrawing = false;
        ctrl.drawScene();
    }

    @Override
    public void onMouseMoved(MouseEvent e) {

    }


    @Override
    public void onMouseDragged(MouseEvent e) {
        if (isDrawing) {

            current = new Point(e.getX(), e.getY());
            if (ctrl.isShiftDown()){
                int[] snapped = ctrl.getSnappedPoint(start.getX(), start.getY(), current.getX(), current.getY());
                current = new Point(snapped[0], snapped[1]);
            }
            ctrl.drawScene();
        }
    }

    @Override
    public void drawPreview() {
        if (isDrawing) {
            ctrl.getLineRasterizer().rasterize(start.getX(), start.getY(), current.getX(), current.getY());
        }
    }

    @Override public void onEnterState() {}
    @Override public void onExitState() { isDrawing = false; }
    @Override public void onKeyPressed(KeyEvent e) {}
    @Override public void onKeyReleased(KeyEvent e) {}
}
