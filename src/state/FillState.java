package state;

import controller.Controller2D;
import fill.ScanLine;
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
        ctrl.getFills().add(seedFill);
        seedFill.fill();
        ctrl.getPanel().repaint();

    }


    @Override
    public void onKeyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            ctrl.getPolygons().getLast().setFill(new ScanLine(ctrl.getPanel().getRaster(), ctrl.getPolygons().getLast(), 0x00ff00, ctrl.getPolygonRasterizer()));
            ctrl.drawScene();
        }
    }

    @Override
    public void onKeyReleased(KeyEvent e) {

    }

    @Override
    public void onEnterState() {

    }

    @Override
    public void onExitState() {

    }
}
