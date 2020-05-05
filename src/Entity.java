import java.util.List;
import java.util.Optional;
import java.util.Random;

import processing.core.PImage;

import javax.naming.RefAddr;
import javax.security.auth.login.AccountNotFoundException;

public final class Entity
{
    private final EntityKind KIND;
    private final String ID;
    private Point position;
    private final List<PImage> IMAGES ;
    private int imageIndex;
    private final int RESOURCELIMIT;
    private int resourceCount;
    private final int ACTIONPERIOD;
    private final int ANIMATIONPERIOD;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    private static final String ORE_ID_PREFIX = "ore -- ";
    private static final int ORE_CORRUPT_MIN = 20000;
    private static final Random RAND = new Random();
    private static final int ORE_CORRUPT_MAX = 30000;
    public static final String ORE_KEY = "ore";
    private static final String BLOB_KEY = "blob";
    private static final String BLOB_ID_SUFFIX = " -- blob";
    private static final int BLOB_PERIOD_SCALE = 4;
    private static final int BLOB_ANIMATION_MIN = 50;
    private static final int BLOB_ANIMATION_MAX = 150;
    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public Entity(
            EntityKind KIND,
            String ID,
            Point position,
            List<PImage> IMAGES,
            int RESOURCELIMIT,
            int resourceCount,
            int ACTIONPERIOD,
            int ANIMATIONPERIOD)
    {
        this.KIND = KIND;
        this.ID = ID;
        this.position = position;
        this.IMAGES = IMAGES;
        this.imageIndex = 0;
        this.RESOURCELIMIT = RESOURCELIMIT;
        this.resourceCount = resourceCount;
        this.ACTIONPERIOD = ACTIONPERIOD;
        this.ANIMATIONPERIOD = ANIMATIONPERIOD;
    }

    private void removeEntity(WorldModel world) {
        world.removeEntityAt(this.position);
    }

