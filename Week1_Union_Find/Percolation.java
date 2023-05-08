import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @author Wenjie FU
 * @create 2022-02-28 21:46
 */
public class Percolation {

    private final boolean[][] matrix;
    private final int n; // n-by-n grid
    private int count = 0; // counting the number of open site
    private final WeightedQuickUnionUF wQucikUF;
    private int topIndex;
    private int bottomIndex;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;
        matrix = new boolean[n][n];
        wQucikUF = new WeightedQuickUnionUF(n * n + 2);
    }

    /**
     * opens the site (row, col) if it is not open already
     * the row and column indices are integers between 1 and n, where (1, 1) is the upper-left site:
     * Throw an IllegalArgumentException if any argument to open(), isOpen(), or isFull() is outside its prescribed range.
     * Throw an IllegalArgumentException in the constructor if n ≤ 0.
     *
     * @param row
     * @param col
     * @throws IllegalArgumentException
     */
    public void open(int row, int col) {

        validRowCol(row, col);

        if (isOpen(row, col)) {
            return;
        }

        int index = (row - 1) * n + col;
        matrix[row - 1][col - 1] = true;
        count++;

        // union up
        if (row == 1) {
            wQucikUF.union(index, 0);
        } else {
            if (isOpen(row - 1, col)) {
                wQucikUF.union(index, index - n);
            }
        }

        // union down
        if (row == n) {
            wQucikUF.union(index, n * n + 1);
        } else {
            if (isOpen(row + 1, col)) {
                wQucikUF.union(index, index + n);
            }
        }

        // union left
        if (col != 1 && isOpen(row, col - 1)) {
            wQucikUF.union(index, index - 1);
        }

        // union right
        if (col != n && isOpen(row, col + 1)) {
            wQucikUF.union(index, index + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validRowCol(row, col);
        return matrix[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validRowCol(row, col);

        if (!isOpen(row, col)) {
            return false;
        }

        return wQucikUF.find((row - 1) * n + col) == 0;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return wQucikUF.find(0) == wQucikUF.find(n * n + 1);
    }

    private void validRowCol(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException();
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(3, 1);

        System.out.println("if percolation : " + percolation.percolates());
    }
}