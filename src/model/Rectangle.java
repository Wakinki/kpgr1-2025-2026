package model;

import exceptions.RectangleSizeException;

import java.util.ArrayList;

public class Rectangle extends Polygon{

    public Rectangle(){
        this.points = new ArrayList<>();
    }

    //Kopírovací konstruktor
    public Rectangle(Rectangle other) {
        this.points = new ArrayList<>();
        for (Point p : other.points) {
            this.points.add(new Point(p.getX(), p.getY()));
        }
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
                Point snapped = getXSnappedPoint(points.getFirst(), p);
                points.add(snapped);
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
     * Vrátí bod se souřadnicí X převzatou z druhého bodu
     * a souřadnicí Y převzatou z prvního bodu.
     * <br>
     * Používá se pro vodorovné (X) zarovnání linky – výsledný bod
     * leží na stejné horizontále jako p1 a ve stejném sloupci jako p2.
     */
    public Point getXSnappedPoint(Point p1, Point p2) {
        return new Point(p2.getX(), p1.getY());
    }

}
