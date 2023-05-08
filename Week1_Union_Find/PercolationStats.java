import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * @author Wenjie FU
 * @create 2022-02-28 22:51
 */
public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final double[] result; // array contain the percolation threshold
    private final int trials;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }

        result = new double[trials];
        this.trials = trials;

        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = (int) StdRandom.uniform(1d, n + 1d);
                int col = (int) StdRandom.uniform(1d, n + 1d);
                if (percolation.isOpen(row, col)) {
                    continue;
                } else {
                    percolation.open(row, col);
                }
            }
            result[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(result);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(result);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean = " + ps.mean());
        System.out.println("stddev = " + ps.stddev());
        System.out.println("mean = " + ps.confidenceLo() + " " + ps.confidenceHi());
    }

}