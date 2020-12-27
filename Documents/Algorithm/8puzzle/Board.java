/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private int[][] tiles;
    private int n;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length; // square, same length for row, col
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", this.tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        int wrongDir = 0;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.tiles[i][j] != i * n + j + 1 && this.tiles[i][j] != 0) {
                    wrongDir++;
                }
            }
        }
        return wrongDir;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int dist = 0;
        // change while loop to for loop
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == (n - 1) && j == (n - 1)) {
                    break;
                }
                StdOut.println("i: " + i + " j: " + j + " i * n + j + 1: " + (i * n + j + 1));
                int row = 0;
                int col = 0;
                while (row < n && col < n) {
                    StdOut.println("tiles " + row + " " + col + ": " + tiles[row][col]);
                    if (tiles[row][col] == i * n + j + 1) {
                        break;
                    }
                    if (col < n - 1) {
                        col++;
                    }
                    else {
                        col = 0;
                        row++;
                    }
                }
                dist += j - col < 0 ? (col - j) : (j - col);
                dist += row - i < 0 ? (i - row) : (row - i);
                StdOut.println("coming here");
                StdOut.println("i " + i + " j " + j + " dist " + dist);
            }
        }
        return dist;
    }

    // is this board the goal board?
    public boolean isGoal() {
        /*
            goal board:
            tiles[i][j] == i * n + j + 1 for 0 - tiles[i][j-1], 0 for tiles[n-1][n-1]
         */
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != i * n + j + 1) {
                    if (tiles[n - 1][n - 1] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board board = (Board) y;
        return Arrays.deepEquals(this.tiles, board.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        /*
        neighbors: swap with blank
        notice: if (row, col) is the blank,
            only neighbors of the blank, i.e. (row + 1, col), (row, col + 1),
            (row - 1, col), (row, col - 1) can swap with the blank
        requirement: returns all the possible cases
        How to deal with iterable? ==> any collection type (list, set, ArrayList,
        inherits from iterable interface ==> any collection type realizes iterable interface
         */
        ArrayList<Board> neighbors = new ArrayList<>();
        int row = 0;
        int col = 0;
        while (row < n && col < n) {
            if (tiles[row][col] == 0) {
                break;
            }
            if (col < n - 1) {
                col++;
            }
            else {
                col = 0;
                row++;
            }
        }
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int[] direction : directions) {
            int rowToCheck = row + direction[0];
            int colToCheck = col + direction[1];
            if (checkValid(rowToCheck, colToCheck)) {
                neighbors.add(new Board(swapTiles(row, col, rowToCheck, colToCheck)));
            }
        }
        return neighbors;
    }

    private int[][] swapTiles(int row, int col, int rowToSwap, int colToSwap) {
        int[][] twinTiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(tiles[i], 0, twinTiles[i], 0, n);
        }
        int temp = this.tiles[rowToSwap][colToSwap];
        twinTiles[rowToSwap][colToSwap] = this.tiles[row][col];
        twinTiles[row][col] = temp;
        return twinTiles;
    }

    private boolean checkValid(int row, int col) {
        return (row >= 0 && row < n && col >= 0 && col < n);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        /*
            get 4 random int: row, col, rolToSwap, colToSwap. Range: 0 - n.
            Requirement: 1. tiles[row][col], tiles[rowToSwap][colToSwap] both not 0;
            2. row != rowToSwap, col != colToSwap
         */
        int row = StdRandom.uniform(0, n);
        int col = StdRandom.uniform(0, n);
        int rowToSwap = StdRandom.uniform(0, n);
        int colToSwap = StdRandom.uniform(0, n);
        while (this.tiles[row][col] == 0) {
            row = StdRandom.uniform(0, n);
        }
        while ((row == rowToSwap && col == colToSwap) ||
                this.tiles[rowToSwap][colToSwap] == 0) {
            rowToSwap = StdRandom.uniform(0, n);
        }
        // int[][] twinTiles = this.tiles.clone();
        // int temp = this.tiles[rowToSwap][colToSwap];
        // twinTiles[rowToSwap][colToSwap] = this.tiles[row][col];
        // twinTiles[row][col] = temp;
        int[][] resTiles = swapTiles(row, col, rowToSwap, colToSwap);
        return new Board(resTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] arr = new int[3][3];
        // for (int i = 0; i < arr.length; i++) {
        //     for (int j = 0; j < arr.length; j++) {
        //         arr[i][j] = i + j;
        //     }
        // }
        arr[0][0] = 8;
        arr[0][1] = 1;
        arr[0][2] = 3;
        arr[1][0] = 4;
        arr[1][1] = 0;
        arr[1][2] = 2;
        arr[2][0] = 7;
        arr[2][1] = 6;
        arr[2][2] = 5;
        int[][] arr2 = arr.clone();
        Board b = new Board(arr);
        Board b2 = new Board(arr2);
        StdOut.println(b.toString());
        // StdOut.println(b.hamming());
        // StdOut.println(b.manhattan());
        // StdOut.println(b.equals(b2));
        // StdOut.println(b.twin().toString());
        for (Board bb : b.neighbors()) {
            StdOut.println(bb);
        }
    }


}
