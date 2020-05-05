import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView
{
    private final PApplet SCREEN;
    private final WorldModel WORLD;
    private final int TILEWIDTH;
    private final int TILEHEIGHT;
    private final Viewport VIEWPORT;

    public WorldView(
            int NUMROWS,
            int NUMCOLS,
            PApplet SCREEN,
            WorldModel WORLD,
            int TILEWIDTH,
            int TILEHEIGHT)
    {
        this.SCREEN = SCREEN;
        this.WORLD = WORLD;
        this.TILEWIDTH = TILEWIDTH;
        this.TILEHEIGHT = TILEHEIGHT;
        this.VIEWPORT = new Viewport(NUMROWS, NUMCOLS);
    }

    public void shiftView(int colDelta, int rowDelta) {
        int newCol = clamp(VIEWPORT.getCol() + colDelta, 0,
                WORLD.getnumCols() - VIEWPORT.getNumCols());
        int newRow = clamp(VIEWPORT.getRow() + rowDelta, 0,
                WORLD.getnumRows() - VIEWPORT.getNumRows());

        VIEWPORT.shift(newCol, newRow);
    }

    private int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    private void drawBackground() {
        for (int row = 0; row < VIEWPORT.getNumRows(); row++) {
            for (int col = 0; col < VIEWPORT.getNumCols(); col++) {
                Point worldPoint = VIEWPORT.viewportToWorld(VIEWPORT, col, row);
                Optional<PImage> image =
                        WORLD.getBackgroundImage(worldPoint);
                if (image.isPresent()) {
                    SCREEN.image(image.get(), col * TILEWIDTH,
                            row * TILEHEIGHT);
                }
            }
        }
    }

    private void drawEntities() {
        for (Entity entity : WORLD.getentities()) {
            Point pos = entity.getPosition();

            if (VIEWPORT.contains(pos)) {
                Point viewPoint = VIEWPORT.worldToViewport(VIEWPORT, pos.X, pos.Y);
                SCREEN.image(entity.getCurrentImage(),
                        viewPoint.X * TILEWIDTH,
                        viewPoint.Y * TILEHEIGHT);
            }
        }
    }

    public void drawViewport() {
        drawBackground();
        drawEntities();
    }
}