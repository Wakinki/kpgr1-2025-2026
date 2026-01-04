package model;

import fill.Filler;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    protected List<Point> points;
    protected Filler fill;

    public Polygon() {
        this.points = new ArrayList<>();
    }

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public Filler getFill() {
        return fill;
    }

    public void setFill(Filler fill) {
        this.fill = fill;
    }

    public void addPoint(Point p){
        points.add(p);
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public Point getLastPoint() {return points.getLast();}

    public Point getFirstPoint() {return points.getFirst();}

    public int getSize() {
        return points.size();
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
