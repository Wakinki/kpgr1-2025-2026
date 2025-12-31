package fill;

import model.Point;
import raster.Raster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanLine implements Filler {

    private final Raster raster;
    private final List<Point> vertices;
    private final int fillColor;

    public ScanLine(Raster raster, List<Point> vertices, int fillColor) {
        this.raster = raster;
        this.vertices = vertices;
        this.fillColor = fillColor;
    }

    @Override
    public void fill() {
        scanlineFill();
    }

    private void scanlineFill() {

        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point p : vertices) {
            minY = Math.min(minY, p.getY());
            maxY = Math.max(maxY, p.getY());
        }

        // For each scanline
        for (int y = minY; y <= maxY; y++) {

            List<Integer> intersections = new ArrayList<>();

            // For each edge
            for (int i = 0; i < vertices.size(); i++) {
                Point p1 = vertices.get(i);
                Point p2 = vertices.get((i + 1) % vertices.size());

                // Ignore horizontal edges
                if (p1.getY() == p2.getY())
                    continue;

                int yMin = Math.min(p1.getY(), p2.getY());
                int yMax = Math.max(p1.getY(), p2.getY());

                // Check scanline intersection (IMPORTANT RULE)
                if (y >= yMin && y < yMax) {

                    // Compute intersection X
                    double x =
                            p1.getX() + (double)(y - p1.getY()) * (p2.getX() - p1.getX())
                                    / (double)(p2.getY() - p1.getY());

                    intersections.add((int)Math.round(x));
                }
            }

            // Sort intersections
            Collections.sort(intersections);

            // Fill between pairs
            for (int i = 0; i < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd   = intersections.get(i + 1);

                for (int x = xStart; x <= xEnd; x++) {
                    raster.setPixel(x, y, fillColor);
                }
            }
        }
    }
}
