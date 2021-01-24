/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int n;
    private int manhattan;
    private int hamming;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.n = tiles.length; // square, same length for row, col
        this.tiles = new int[n][n];
        this.hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] != 0 && tiles[i][j] != i * n + j + 1) {
                    this.manhattan += manhattanDistance(i, j, tiles[i][j]);
                    this.hamming++;
                }
            }
        }
    }

    private int manhattanDistance(int row, int col, int value) {
        /*
        ideal: block[row][col] = row * n + col + 1
        now: block[i][j] = row * n + col + 1
        difference:
        horizontal = row - i;
        vertical = col - j;
        regard value = row * dim() + col + 1
        value - 1 = row * dim() + col
        dim() = n
        value - 1 = row * n + col
        0 <= col < n, 0 <= row < n
         */
        value--;
        int vertical = Math.abs(value / n - row);
        int horizontal = Math.abs(value % n - col);
        return (horizontal + vertical);
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
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        /*
            goal board:
            tiles[i][j] == i * n + j + 1 for 0 - tiles[i][j-1], 0 for tiles[n-1][n-1]
         */
        return hamming() == 0;
    }

    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this == y)
            return true;
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
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
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
            just get a random twin
         */
        // swap 2 fixed tiles is the quickest solution (random takes too long)
        if (this.tiles[0][0] != 0 && this.tiles[0][1] != 0) {
            return new Board(swapTiles(0, 0, 0, 1));
        }
        else {
            return new Board(swapTiles(1, 0, 1, 1));
        }

    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] arr = new int[3][3];
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
        StdOut.println(b.hamming());
        StdOut.println(b.manhattan());
        StdOut.println(b.equals(b2));
        StdOut.println(b.twin().toString());
        for (Board bb : b.neighbors()) {
            StdOut.println(bb);
        }
    }


}
