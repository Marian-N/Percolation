import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
public class PercolationStats {
    private static final double S_CONST = 1.96;
    private final double mean, stddev, confidenceLo, confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        int sites = n * n;
        final double[] threasholds = new double[trials];
        for (int trial = 0; trial < trials; trial++) {
            Percolation perc = new Percolation(n);
            int openSites = 0;
            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int column = StdRandom.uniform(1, n + 1);
                if (perc.isOpen(row, column)) continue;
                perc.open(row, column);
                openSites++;
            }
            threasholds[trial] = (double) openSites / sites;
        }
        mean = StdStats.mean(threasholds);
        stddev = StdStats.stddev(threasholds);
        confidenceLo = mean - ((S_CONST * stddev) / Math.sqrt(trials));
        confidenceHi = mean + ((S_CONST * stddev) / Math.sqrt(trials));
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        // n by n percolation and trials (default 10x10 and 10 trials)
        int n = 10, trials = 10;
        if (args.length >= 1)
            n = Integer.parseInt(args[0]);
        if (args.length == 2)
            trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);
        StdOut.println("mean                    = " +  ps.mean());
        StdOut.println("stddev                  = " +  ps.stddev());
        String interval = "[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]";
        StdOut.println("95% confidence interval = " +  interval);
    }
}
