/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private static class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode prevNode;
        private final int manDist;
        private final int priority; // caching purpose
        private final boolean twin;


        // initial search node
        public SearchNode(Board board, boolean twin) {
            this.board = board;
            moves = 0;
            prevNode = null;
            this.twin = twin;
            manDist = this.board.manhattan();
            priority = manDist + moves;
        }

        // succeeding search node
        public SearchNode(Board board, SearchNode prevNode) {
            this.board = board;
            this.moves = prevNode.moves + 1;
            this.prevNode = prevNode;
            this.twin = prevNode.twin;
            manDist = this.board.manhattan();
            this.priority = manDist + this.moves;
        }

        @Override
        public int compareTo(SearchNode that) {
            return (board.manhattan() + moves) - (that.board.manhattan() + that.moves);
        }


    }

    private Stack<Board> solutions;
    // int moves;
    private boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        /*
        pq: maintain leaf nodes
        add initial board to the new searchnode
        insert initial searchnode into pq
        repeat:
        del from the board searchnode with min priority
        insert all neighboring searchnode
        until searchnode contains the goal board
         */
        MinPQ<SearchNode> pq = new MinPQ<>();
        solutions = new Stack<>();
        solvable = false;
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        SearchNode initialNode = new SearchNode(initial, false);
        SearchNode twinNode = new SearchNode(initial.twin(), true);
        pq.insert(initialNode);
        pq.insert(twinNode);

        while (!pq.min().board.isGoal()) {
            SearchNode parentNode = pq.delMin();
            for (Board ele : parentNode.board.neighbors()) {
                if (parentNode.prevNode == null || !parentNode.prevNode.board.equals(ele)) {
                    pq.insert(new SearchNode(ele, parentNode));
                }
            }
        }
        SearchNode goal = pq.min();
        if (!goal.twin) {
            solvable = true;
            while (goal != null) {
                solutions.push(goal.board);
                goal = goal.prevNode;
            }
        }
        else {
            solvable = false;
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        /*
        to determine if initial board solvable, has to determine if there are twin node solvable
        if there is twin node solvable, then the initial board is unsolvable
        implementation:
        1. in constructor, add initial node and twin node on pq simultaneously
        2. find solution for the initial board and its twin node
        3. see what is the solution belongs
         */
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        /*
        if solvable, moves is the move recorded in the goal board;
        otherwise, moves is -1
         */
        if (isSolvable()) {
            // StdOut.println("solutions size " + solutions.size());
            return solutions.size() - 1;
        }
        return -1;
    }

    // // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return solutions;
        }
        return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In("puzzle04.txt");
        int n = in.readInt();
        // StdOut.println(n);
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
