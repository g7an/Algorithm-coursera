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
        List<LineSegment> resSegment = new ArrayList<>();
        if (points.length == 0) {
            throw new IllegalArgumentException("Input is null");
        }
        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("Input contains null");
            }
        }
        Point[] pts = points.clone();
        Arrays.sort(pts);

        for (int i = 0; i < ptSize - 1; i++) {
            Point p = pts[i];
            // deep copy of sorted points
            Point[] sortedPoints = pts.clone();
            Arrays.sort(sortedPoints, p.slopeOrder());
            for (Point pt : sortedPoints) {
                StdOut.println(i + ": " + pt);
            }
            ArrayList<Point> collinearPts = new ArrayList<>();

            // double slope = p.slopeTo(sortedPoints[i]);
            for (int j = 1; j < ptSize; j++) {
                double slope = p.slopeTo(sortedPoints[j]);
                StdOut.println("slope: " + slope);
                collinearPts.add(sortedPoints[j]);
                for (int k = j + 1; k < ptSize; k++) {
                    if (p.slopeTo(sortedPoints[k]) == slope) {
                        collinearPts.add(sortedPoints[k]);
                        StdOut.println(
                                "p " + p + " , p j " + sortedPoints[j] + "p k" + sortedPoints[k]);
                        StdOut.println("now size: " + collinearPts.size());
                    }
                    if (collinearPts.size() >= 3
                            && p.compareTo(collinearPts.get(0)) < 0 && isUnique(resSegment, p,
                                                                                collinearPts
                                                                                        .get(collinearPts
                                                                                                     .size()
                                                                                                     - 1))) {
                        StdOut.println(
                                "p: " + p + " last " + collinearPts.get(collinearPts.size() - 1));
                        resSegment
                                .add(new LineSegment(p, collinearPts.get(collinearPts.size() - 1)));
                    }
                }


            }
            ls = resSegment.toArray(new LineSegment[0]);
            for (LineSegment l : ls) {
                StdOut.println(l);
            }
        }
    }


    private boolean isUnique(List<LineSegment> resSegment, Point p, Point point) {
        LineSegment testSeg = new LineSegment(p, point);
        for (LineSegment res : resSegment) {
            if (res == testSeg) {
                return false;
            }
        }
        return true;
    }


    // the number of line segments
    public int numberOfSegments() {
        return ls.length;
    }

    // the line segments
    // public LineSegment[] segments() {
    //
    // }

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
    }
}
