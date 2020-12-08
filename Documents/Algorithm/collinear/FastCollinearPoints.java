/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private LineSegment[] ls;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        final int ptSize = points.length;
        checkNull(points);
        Point[] pts = points.clone();
        Arrays.sort(pts);
        checkDup(pts, ptSize);
        List<LineSegment> resSegment = new ArrayList<>();
        for (int i = 0; i < ptSize; i++) {
            Point p = pts[i];
            // deep copy of sorted points
            Point[] sortedPoints = pts.clone();
            Arrays.sort(sortedPoints, p.slopeOrder());
            // for (Point pt : sortedPoints) {
            //     StdOut.println(i + ": " + pt);
            // }

            // double slope = p.slopeTo(sortedPoints[i]);
            int n = 1;
            while (n < ptSize) {
                ArrayList<Point> collinearPts = new ArrayList<>();
                double slope = p.slopeTo(sortedPoints[n]);
                // StdOut.println("slope: " + slope);
                do {
                    collinearPts.add(sortedPoints[n++]);
                } while (n < ptSize && p.slopeTo(sortedPoints[n]) == slope);
                if (collinearPts.size() >= 3
                        && p.compareTo(collinearPts.get(0)) < 0) {
                    resSegment.add(new LineSegment(p, collinearPts.get(collinearPts.size() - 1)));
                }
            }
            ls = resSegment.toArray(new LineSegment[0]);
        }
    }

    private void checkNull(Point[] points) {
        if (points.length == 0)
            throw new IllegalArgumentException("input is null");
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("input contains null");
            }
        }
    }

    private void checkDup(Point[] pts, int ptSize) {
        if (ptSize > 1) {
            for (int i = 0; i < ptSize - 1; i++) {
                if (pts[i].compareTo(pts[i + 1]) == 0) {
                    throw new IllegalArgumentException("input element duplicates");
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return ls.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return ls.clone();
    }

    public static void main(String[] args) {
        Point[] pts = new Point[8];
        pts[0] = new Point(2, 2);
        pts[1] = new Point(1, 1);
        pts[2] = new Point(3, 3);
        pts[3] = new Point(4, 4);
        pts[4] = new Point(1, 2);
        pts[5] = new Point(2, 4);
        pts[6] = new Point(3, 6);
        pts[7] = new Point(4, 8);
        FastCollinearPoints fcp = new FastCollinearPoints(pts);

        StdOut.println("Number of Segments: " + fcp.numberOfSegments());
        for (LineSegment l : fcp.segments()) {
            StdOut.println(l);
        }
    }
}
