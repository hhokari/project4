public final class Viewport
{
    private int row;
    private int col;
    private final int numRows;
    private final int numCols;

    public Viewport(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public Point viewportToWorld(Viewport viewport, int col, int row) {
        return new Point(col + viewport.col, row + viewport.row);
    }

    public Point worldToViewport(Viewport viewport, int col, int row) {
        return new Point(col - viewport.col, row - viewport.row);
    }

    public void shift(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public boolean contains(Point p) {
        return p.y >= row && p.y < row + numRows
                && p.x >= col && p.x < col + numCols;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    public int getNumRows()
    {
        return numRows;
    }

    public int getNumCols()
    {
        return numCols;
    }

}
