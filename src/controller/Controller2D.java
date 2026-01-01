package controller;


import fill.Filler;
import model.Point;
import state.*;
import model.Line;
import model.Polygon;
import rasterize.*;
import view.Panel;

import java.awt.event.*;
import java.util.ArrayList;

public class Controller2D {
    private final Panel panel;
    private DrawingState currentState;
//    private int color = 0xffffff;

    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<Polygon> polygons = new ArrayList<>();
    private ArrayList<Filler> fills = new ArrayList<>();

    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;

    private boolean isShiftDown = false;

    public Controller2D(Panel panel) {
        this.panel = panel;

        //lineRasterizer = new LineRasterizerColorTransition(panel.getRaster());
        lineRasterizer = new LineRasterizerBresenham(panel.getRaster());
        polygonRasterizer = new PolygonRasterizer(lineRasterizer);

        setState(new PolygonState(this));

        initListeners();
    }

    public void setState(DrawingState newState) {
        if (currentState != null) {
            currentState.onExitState();
        }
        currentState = newState;
        currentState.onEnterState();
        drawScene();
    }

    public DrawingState getState() {
        return currentState;
    }
    public ArrayList<Line> getLines() { return lines; }
    public ArrayList<Polygon> getPolygons() { return polygons; }
    public ArrayList<Filler> getFills() {
        return fills;
    }
    public LineRasterizer getLineRasterizer() { return lineRasterizer; }
    public PolygonRasterizer getPolygonRasterizer() { return polygonRasterizer; }
    public Panel getPanel() { return panel; }

    public boolean isShiftDown() {
        return isShiftDown;
    }

    public void setMode(Mode mode) {
        switch (mode) {
            case LINE -> setState(new LineState(this));
            case POLYGON -> setState(currentState = new PolygonState(this));
            case FILL -> setState(currentState = new FillState(this));
            case RECTANGLE -> setState(currentState = new RectangleState(this));
        }
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { currentState.onMousePressed(e); }
            @Override public void mouseReleased(MouseEvent e) { currentState.onMouseReleased(e); }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) { currentState.onMouseMoved(e); }
            @Override public void mouseDragged(MouseEvent e) { currentState.onMouseDragged(e); }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) { currentState.onKeyPressed(e);

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_L -> setMode(Mode.LINE);
                    case KeyEvent.VK_P -> setMode(Mode.POLYGON);
                    case KeyEvent.VK_F -> setMode(Mode.FILL);
                    case KeyEvent.VK_R -> setMode(Mode.RECTANGLE);
                    case KeyEvent.VK_C -> {
                        lines.clear();
                        polygons.clear();
                        currentState.onExitState();
                        drawScene();
                    }
                    case KeyEvent.VK_SHIFT -> isShiftDown = true;
                }
            }

            @Override public void keyReleased(KeyEvent e) { currentState.onKeyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    isShiftDown = false;
                }
            }


        });
    }

    public void drawScene() {
        panel.getRaster().clear();

        // rasterizace uložených dat
        for (Line l : lines) {
            lineRasterizer.rasterize(l);
        }

        for (Polygon p : polygons) {
            polygonRasterizer.rasterize(p);
        }

        for (Filler f : fills) {
            f.fill();
        }


        currentState.drawPreview();

        panel.repaint();
    }


    /**
     * Spočítá kde má být konečný bod pro funkci zarovnání linky na předem definované úhle
     */
    public int[] getSnappedPoint(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        double angle = Math.atan2(dy, dx);
        double distance = Math.sqrt(dx * dx + dy * dy);

        // zaokrouhlení úhlu na nejbližší čtvrtinu PI
        double snappedAngle = Math.round(angle / (Math.PI / 4)) * (Math.PI / 4);

        // špočítání konečného bodu pomocí zarovnaného úhlu
        int snappedX = x1 + (int) Math.round(distance * Math.cos(snappedAngle));
        int snappedY = y1 + (int) Math.round(distance * Math.sin(snappedAngle));

        return new int[]{snappedX, snappedY};
    }
}
