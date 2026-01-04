package state;

import clip.Clipper;
import controller.Controller2D;
import model.Line;
import model.Point;
import model.Polygon;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ClipState extends DrawingState {

    private int clipSize = 100;
    private Clipper clipper = new Clipper();
    private Polygon clipPath = createDefaultClipperPolygon(new Point(ctrl.getPanel().getRaster().getWidth()/2, ctrl.getPanel().getRaster().getHeight()/2), clipSize);
    private boolean isEdit = false;
    protected Point previewPoint = null;

    public ClipState(Controller2D ctrl) {
        super(ctrl);
    }

    @Override
    public void onMousePressed(MouseEvent e) {
        if(ctrl.getPolygons().isEmpty()){
            return;
        }
        if(isEdit && e.getButton() == MouseEvent.BUTTON1){
            isEdit = false;
            return;
        }

        Polygon lastPolygon = ctrl.getPolygons().removeLast();
        Polygon clippedLastPolygon = clipper.clip(lastPolygon, clipPath);
        ctrl.getPolygons().add(clippedLastPolygon);
    }

    @Override
    public void onMouseMoved(MouseEvent e) {
        if (isEdit) {
            clipSize =  Math.abs((int) Math.round(new Line(previewPoint, new Point(e.getX(), e.getY())).getLenght()*2));
        } else {
            previewPoint = new Point(e.getX(), e.getY());
        }
        clipPath = createDefaultClipperPolygon(previewPoint, clipSize);
        ctrl.drawScene();
    }

    @Override
    public void drawPreview() {
        ctrl.getPolygonRasterizer().rasterize(clipPath);
    }

    @Override
    public void onEnterState() {
        isEdit = false;
    }

    @Override
    public void onExitState() {
        isEdit = false;
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_E -> isEdit = !isEdit;
            case KeyEvent.VK_ENTER ->
                isEdit = false;
        }

    }

    /**
     * Creates a regular pentagon centered at (center),
     * with a size of size
     */
    private static Polygon createDefaultClipperPolygon(
            Point center, int size
    ) {
        double radius = size / 2.0;

        Polygon clip = new Polygon();

        for (int i = 0; i < 5; i++) {
            double ang = -Math.PI / 2 + i * (2 * Math.PI / 5.0); // CCW

            int x = (int) Math.round(center.getX() + radius * Math.cos(ang));
            int y = (int) Math.round(center.getY() + radius * Math.sin(ang));

            clip.addPoint(new Point(x, y));
        }

        return clip;
    }
}
