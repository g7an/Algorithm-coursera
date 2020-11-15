/* *****************************************************************************
 *  Name: Shuyao Tan
 *  Date: 11/16/2020
 *  Description: Assignment 2
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    // initial capacity of underlying resizing array
    private static final int INIT_CAPACITY = 8;

    private Item[] arr;         // array of items
    private int size;            // number of elements on queue

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[INIT_CAPACITY];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= size : "capacity should be larger than current size";

        Item[] copy;
        copy = Arrays.copyOf(arr, capacity);
        arr = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size == arr.length) {
            resize(2 * size);
        }
        arr[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int randomIndex = StdRandom.uniform(size);
        Item item = arr[randomIndex];
        if (randomIndex != size - 1)
            arr[randomIndex] = arr[size - 1];
        arr[size - 1] = null;
        size--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }

        int randomIndex = StdRandom.uniform(size);
        Item item = arr[randomIndex];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomArrayIterator();
    }

    private class RandomArrayIterator implements Iterator<Item> {
        private Item[] arrCopy;
        private int pointer = 0;

        public RandomArrayIterator() {
            copyQueue();
            StdRandom.shuffle(arrCopy);
        }

        private void copyQueue() {
            arrCopy = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                arrCopy[i] = arr[i];
            }
        }

        public boolean hasNext() {
            return pointer < size;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            return arrCopy[pointer++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        StdOut.println(rq.dequeue());
        rq.enqueue(1);
        StdOut.println(rq.size());
        for (int i = 0; i < 5; i++) {
            rq.enqueue(i);
        }
        for (int a : rq) {
            for (int b : rq)
                StdOut.print(a + "-" + b + " ");
            StdOut.println(a);
            StdOut.println();
        }
        for (int i = 0; i < 5; i++) {
            StdOut.println("random peek: " + rq.sample());
            StdOut.println("random dequeue: " + rq.dequeue());
        }

    }

}
