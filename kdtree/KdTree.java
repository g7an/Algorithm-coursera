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
        if (isEmpty()) {
            root = insert(root, p, true, null);

        }
        root = insert(root, p, root.isVertical, root.rect);
        StdOut.println("root: " + root.p.x() + ", " + root.p.y());

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
                node.left = insert(node.left, pt, false, r);
            }
            if (pt.x() > node.p.x()) {
                r = new RectHV(node.p.x(), rect.ymin(), rect.xmax(), rect.ymax());
                node.right = insert(node.right, pt, false, r);
            }
        }
        else { // compare vertically
            if (pt.y() <= node.p.y()) {
                r = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.p.y());
                node.left = insert(node.left, pt, true, r);
            }
            if (pt.y() > node.p.y()) {
                r = new RectHV(rect.xmin(), node.p.y(), rect.xmax(), rect.ymax());
                node.right = insert(node.right, pt, true, r);
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
        draw(root);

    }

    private void draw(Node node) {
        if (node.left != null) draw(node.left);
        if (node.right != null) draw(node.right);
        // draw the point first
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        StdDraw.point(node.p.x(), node.p.y());
        // StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        // if (node.isVertical) {
        //     StdDraw.setPenColor(StdDraw.RED);
        //     StdDraw.setPenRadius(0.01);
        //     StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        // }
        // else {
        //     StdDraw.setPenColor(StdDraw.BLUE);
        //     StdDraw.setPenRadius();
        //     StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.ymin(), node.p.y());
        // }
        double xmin, ymin, xmax, ymax;
        if (node.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            xmin = node.p.x();
            xmax = node.p.x();
            ymin = node.rect.ymin();
            ymax = node.rect.ymax();
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            ymin = node.p.y();
            ymax = node.p.y();
            xmin = node.rect.xmin();
            xmax = node.rect.xmax();
        }
        StdDraw.setPenRadius();
        StdDraw.line(xmin, ymin, xmax, ymax);
    }

    private LinkedList<Point2D> boundedPts = new LinkedList<>();

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        /*
        To find all points contained in a given query rectangle, start at the root and recursively
        search for points in both subtrees using the following pruning rule: if the query rectangle
        does not intersect the rectangle corresponding to a node, there is no need to explore that
        node (or its subtrees). A subtree is searched only if it might contain a point contained in
        the query rectangle.
         */
        search(root, rect);
        return boundedPts;
    }

    private void search(Node node, RectHV rect) {
        if (node == null || !node.rect.intersects(rect)) return;
        if (rect.contains(node.p)) {
            boundedPts.add(node.p);
        }
        search(node.left, rect);
        search(node.right, rect);
    }

    private Point2D nearestPt = null;

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        /*
        To find a closest point to a given query point, start at the root and recursively search in
        both subtrees using the following pruning rule: if the closest point discovered so far is
        closer than the distance between the query point and the rectangle corresponding to a node,
        there is no need to explore that node (or its subtrees). That is, search a node only
        if it might contain a point that is closer than the best one found so far. The effectiveness
         of the pruning rule depends on quickly finding a nearby point. To do this, organize the
         recursive method so that when there are two possible subtrees to go down, you always choose
         the subtree that is on the same side of the splitting line as the query point as the first
         subtree to explore—the closest point found while exploring the first subtree may enable
         pruning of the second subtree.
         */
        double minDist = root.p.distanceTo(p);
        searchPt(root, p, minDist);
        return nearestPt;
    }

    private Point2D searchPt(Node node, Point2D pt, double minDist) {
        if (node.p.distanceTo(pt) <= minDist) {
            nearestPt = node.p;
            minDist = node.p.distanceTo(pt);
        }
        int cmp = 0;
        if (node.isVertical) {
            cmp = Point2D.X_ORDER.compare(node.p, pt);
        }
        else {
            cmp = Point2D.Y_ORDER.compare(node.p, pt);
        }
        if (cmp > 0 && minDist > node.left.rect.distanceTo(pt)) {
            /*
            children's going to right, how?
             */
            return searchPt(node.left, pt, minDist);
        }
        return searchPt(node.right, pt, minDist);


    }


    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        // brute.print();
        StdOut.println("kdTree");
        StdOut.println("is empty? " + kdtree.isEmpty());
        StdOut.println("size: " + kdtree.size());
        Point2D pt = new Point2D(0.011, 0.001);
        kdtree.insert(pt);
        StdOut.println("insert successful? " + kdtree.contains(pt));
        kdtree.draw();
        for (Point2D p : kdtree.range(new RectHV(0.0, 0, 1, 1))) {
            StdOut.println(p.x() + ", " + p.y());
        }

    }
}
