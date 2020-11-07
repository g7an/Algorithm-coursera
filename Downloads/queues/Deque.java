/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.ListIterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size = 0;

    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        if (isEmpty()) {
            last = first;
        }
        else {
            first.next = oldFirst;
            oldFirst.prev = first;
        }

        StdOut.println("added first: " + item);
        size++;

    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        // Node trueFirst = new Node();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        if (isEmpty()) {
            first = last;
        }
        else {
            oldLast.next = last;
            last.prev = oldLast;
            last.next = null;
        }
        size++;
        StdOut.println("added last: " + item);
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();
        Item item = first.item;
        Node oldFirst = first;
        if (size() == 0) {
            first = null;
        }
        first = oldFirst.next;
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();

        Item item = last.item;
        Node oldLast = last;
        if (size == 0) {
            last = null;
        }
        // StdOut.println("last: " + last.item + " size: " + size);
        last = oldLast.prev;
        // StdOut.println("what now: " + last);
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator<Item>() {
            private Node current = first;

            public boolean hasNext() {
                return current != null;
            }

            public Item next() {
                if (current == null) {
                    throw new java.util.NoSuchElementException();
                }
                Item item = current.item;
                current = current.next;
                return item;
            }

            public boolean hasPrevious() {
                return false;
            }

            public Item previous() {
                return null;
            }

            public int nextIndex() {
                return 0;
            }

            public int previousIndex() {
                return 0;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }

            public void set(Item item) {

            }

            public void add(Item item) {

            }
        };
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<>();
        // addFirst and removeLast
        for (int i = 0; i < 5; i++) {
            dq.addFirst(i);
            StdOut.println("size: " + dq.size());
        }

        for (int a : dq) {
            for (int b : dq)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }

        for (int i = 0; i < 5; i++) {
            StdOut.println("removed: " + dq.removeLast());
            StdOut.println("is Empty? " + dq.isEmpty());
        }

        // addLast and remove first
        for (int i = 0; i < 5; i++) {
            dq.addLast(i);
            StdOut.println("size: " + dq.size());
        }

        for (int i = 0; i < 5; i++) {
            StdOut.println("removed: " + dq.removeFirst());
            StdOut.println("is Empty? " + dq.isEmpty());
        }

        dq.addFirst(1);
        dq.addLast(2);
        StdOut.println("remove last: " + dq.removeLast());
        StdOut.println("remove first: " + dq.removeFirst());
    }
}
