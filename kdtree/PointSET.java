/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> rbTree;

    // construct an empty set of points
    public PointSET() {
        rbTree = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return rbTree.size() == 0;
    }

    // number of points in the set
    public int size() {
        return rbTree.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        rbTree.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        if (rbTree.contains(p)) {
            return true;
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.01);

        for (Point2D p : rbTree) {
            StdDraw.point(p.x(), p.y());
            // StdDraw.text(p.x(), p.y(), "(" + p.x() + ", " + p.y() + ")");
        }
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        /*
            1. find the (xmin, ymin), (xmax, ymax) of the rect
            2. iterate through points, check:
                - x coord of the point between [xmin, xmax];
                  y coord of the point between [ymin, ymax];
            3. if meets condition in 3, add to the list
         */
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point2D> boundedPts = new ArrayList<>();
        Point2D pointMin = new Point2D(rect.xmin(), rect.ymin());
        Point2D pointMax = new Point2D(rect.xmax(), rect.ymax());
        for (Point2D pt : rbTree.subSet(pointMin, true, pointMax, true)) {
            if (rect.contains(pt)) {
                boundedPts.add(pt);
            }
        }
        return boundedPts;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        /*
         find the point with minimum dist to point p
         update the distance if a point is closer
         assign the point to min point
         */
        if (p == null) {
            throw new IllegalArgumentException();
        }
        double minDist = Double.POSITIVE_INFINITY;
        // ArrayList<Point2D> minSet = new ArrayList<>();
        Point2D nearestPoint = null;
        for (Point2D pt : rbTree) {
            // double dist = Math.pow(pt.x() - p.x(), 2) + Math.pow(pt.y() - p.y(), 2);
            double dist = pt.distanceSquaredTo(p);
            if (minDist > dist) {
                minDist = dist;
                nearestPoint = pt;
            }
        }
        return nearestPoint;
    }

    private void print() {
        for (Point2D pt : rbTree) {
            StdOut.println("x coordinate: " + pt.x() + " y coordinate: " + pt.y());
        }
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        // brute.print();
        StdOut.println("PointSET");
        StdOut.println("is empty? " + brute.isEmpty());
        StdOut.println("size: " + brute.size());
        Point2D pt = new Point2D(0.011, 0.001);
        brute.insert(pt);
        StdOut.println("insert successful? " + brute.contains(pt));
        brute.draw();
        RectHV rect = new RectHV(0.1, 0.1, 0.4, 0.8);
        StdOut.println("point within range: ");
        for (Point2D p : brute.range(rect)) {
            StdOut.println(p.x() + ", " + p.y());
        }
        StdOut.println("nearest point: " + brute.nearest(new Point2D(0.1, 0.1)));


    }
}
