import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] trialResult;
    private int trials;
    private static final double CONFIDENCE_95 = 1.96;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (trials <= 0 || n <= 0)
            throw new IllegalArgumentException("trials or grid "
                                                       + "size must be larger than 0");
        this.trials = trials;
        trialResult = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                perc.open(row, col);
            }
            double numOpen = perc.numberOfOpenSites();
            double result = numOpen / (n * n);
            trialResult[i] = result;
        }


    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialResult);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialResult);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double conLo = mean() - CONFIDENCE_95 * StdStats.var(trialResult) / Math.sqrt(trials);
        return conLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double conHi = mean() + CONFIDENCE_95 * StdStats.var(trialResult) / Math.sqrt(trials);
        return conHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        int gridSize = 0;
        int trialCount = 0;
        if (args.length >= 2) {
            gridSize = Integer.parseInt(args[0]);
            trialCount = Integer.parseInt(args[1]);
        }
        StdOut.println(gridSize + " " + trialCount);
        PercolationStats ps = new PercolationStats(gridSize, trialCount);
        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);

    }


}
