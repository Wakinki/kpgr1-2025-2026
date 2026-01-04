package fill;

import model.Point;
import model.Polygon;
import rasterize.PolygonRasterizer;
import raster.Raster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Scan-Line polygon filling algorithm.
 *
 * The polygon is defined geometrically by its boundary edges
 * (a list of vertices). The interior is filled using the
 * even–odd rule.
 */
public class ScanLine implements Filler {

    private final Raster raster;
    private final Polygon polygon;
    private final int fillColor;
    private final PolygonRasterizer polygonRasterizer;

    public ScanLine(Raster raster, Polygon polygon, int fillColor,
                    PolygonRasterizer polygonRasterizer) {
        this.raster = raster;
        this.polygon = polygon;
        this.fillColor = fillColor;
        this.polygonRasterizer = polygonRasterizer;
    }

    /**
     * Stores information about a polygon edge prepared for
     * scan-line processing.
     *
     * The edge is stored so that y1 < y2 and represents
     * a half-open interval [y1, y2).
     */
    private static class Edge {
        int x1, y1, x2, y2; // y1 < y2

        Edge(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }
    }

    /*
     * Preprocessing steps:
     *  1) Remove horizontal edges
     *  2) Normalize edge orientation so that y1 < y2
     *  3) Use a half-open interval [y1, y2) to avoid
     *     double intersections at polygon vertices
     *  4) Determine the minimum and maximum y-coordinates
     */

    @Override
    public void fill() {
        if (raster == null || polygon == null) return;

        List<Point> v = polygon.getPoints();
        if (v == null || v.size() < 3) return;

        int w = raster.getWidth();
        int h = raster.getHeight();

        // Prepare the list of polygon edges
        List<Edge> edges = new ArrayList<>();
        int yMin = Integer.MAX_VALUE; // top-most polygon coordinate
        int yMax = Integer.MIN_VALUE; // bottom-most polygon coordinate

        // Iterate over all polygon edges
        for (int i = 0; i < v.size(); i++) {
            Point a = v.get(i);
            Point b = v.get((i + 1) % v.size()); // connect last vertex to first

            // Skip horizontal edges
            if (a.getY() == b.getY()) continue;

            // Normalize edge direction so y1 < y2
            int x1 = a.getX(), y1 = a.getY();
            int x2 = b.getX(), y2 = b.getY();

            if (y1 > y2) {
                int tx = x1; x1 = x2; x2 = tx;
                int ty = y1; y1 = y2; y2 = ty;
            }

            // Store the edge
            edges.add(new Edge(x1, y1, x2, y2));

            // Update vertical bounds of the polygon
            if (y1 < yMin) yMin = y1;
            if (y2 > yMax) yMax = y2;
        }

        if (edges.isEmpty()) return;

        // Clip scan-line range to raster bounds
        int ys = Math.max(0, yMin);
        int ye = Math.min(h - 1, yMax);

        /*
         * Filling algorithm:
         *  1) For each scan line y from ymin to ymax:
         *     - Find intersections of polygon edges with the scan line
         *     - Sort intersection x-coordinates
         *     - Fill spans between pairs of intersections
         *       using the even–odd rule
         *  2) Draw the polygon outline
         */

        // Main scan-line loop
        for (int y = ys; y <= ye; y++) {
            List<Integer> xs = new ArrayList<>();

            // Find intersections of edges with scan line y
            for (Edge e : edges) {
                // Check if y lies in the half-open interval [y1, y2)
                if (y < e.y1 || y >= e.y2) continue;

                // Compute interpolation parameter
                double t = (y - e.y1) / (double) (e.y2 - e.y1);

                // Compute x-coordinate of the intersection
                double x = e.x1 + t * (e.x2 - e.x1);

                // Add intersection x-coordinate
                xs.add((int) Math.round(x));
            }

            // At least two intersections are required to fill
            if (xs.size() < 2) continue;

            // Sort intersections from left to right
            Collections.sort(xs);

            // Fill spans between pairs of intersections (even–odd rule)
            for (int i = 0; i + 1 < xs.size(); i += 2) {
                int xA = xs.get(i);     // left boundary
                int xB = xs.get(i + 1); // right boundary

                // Skip spans outside the raster
                if (xB < 0 || xA >= w) continue;

                // Clip span to raster bounds
                int xStart = Math.max(0, xA);
                int xEnd   = Math.min(w - 1, xB);

                // Fill the pixels in the span
                for (int xPix = xStart; xPix <= xEnd; xPix++) {
                    raster.setPixel(xPix, y, fillColor);
                }
            }
        }

        // Draw the polygon outline
        if (polygonRasterizer != null) {
            polygonRasterizer.rasterize(polygon);
        }
    }
}
