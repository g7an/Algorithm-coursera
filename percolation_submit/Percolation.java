/* *****************************************************************************
 *  Name:              Shuyao Tan
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */


import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // creates n-by-n grid, with all sites initially blocked
    private WeightedQuickUnionUF grid;
    private int numN; //size
    private int numOpen;
    private boolean[] site;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n must be larger than "
                                                       + "0");

        this.numN = n;
        site = new boolean[n * n + 2];
        grid = new WeightedQuickUnionUF(n * n + 2);
        for (int i = 0; i <= n * n + 2; i++) {
            numOpen = 0;
        }

    }

    private int flattenGrid(int row, int col) {
        return (row - 1) * numN + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!checkGrid(row, col)) {
            throw new IllegalArgumentException("input not valid");
        }

        if (!isOpen(row, col)) {
            site[flattenGrid(row, col)] = true;
            numOpen++;
        }

        // upper
        if (checkGrid(row - 1, col) && isOpen(row - 1, col)) {
            grid.union(flattenGrid(row, col), flattenGrid(row - 1, col));
        }
        // lower
        if (checkGrid(row + 1, col) && isOpen(row + 1, col)) {
            grid.union(flattenGrid(row, col), flattenGrid(row + 1, col));
        }
        // left
        if (checkGrid(row, col - 1) && isOpen(row, col - 1)) {
            grid.union(flattenGrid(row, col), flattenGrid(row, col - 1));
        }
        // right
        if (checkGrid(row, col + 1) && isOpen(row, col + 1)) {
            grid.union(flattenGrid(row, col), flattenGrid(row, col + 1));
        }
        if (row == 1) {
            activateTop(col);
        }
        if (row == numN) {
            activateBottom(col);
        }

    }

    private boolean checkGrid(int row, int col) {
        return (row > 0 && col > 0 && row <= numN && col <= numN);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!checkGrid(row, col)) {
            throw new IllegalArgumentException("input not valid");
        }
        return site[flattenGrid(row, col)];
    }

    // make auxilliary top and bottom site true
    private void activateTop(int col) {
        site[0] = true;
        grid.union(0, flattenGrid(1, col));
    }

    private void activateBottom(int col) {
        site[numN * numN + 1] = true;
        grid.union(numN * numN + 1, flattenGrid(numN, col));
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!checkGrid(row, col)) {
            throw new IllegalArgumentException("input not valid");
        }
        return grid.find(flattenGrid(row, col)) == grid.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        if (grid.find(numN * numN + 1) == grid.find(0)) {
            return true;
        }
        return false;
    }

}

