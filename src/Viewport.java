public final class Viewport
{
    private int row;
    private int col;
    private final int NUMROWS;
    private final int NUMCOLS;

    public Viewport(int NUMROWS, int NUMCOLS) {
        this.NUMROWS = NUMROWS;
        this.NUMCOLS = NUMCOLS;
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
        return p.y >= row && p.y < row + NUMROWS
                && p.x >= col && p.x < col + NUMCOLS;
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
        return NUMROWS;
    }

    public int getNumCols()
    {
        return NUMCOLS;
    }

}
