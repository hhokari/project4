import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView
{
    public PApplet screen;
    public WorldModel world;
    public int tileWidth;
    public int tileHeight;
    public Viewport viewport;

    public WorldView(
            int numRows,
            int numCols,
            PApplet screen,
            WorldModel world,
            int tileWidth,
            int tileHeight)
    {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }

    public Point viewportToWorld(Viewport viewport, int col, int row) {
        return new Point(col + viewport.col, row + viewport.row);
    }

    public Point worldToViewport(Viewport viewport, int col, int row) {
        return new Point(col - viewport.col, row - viewport.row);
    }

    public void shift(Viewport viewport, int col, int row) {
        viewport.col = col;
        viewport.row = row;
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = clamp(this.viewport.col + colDelta, 0,
                this.world.numCols - this.viewport.numCols);
        int newRow = clamp(this.viewport.row + rowDelta, 0,
                this.world.numRows - this.viewport.numRows);

        this.shift(this.viewport, newCol, newRow);
    }

    public int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public void drawBackground() {
        for (int row = 0; row < this.viewport.numRows; row++) {
            for (int col = 0; col < this.viewport.numCols; col++) {
                Point worldPoint = this.viewportToWorld(this.viewport, col, row);
                Optional<PImage> image =
                        world.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth,
                            row * this.tileHeight);
                }
            }
        }
    }

    public void drawEntities() {
        for (Entity entity : this.world.entities) {
            Point pos = entity.position;

            if (viewport.contains(pos)) {
                Point viewPoint = this.worldToViewport(this.viewport, pos.x, pos.y);
                this.screen.image(Functions.getCurrentImage(entity),
                        viewPoint.x * this.tileWidth,
                        viewPoint.y * this.tileHeight);
            }
        }
    }

    public void drawViewport() {
        this.drawBackground();
        this.drawEntities();
    }
}