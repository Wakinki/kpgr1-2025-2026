package state;

import controller.Controller2D;
import fill.SeedFill;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class FillState extends DrawingState{

    public FillState(Controller2D ctrl) {
        super(ctrl);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        SeedFill seedFill = new SeedFill(
                ctrl.getPanel().getRaster(),
                ctrl.getPanel().getRaster().getPixel(e.getX(), e.getY()),
                e.getX(),
                e.getY()
        );
        seedFill.fill();
        ctrl.getPanel().repaint();

    }

    @Override
    public void onMouseReleased(MouseEvent e) {

    }

    @Override
    public void onMouseMoved(MouseEvent e) {

    }

    @Override
    public void onMouseDragged(MouseEvent e) {

    }

    @Override
    public void onKeyPressed(KeyEvent e) {

    }

    @Override
    public void onKeyReleased(KeyEvent e) {

    }

    @Override
    public void drawPreview() {

    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onExitState() {

    }
}
