package model;

import exceptions.RectangleSizeException;

import java.util.ArrayList;

public class Rectangle extends Polygon{

    public Rectangle(){
        this.points = new ArrayList<>();
    }

    @Override
    public void addPoint(Point p) {

        if(points.size() > 3){
            throw new RectangleSizeException("Too many points in rectangle array");
        }

        switch (points.size()) {
            case 0: {
                points.add(p);
                break;
            }
            case 1: {
                int[] snapped = getXSnappedPoint(points.getFirst().getX(), points.getFirst().getY(), p.getX(), p.getY());
                points.add(new Point(snapped[0], snapped[1]));
                break;
            }
            case 2: {
                points.add(new Point(points.getLast().getX(), p.getY()));
                points.add(new Point(points.getFirst().getX(), p.getY()));
                break;
            }


        }

    }

    /**
     * Spočítá kde má být konečný bod pro funkci zarovnání linky na předem definované úhle
     */
    public int[] getXSnappedPoint(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;

        double angle = Math.atan2(dy, dx);
        double distance = Math.sqrt(dx * dx + dy * dy);

        // zaokrouhlení úhlu na nejbližší čtvrtinu PI
        double snappedAngle = Math.round(angle / (Math.PI)) * (Math.PI);

        // špočítání konečného bodu pomocí zarovnaného úhlu
        int snappedX = x1 + (int) Math.round(distance * Math.cos(snappedAngle));
        int snappedY = y1 + (int) Math.round(distance * Math.sin(snappedAngle));

        return new int[]{snappedX, snappedY};
    }

}
