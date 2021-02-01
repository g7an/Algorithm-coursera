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

import java.util.LinkedList;

public class KdTree {


    private class Node {
        Node left;
        Node right;
        Point2D p;
        private RectHV rect;
        boolean isVertical;

        public Node(Point2D p, boolean isVertical, RectHV rect) {
            this.p = p;
            this.left = null;
            this.right = null;
            this.isVertical = isVertical;
            if (rect == null) {
                rect = new RectHV(0, 0, 1, 1);
            }
            this.rect = rect;
        }
    }

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        /*
            if empty, insert directly;
            if not empty:
                even number, compare with parent with x coord;
                odd number, compare with parent with y coord;
            use recursive method to compare with parent
         */
        root = insert(root, p, true, root.rect);
    }

    private Node insert(Node node, Point2D pt, boolean isVertical, RectHV rect) {
        if (node == null) {
            size++;
            return new Node(pt, isVertical, rect);
        }
        if (node.p.equals(pt)) {
            return node;
        }
        RectHV r = null;
        if (node.isVertical) { // compare horizontally
            if (pt.x() <= node.p.x()) {
                r = new RectHV(rect.xmin(), rect.ymin(), node.p.x(), rect.ymax());
                node.left = insert(node.left, pt, !node.isVertical, r);
            }
            if (pt.x() > node.p.x()) {
                r = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                node.right = insert(node.right, pt, !node.isVertical, r);
            }
        }
        else { // compare vertically
            if (pt.y() <= node.p.y()) {
                r = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
                node.left = insert(node.left, pt, !node.isVertical, r);
            }
            if (pt.y() > node.p.y()) {
                r = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
                node.right = insert(node.left, pt, !node.isVertical, r);
            }

        }
        return node;
    }


    // does the set contain point p?
    public boolean contains(Point2D p) {
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (node == null)
            return false;

        if (node.p.x() == p.x() && node.p.y() == p.y()) {
            return true;
        }

        if ((node.isVertical && p.x() < node.p.x()) || (!node.isVertical
                && p.y() <= node.p.y())) {
            return contains(node.left, p);
        }
        else {
            return contains(node.right, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        // draw the node & rect recursively
        StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);
        draw(root, root.isVertical);

    }

    private void draw(Node node, boolean isVertical) {
        if (node.left != null) draw(node.left, !node.isVertical);
        if (node.right != null) draw(node.right, !node.isVertical);
        // draw the point first
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.p.x(), node.p.y());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        /*
        To find all points contained in a given query rectangle, start at the root and recursively
        search for points in both subtrees using the following pruning rule: if the query rectangle
        does not intersect the rectangle corresponding to a node, there is no need to explore that
        node (or its subtrees). A subtree is searched only if it might contain a point contained in
        the query rectangle.
         */
        LinkedList<Point2D> boundedPts = new LinkedList<>();

        return boundedPts;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return null;
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
        StdOut.println("kdTree");
        StdOut.println("is empty? " + brute.isEmpty());
        StdOut.println("size: " + brute.size());
        Point2D pt = new Point2D(0.011, 0.001);
        brute.insert(pt);
        StdOut.println("insert successful? " + brute.contains(pt));
        brute.draw();
    }
}
