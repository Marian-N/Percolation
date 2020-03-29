/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private final int n;
    private final int virtualTop;
    private final int virtualBot;
    private final WeightedQuickUnionUF qu;
    private final WeightedQuickUnionUF qub;
    private int openSites = 0;

    // creates n-by-n grid, with all sites initially blocked (set to 0)
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        grid = new boolean[n][n];
        this.n = n;
        int qubSize = n * n;
        int quSize = qubSize + 2;
        qu = new WeightedQuickUnionUF(quSize);
        qub = new WeightedQuickUnionUF(qubSize + 1);
        virtualBot = quSize - 1;
        virtualTop = 0;
        for (int row = 0; row < n; row++)
            for (int column = 0; column < n; column++)
                grid[row][column] = false;
    }

    private void connect(int row, int column, int unionPosition) {
        // if it is at top column connect it to virtual top
        if (row == 1) {
            qu.union(virtualTop, unionPosition);
            qub.union(virtualTop, unionPosition);
        }
        // if it is at bottom column connect it to vritual bottom
        if (row == n) qu.union(virtualBot, unionPosition);
        // connect it to the left if possible
        if (column - 1 >= 1 && isOpen(row, column - 1)) {
            qu.union(unionPosition(row, column - 1), unionPosition);
            qub.union(unionPosition(row, column - 1), unionPosition);
        }
        // connect it to the right if possible
        if (column + 1 <= n && isOpen(row, column + 1)) {
            qu.union(unionPosition(row, column + 1), unionPosition);
            qub.union(unionPosition(row, column + 1), unionPosition);
        }
        // connect it to the bottom if possible
        if (row + 1 <= n && isOpen(row + 1, column)) {
            qu.union(unionPosition(row + 1, column), unionPosition);
            qub.union(unionPosition(row + 1, column), unionPosition);
        }
        // connect it to the top if possible
        if (row - 1 >= 1 && isOpen(row - 1, column)) {
            qu.union(unionPosition(row - 1, column), unionPosition);
            qub.union(unionPosition(row - 1, column), unionPosition);
        }
    }

    // opens the site (row, column) if it is not open already
    public void open(int row, int column) {
        checkBounds(row, column);
        if (isClosed(row, column)) {
            grid[row - 1][column - 1] = true;
            openSites++;
        }
        else return;
        int unionPosition = unionPosition(row, column);
        // connect it to neighbours if possible
        connect(row, column, unionPosition);
    }

    private boolean isClosed(int row, int column) {
        checkBounds(row, column);
        return !grid[row - 1][column - 1];
    }

    private int unionPosition(int row, int column) {
        return n * (row - 1) + column;
    }

    // is the site (row, column) open?
    public boolean isOpen(int row, int column) {
        checkBounds(row, column);
        return grid[row - 1][column - 1];
    }

    // is the site (row, column) full?
    public boolean isFull(int row, int column) {
        checkBounds(row, column);
        return qub.connected(unionPosition(row, column), virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return qu.connected(virtualBot, virtualTop);
    }

    private void checkBounds(int row, int column) {
        if (row < 1 || row > n) throw new IllegalArgumentException();
        if (column < 1 || column > n) throw new IllegalArgumentException();
    }
}
