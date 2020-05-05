import processing.core.PImage;

import java.util.*;

public final class WorldModel
{
    private final int NUMROWS;
    private final int NUMCOLS;
    private final Background BACKGROUND[][];
    private final Entity OCCUPANCY[][];
    private final Set<Entity> ENTITIES;
    private static final int ORE_REACH = 1;

    public WorldModel(int NUMROWS, int NUMCOLS, Background defaultBackground) {
        this.NUMROWS = NUMROWS;
        this.NUMCOLS = NUMCOLS;
        this.BACKGROUND = new Background[NUMROWS][NUMCOLS];
        this.OCCUPANCY = new Entity[NUMROWS][NUMCOLS];
        this.ENTITIES = new HashSet<>();

        for (int row = 0; row < NUMROWS; row++) {
            Arrays.fill(this.BACKGROUND[row], defaultBackground);
        }
    }

    private void setBackgroundCell(
            Point pos, Background background)
    {
        this.BACKGROUND[pos.Y][pos.X] = background;
    }

    private Background getBackgroundCell(Point pos) {
        return BACKGROUND[pos.Y][pos.X];
    }

    private Entity getOccupancyCell(Point pos) {
        return OCCUPANCY[pos.Y][pos.X];
    }

    private void setOccupancyCell(
            Point pos, Entity entity)
    {
        OCCUPANCY[pos.Y][pos.X] = entity;
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    private boolean withinBounds(Point pos) {
        return pos.Y >= 0 && pos.Y < NUMROWS && pos.X >= 0
                && pos.X < NUMCOLS;
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    public void setBackground(
            Point pos, Background background)
    {
        if (withinBounds(pos)) {
            setBackgroundCell(pos, background);
        }
    }

    public Optional<PImage> getBackgroundImage(
            Point pos)
    {
        if (withinBounds(pos)) {
            return Optional.of(getBackgroundCell(pos).getCurrentImage());
        }
        else {
            return Optional.empty();
        }
    }

    public void removeEntity(Entity entity) {
        removeEntityAt(entity.position);
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            ENTITIES.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);
        }
    }

    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            ENTITIES.add(entity);
        }
    }

    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    public Optional<Point> findOpenAround(Point pos) {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
                Point newPt = new Point(pos.X + dx, pos.Y + dy);
                if (withinBounds(newPt) && !isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public Optional<Entity> findNearest(
            Point pos, EntityKind kind)
    {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : ENTITIES) {
            if (entity.getKind() == kind) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    private static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    private static int distanceSquared(Point p1, Point p2) {
        int deltaX = p1.X - p2.X;
        int deltaY = p1.Y - p2.Y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public int getnumRows()
    {
        return NUMROWS;
    }

    public int getnumCols() {
        return NUMCOLS;
    }

    public Set<Entity> getentities()
    {
        return ENTITIES;
    }
}