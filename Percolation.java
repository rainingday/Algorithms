/**
 * Coursera Algorithm I Assignment 1 Percolation
 * Problem description:
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * http://algs4.cs.princeton.edu/code/index.php#stdlib
 * Date: March 4, 2015
 * Author: Claire
 * */

public class Percolation {
    private boolean[][] grid;
    private int gridSize;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF backWash;
    private final int top;
    private final int bottom;
    
    public Percolation(int N) { // create N-by-N grid, with all sites blocked
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("failure creating N-by-N grid");
        }
        grid = new boolean[N][N];
        gridSize = N;
        top = 0;
        bottom = N*N+1;
        uf = new WeightedQuickUnionUF(N*N+1);
        backWash = new WeightedQuickUnionUF(N*N+2);
    }
    
    public void open(int i, int j) { // open site (row i, column j) if it is not open already
        validateArray(i, j);
        grid[i - 1][j - 1] = true;
        if (i == 1) {
            uf.union(top, distance1D(i, j));
            backWash.union(top, distance1D(i, j));
        }
        
        if (i == gridSize) {
            backWash.union(distance1D(i, j), bottom);
        }
        
        if (i > 1 && isOpen(i - 1, j)) {
            uf.union(distance1D(i, j), distance1D(i - 1, j));
            backWash.union(distance1D(i, j), distance1D(i - 1, j));
        }
        
        if (i < gridSize && isOpen(i + 1, j)) {
            uf.union(distance1D(i, j), distance1D(i + 1, j));
            backWash.union(distance1D(i, j), distance1D(i + 1, j));
        }
        
        if (j > 1 && isOpen(i, j - 1)) {
            uf.union(distance1D(i, j), distance1D(i, j - 1));
            backWash.union(distance1D(i, j), distance1D(i, j - 1));
        }
        
        if (j < gridSize && isOpen(i, j + 1)) {
            uf.union(distance1D(i, j), distance1D(i, j + 1));
            backWash.union(distance1D(i, j), distance1D(i, j + 1));
        }
    }
    
    public boolean isOpen(int i, int j) { // is site (row i, column j) open?
        validateArray(i, j);
        return grid[i-1][j-1];
    }
    
    public boolean isFull(int i, int j) { // is site (row i, column j) full?
        validateArray(i, j);
        return uf.connected(distance1D(i, j), top);
    }
    
    public boolean percolates() { // does the system percolate?
        return backWash.connected(top, bottom);
    }
    
    private int distance1D(int i, int j) {
        return (i - 1) * gridSize + j;
    }
    
    private void validateArray(int i, int j) {
        if (i <= 0 || j <= 0 || i > gridSize || j > gridSize) {
            throw new IndexOutOfBoundsException("index: (" + i + ", " + j + ") are out of bound!");
        }
    }
    

    public static void main(String[] args) {  // test client (optional)
        
        int N = 0;
        int i;
        int j;
        if (!StdIn.isEmpty()) {
            N = StdIn.readInt();
        }
        Percolation percolation = new Percolation(N);
        
        while (!StdIn.isEmpty()) {
            i = StdIn.readInt();
            j = StdIn.readInt();
            percolation.open(i, j);
        }
        //StdOut.println("is (i,j) Open?:" + percolation.isOpen(1,2));
        StdOut.println("is percolation?:" + percolation.percolates());
        //StdOut.println(uf.count(distance1D(i,j)) + "open sites.");
    }
}
