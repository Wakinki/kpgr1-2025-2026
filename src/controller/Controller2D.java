package controller;


import model.Line;
import model.Point;
import model.Polygon;
import rasterize.*;
import view.Panel;
import java.awt.Color;

import java.awt.event.*;
import java.util.ArrayList;

public class Controller2D {
    private final Panel panel;
    private int color = 0xffffff;

    private LineRasterizer lineRasterizer;

    // To draw
    private Polygon polygon = new Polygon();

    // Ukázka zapamatování si seznamu úseček
    private ArrayList<Line> lines = new ArrayList<>();;
    private int startX, startY;
    private int currentX, currentY;
    private boolean isLineStartSet;
    private boolean isDrawingPreview = false;
    private boolean isShiftDown = false;

    public Controller2D(Panel panel) {
        this.panel = panel;


        //lineRasterizer = new LineRasterizerColorTransition(panel.getRaster());
        lineRasterizer = new LineRasterizerBresenham(panel.getRaster());

        initListeners();
    }

    private void initListeners() {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
//             Ukázka zapamatování si seznamu úseček
                if (!isLineStartSet) {
                    startX = e.getX();
                    startY = e.getY();
                    currentX = startX;
                    currentY = startY;
                    isLineStartSet = true;
                    isDrawingPreview = true;
                }else {
                    isLineStartSet = false;
                    isDrawingPreview = false;
                    Line line = new Line(startX, startY, currentX, currentY);
                    lines.add(line);
//                polygon.addPoint(new Point(e.getX(), e.getY()));
                    drawScene();
                }
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                if (isDrawingPreview) {
                    currentX = e.getX();
                    currentY = e.getY();

                    if (isShiftDown) {
                        //Aplikace zarovnání
                        int[] snapped = getSnappedPoint(startX, startY, currentX, currentY);
                        currentX = snapped[0];
                        currentY = snapped[1];
                    }

                    drawScene();
                }
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SHIFT -> isShiftDown = true;
                    case KeyEvent.VK_C -> {
                        panel.getRaster().clear();
                        lines.clear();
                        panel.repaint();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT)
                    isShiftDown = false;
            }
        });

    }

    private void drawScene() {
        panel.getRaster().clear();
//      Ukázka zapamatování si seznamu úseček
        for(Line line : lines){
            lineRasterizer.rasterize(line);
        }

        if (isDrawingPreview){
            lineRasterizer.rasterize(new Line(startX, startY, currentX, currentY));
        }

        panel.repaint();
    }

    /**
     * Spočítá kde má být konečný bod pro funkci zarovnání linky na předem definované úhle
     */
    private int[] getSnappedPoint(int x1, int y1, int x2, int y2) {
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
