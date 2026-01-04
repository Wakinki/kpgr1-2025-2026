package clip;

import model.Point;
import model.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * Sutherland–Hodgman algorithm (polygon clipping by a polygon).
 *
 * Input:
 *  - In  = list of vertices of the clipped (subject) polygon
 *  - E   = one clipping edge of the clipper (A -> B)
 *
 * Output:
 *  - Out = new list of vertices after clipping
 *
 * Procedure (for each edge E of the clipper):
 *  For each pair of consecutive vertices of the subject polygon,
 *  form a segment S -> E:
 *   - Determine whether S and E lie inside with respect to
 *     the clipping edge E (A -> B).
 *   - Depending on the inside / outside combination, there are 4 cases:
 *     1) S inside,  E inside  -> add E
 *     2) S inside,  E outside -> add the intersection
 *     3) S outside, E outside -> add nothing
 *     4) S outside, E inside  -> add the intersection, then E
 */
public class Clipper {

    /**
     * @param subject   the polygon to be clipped
     * @param clipPath the polygon used as the clipping shape
     * @return a new polygon containing only the part of the subject
     *         that lies inside the clipper
     */
    public Polygon clip(Polygon subject, Polygon clipPath) {
        // původní vrcholy ořezávaného polygonu
        List<Point> out = new ArrayList<>(subject.getPoints());
        List<Point> c = clipPath.getPoints();

        // kontrola platnosti clipPath, pokud neni platny, vrátíme původní polygon
        if (c == null || c.size() < 3) {
            Polygon r = new Polygon();
            r.setPoints(out);
            return r;
        }

        // urceni orientace clipPath +1 = proti smeru hodinovych rucicek, -1 = po smeru
        final int clipSign = orientationSign(c);

        // orezavani polygonu hranami polygonu clipPath
        for (int i = 0; i < c.size(); i++) {
            Point A = c.get(i);
            Point B = c.get((i + 1) % c.size());

            // predchozi vystup se stava vstupem pro dalsi orezavaci hranu
            List<Point> in = out;
            out = new ArrayList<>();
            if (in.isEmpty()) break;  // pokud uz nic nezbyva, koncime

            // S = posledni vrchol v seznamu in (pro prvni iteraci)
            Point S = in.getLast();

            // prochazeni vsech hran subject polygonu
            for (Point E : in) {
                boolean Ein = inside(E, A, B, clipSign);  // je koncovy bod E uvnitr?
                boolean Sin = inside(S, A, B, clipSign);  // je pocatecni bod S uvnitr?

                if (Ein) {
                    // E je uvnitr
                    if (!Sin) {
                        // S je venku, E je uvnitr - varianta 4
                        out.add(intersection(S, E, A, B));  // pridame prusecnik
                    }
                    out.add(E);  // pridame koncovy vrchol (varianta 1 nebo 4)
                } else {
                    // E je venku
                    if (Sin) {
                        // S je uvnitr, E je venku - varianta 2
                        out.add(intersection(S, E, A, B));  // pridame prusecnik
                    }
                    // varianta 3: S venku, E venku - nic nepridavame
                }

                // posuneme se na dalsi hranu: S se stava aktualnim E
                S = E;
            }

        }

        // vrácení vysledneho polygonu z orezanych vrcholu
        return new Polygon(out);
    }

    /**
     * Determines whether point p lies inside with respect to edge A -> B.
     * "Inside" means the point is to the left of the edge for a CCW polygon,
     * or to the right of the edge for a CW polygon.
     *
     * @param p        the point being tested
     * @param a        the starting point of the edge
     * @param b        the ending point of the edge
     * @param clipSign orientation of the clipper polygon
     *                 (+1 for CCW, -1 for CW)
     * @return true if point p is inside with respect to edge A -> B
     */
    private static boolean inside(Point p, Point a, Point b, int clipSign) {
        // vypocet vektoroveho soucinu (B-A) × (P-A)
        long cross = (long) (b.getX() - a.getX()) * (p.getY() - a.getY()) - (long) (b.getY() - a.getY()) * (p.getX() - a.getX());
        // pro CCW: cross >= 0 (bod nalevo), pro CW: cross <= 0 (bod napravo)
        return cross * clipSign >= 0;
    }

    /**
     * Determines the orientation of a polygon based on the sign of its oriented area.
     * The area is computed using the shoelace formula (Gauss's area formula).
     *
     * @param poly the list of vertices of the polygon
     * @return +1 for a polygon oriented counterclockwise (CCW),
     *         -1 for a polygon oriented clockwise (CW)
     */
    private static int orientationSign(List<Point> poly) {
        long sum = 0;  // dvojnasobek orientovane plochy polygonu
        for (int i = 0; i < poly.size(); i++) {
            Point p = poly.get(i);
            Point q = poly.get((i + 1) % poly.size());
            sum += (long) p.getX() * q.getY() - (long) q.getX() * p.getY();  // xi*y(i+1) - x(i+1)*yi
        }
        return (sum >= 0) ? 1 : -1;  // kladna plocha = CCW, zaporna = CW
    }

    /**
     * Computes the intersection of two line segments: S -> E (subject edge)
     * and A -> B (clipper edge).
     * Uses the parametric form of line equations.
     *
     * @param s the starting point of the subject edge
     * @param e the ending point of the subject edge
     * @param a the starting point of the clipper edge
     * @param b the ending point of the clipper edge
     * @return the intersection point of segment S -> E and line A -> B
     */
    private static Point intersection(Point s, Point e, Point a, Point b) {
        // parametrizace usecky S->E: P = S + t*(E-S)
        double px = s.getX(), py = s.getY();          // pocatecni bod S
        double rx = e.getX() - s.getX(), ry = e.getY() - s.getY();  // smerovy vektor S->E

        // parametrizace usecky A->B: Q = A + u*(B-A)
        double qx = a.getX(), qy = a.getY();          // pocatecni bod A
        double sx = b.getX() - a.getX(), sy = b.getY() - a.getY();  // smerovy vektor A->B

        // determinant pro reseni soustavy rovnic
        double den = rx * sy - ry * sx;

        // pokud jsou usecky rovnobezne, vratime koncovy bod E jako fallback
        if (den == 0) return e;

        // vypocet parametru t pro prusecik na usecce S->E
        double t = ((qx - px) * sy - (qy - py) * sx) / den;

        // souradnice pruseciku
        return new Point(px + t * rx, py + t * ry);
    }
}