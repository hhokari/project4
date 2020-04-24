import processing.core.PImage;

import java.util.*;

public final class WorldModel
{
    public int numRows;
    public int numCols;
    public Background background[][];
    public Entity occupancy[][];
    public Set<Entity> entities;
    public static final int ORE_REACH = 1;
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public void setBackgroundCell(
            WorldModel world, Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    public Background getBackgroundCell(WorldModel world, Point pos) {
        return world.background[pos.y][pos.x];
    }

    public void setOccupancyCell(
            WorldModel world, Point pos, Entity entity)
    {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public Entity getOccupancyCell(WorldModel world, Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public boolean withinBounds(WorldModel world, Point pos) {
        return pos.y >= 0 && pos.y < world.numRows && pos.x >= 0
                && pos.x < world.numCols;
    }

    public boolean isOccupied(WorldModel world, Point pos) {
        return this.withinBounds(world, pos) && this.getOccupancyCell(world, pos) != null;
    }

//    public Optional<Entity> findNearest(
//            WorldModel world, Point pos, EntityKind kind)
//    {
//        List<Entity> ofType = new LinkedList<>();
//        for (Entity entity : this.entities) {
//            if (entity.kind == kind) {
//                ofType.add(entity);
//            }
//        }
//        return nearestEntity(ofType, pos);
//    }

    public void tryAddEntity(WorldModel world, Entity entity) {
        if (this.isOccupied(world, entity.position)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(world, entity);
    }

    public void addEntity(WorldModel world, Entity entity) {
        if (world.withinBounds(world, entity.position)) {
            world.setOccupancyCell(world, entity.position, entity);
            world.entities.add(entity);
        }
    }

    public void setBackground(
            WorldModel world, Point pos, Background background)
    {
        if (world.withinBounds(world, pos)) {
            world.setBackgroundCell(world, pos, background);
        }
    }

    public Optional<Entity> getOccupant(WorldModel world, Point pos) {
        if (world.isOccupied(world, pos)) {
            return Optional.of(world.getOccupancyCell(world, pos));
        }
        else {
            return Optional.empty();
        }
    }

    public void moveEntity(WorldModel world, Entity entity, Point pos) {
        Point oldPos = entity.position;
        if (world.withinBounds(world, pos) && !pos.equals(oldPos)) {
            world.setOccupancyCell(world, oldPos, null);
            world.removeEntityAt(world, pos);
            world.setOccupancyCell(world, pos, entity);
            entity.position = pos;
        }
    }

    public void removeEntity(WorldModel world, Entity entity) {
        removeEntityAt(world, entity.position);
    }

    public void removeEntityAt(WorldModel world, Point pos) {
        if (world.withinBounds(world, pos) && world.getOccupancyCell(world, pos) != null) {
            Entity entity = world.getOccupancyCell(world, pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.position = new Point(-1, -1);
            world.entities.remove(entity);
            world.setOccupancyCell(world, pos, null);
        }
    }

    public Optional<Point> findOpenAround(WorldModel world, Point pos) {
        for (int dy = -ORE_REACH; dy <= ORE_REACH; dy++) {
            for (int dx = -ORE_REACH; dx <= ORE_REACH; dx++) {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (world.withinBounds(world, newPt) && !world.isOccupied(world, newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public Entity createBlacksmith(
            String id, Point position, List<PImage> images)
    {
        return new Entity(EntityKind.BLACKSMITH, id, position, images, 0, 0, 0,
                0);
    }

    public Entity createMinerNotFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Entity(EntityKind.MINER_NOT_FULL, id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public Entity createObstacle(
            String id, Point position, List<PImage> images)
    {
        return new Entity(EntityKind.OBSTACLE, id, position, images, 0, 0, 0,
                0);
    }

    public Entity createOre(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Entity(EntityKind.ORE, id, position, images, 0, 0,
                actionPeriod, 0);
    }

    public Entity createOreBlob(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Entity(EntityKind.ORE_BLOB, id, position, images, 0, 0,
                actionPeriod, animationPeriod);
    }

    public Entity createQuake(
            Point position, List<PImage> images)
    {
        return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images, 0, 0,
                QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public Entity createVein(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Entity(EntityKind.VEIN, id, position, images, 0, 0,
                actionPeriod, 0);
    }
}