    private Point nextPositionOreBlob(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.X - position.X);
        Point newPos = new Point(position.X + horiz, position.Y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getKind()
                == EntityKind.ORE)))
        {
            int vert = Integer.signum(destPos.Y - position.Y);
            newPos = new Point(position.X, position.Y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getKind()
                    == EntityKind.ORE)))
            {
                newPos = position;
            }
        }

        return newPos;
    }

    private Point nextPositionMiner(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.X - position.X);
        Point newPos = new Point(position.X + horiz, position.Y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.Y - position.Y);
            newPos = new Point(position.X, position.Y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = position;
            }
        }

        return newPos;
    }

    private boolean moveToOreBlob(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(position, target.position)) {
            target.removeEntity(world);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPositionOreBlob(world, target.position);

            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private boolean moveToFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(position, target.position)) {
            return true;
        }
        else {
            Point nextPos = nextPositionMiner(world, target.position);

            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private boolean moveToNotFull(
            WorldModel world,
            Entity target,
            EventScheduler scheduler)
    {
        if (Functions.adjacent(position, target.position)) {
            resourceCount += 1;
            target.removeEntity(world);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = nextPositionMiner(world, target.position);

            if (!position.equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    private void transformFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        Entity miner = createMinerNotFull(ID, RESOURCELIMIT,
                position, ACTIONPERIOD,
                ANIMATIONPERIOD,
                IMAGES);

        removeEntity(world);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }

    private boolean transformNotFull(
            WorldModel world,
            EventScheduler scheduler,
            ImageStore imageStore)
    {
        if (resourceCount >= RESOURCELIMIT) {
            Entity miner = createMinerFull(ID, RESOURCELIMIT,
                    position, ACTIONPERIOD,
                    ANIMATIONPERIOD,
                    IMAGES);

            removeEntity(world);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void scheduleActions(
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        switch (getKind()) {
            case MINER_FULL:
                scheduler.scheduleEvent(this,
                        Action.createActivityAction(this, world, imageStore),
                        ACTIONPERIOD);
                scheduler.scheduleEvent(this,
                        Action.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case MINER_NOT_FULL:
                scheduler.scheduleEvent(this,
                        Action.createActivityAction(this, world, imageStore),
                        ACTIONPERIOD);
                scheduler.scheduleEvent(this,
                        Action.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case ORE:
                scheduler.scheduleEvent(this,
                        Action.createActivityAction(this, world, imageStore),
                        ACTIONPERIOD);
                break;

            case ORE_BLOB:
                scheduler.scheduleEvent(this,
                        Action.createActivityAction(this, world, imageStore),
                        ACTIONPERIOD);
                scheduler.scheduleEvent(this,
                        Action.createAnimationAction(this, 0),
                        getAnimationPeriod());
                break;

            case QUAKE:
                scheduler.scheduleEvent(this,
                        Action.createActivityAction(this , world, imageStore),
                        ACTIONPERIOD);
                scheduler.scheduleEvent(this, Action.createAnimationAction(this,
                        QUAKE_ANIMATION_REPEAT_COUNT),
                        getAnimationPeriod());
                break;

            case VEIN:
                scheduler.scheduleEvent(this,
                        Action.createActivityAction(this, world, imageStore),
                        ACTIONPERIOD);
                break;

            default:
        }
    }

    public void executeVeinActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(position);

        if (openPt.isPresent()) {
            Entity ore = createOre(ORE_ID_PREFIX + ID, openPt.get(),
                    ORE_CORRUPT_MIN + RAND.nextInt(
                            ORE_CORRUPT_MAX - ORE_CORRUPT_MIN),
                    imageStore.getImageList(ORE_KEY));
            world.addEntity(ore);
            ore.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Action.createActivityAction(this, world, imageStore),
                ACTIONPERIOD);
    }

    public void executeQuakeActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        removeEntity(world);
    }

    public void executeOreBlobActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> blobTarget =
                world.findNearest(position, EntityKind.VEIN);
        long nextPeriod = ACTIONPERIOD;

        if (blobTarget.isPresent()) {
            Point tgtPos = blobTarget.get().position;

            if (moveToOreBlob(world, blobTarget.get(), scheduler)) {
                Entity quake = createQuake(tgtPos,
                        imageStore.getImageList(Functions.QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += ACTIONPERIOD;
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                Action.createActivityAction(this, world, imageStore),
                nextPeriod);
    }

    public void executeOreActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Point pos = position;

        removeEntity(world);
        scheduler.unscheduleAllEvents(this);

        Entity blob = createOreBlob(ID + BLOB_ID_SUFFIX, pos,
                ACTIONPERIOD / BLOB_PERIOD_SCALE,
                BLOB_ANIMATION_MIN + RAND.nextInt(
                        BLOB_ANIMATION_MAX
                                - BLOB_ANIMATION_MIN),
                imageStore.getImageList(BLOB_KEY));

        world.addEntity(blob);
        blob.scheduleActions(scheduler, world, imageStore);
    }

    public void executeMinerNotFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget =
                world.findNearest(position, EntityKind.ORE);

        if (!notFullTarget.isPresent() || !this.moveToNotFull(world, notFullTarget.get(),
                scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    Action.createActivityAction(this, world, imageStore),
                    ACTIONPERIOD);
        }
    }

    public void executeMinerFullActivity(
            WorldModel world,
            ImageStore imageStore,
            EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
                world.findNearest(position, EntityKind.BLACKSMITH);

        if (fullTarget.isPresent() && moveToFull(world, fullTarget.get(),
                scheduler))
        {
            transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    Action.createActivityAction(this, world, imageStore),
                    ACTIONPERIOD);
        }
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % IMAGES.size();
    }

    public int getAnimationPeriod() {
        switch (KIND) {
            case MINER_FULL:
            case MINER_NOT_FULL:
            case ORE_BLOB:
            case QUAKE:
                return ANIMATIONPERIOD;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                getKind()));
        }
    }

    public PImage getCurrentImage() {
        return (IMAGES.get(imageIndex));
    }


    public static Entity createBlacksmith(
            String id, Point position, List<PImage> images)
    {
        return new Entity(EntityKind.BLACKSMITH, id, position, images, 0, 0, 0,
                0);
    }

    public static Entity createMinerFull(
            String id,
            int resourceLimit,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Entity(EntityKind.MINER_FULL, id, position, images,
                resourceLimit, resourceLimit, actionPeriod,
                animationPeriod);
    }

    public static Entity createMinerNotFull(
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

    public static Entity createObstacle(
            String id, Point position, List<PImage> images)
    {
        return new Entity(EntityKind.OBSTACLE, id, position, images, 0, 0, 0,
                0);
    }

    public static Entity createOre(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Entity(EntityKind.ORE, id, position, images, 0, 0,
                actionPeriod, 0);
    }

    private static Entity createOreBlob(
            String id,
            Point position,
            int actionPeriod,
            int animationPeriod,
            List<PImage> images)
    {
        return new Entity(EntityKind.ORE_BLOB, id, position, images, 0, 0,
                actionPeriod, animationPeriod);
    }

    private static Entity createQuake(
            Point position, List<PImage> images)
    {
        return new Entity(EntityKind.QUAKE, QUAKE_ID, position, images, 0, 0,
                QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public static Entity createVein(
            String id, Point position, int actionPeriod, List<PImage> images)
    {
        return new Entity(EntityKind.VEIN, id, position, images, 0, 0,
                actionPeriod, 0);
    }

    public EntityKind getKind()
    {
        return KIND;
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }
}