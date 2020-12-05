/* *****************************************************************************
 *  Name: Shuyao Tan
 *  Date: 05/12/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] ls;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        // check null
        checkNull(points);

        final int ptSize = points.length;
        ArrayList<LineSegment> lineSeg = new ArrayList<>();
        Point[] pts = points.clone();
        Arrays.sort(pts);
        // check duplicates
        checkDup(pts, ptSize);

        if (ptSize >= 4) {
            Point[] temp = new Point[4];
            for (int i = 0; i < ptSize - 3; i++) {
                temp[0] = pts[i];
                for (int j = i + 1; j < ptSize - 2; j++) {
                    temp[1] = pts[j];
                    for (int k = j + 1; k < ptSize - 1; k++) {
                        temp[2] = pts[k];
                        for (int l = k + 1; l < ptSize; l++) {
                            temp[3] = pts[l];
                            if (isCollinear(temp)) {
                                LineSegment sg = createSeg(temp);
                                lineSeg.add(sg);
                            }
                        }
                    }
                }
            }
        }
        ls = lineSeg.toArray(new LineSegment[0]);
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

    private void checkNull(Point[] points) {
        if (points.length == 0)
            throw new IllegalArgumentException("input is null");
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("input contains null");
            }
        }
    }

    private LineSegment createSeg(Point[] temp) {
        return new LineSegment(temp[0], temp[3]);
    }


    private boolean isCollinear(Point[] temp) {
        double slopeAB = temp[0].slopeTo(temp[1]);
        double slopeAC = temp[0].slopeTo(temp[2]);
        double slopeAD = temp[0].slopeTo(temp[3]);
        if (slopeAB == slopeAC && slopeAB == slopeAD) {
            return true;
        }
        return false;
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
        Point[] pts = new Point[4];
        pts[0] = new Point(1, 1);
        pts[1] = new Point(2, 2);
        pts[2] = new Point(3, 3);
        pts[3] = new Point(4, 4);
        BruteCollinearPoints bcp = new BruteCollinearPoints(pts);
        StdOut.println("Number of Segments: " + bcp.numberOfSegments());

        for (LineSegment ls : bcp.segments()) {
            StdOut.println(ls);
        }
    }
}
