public final class Viewport
{
    public int row;
    public int col;
    public int numRows;
    public int numCols;

    public Viewport(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public Point viewportToWorld(Viewport viewport, int col, int row) {
        return new Point(col + this.col, row + this.row);
    }

    public Point worldToViewport(Viewport viewport, int col, int row) {
        return new Point(col - this.col, row - this.row);
    }

    public void shift(Viewport viewport, int col, int row) {
        viewport.col = col;
        viewport.row = row;
    }

    public boolean contains(Viewport viewport, Point p) {
        return p.y >= viewport.row && p.y < viewport.row + this.numRows
                && p.x >= viewport.col && p.x < viewport.col + this.numCols;
    }
}
