import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Obstacle implements Entity {
    private final String ID;
    private Point position;
    private final List<PImage> IMAGES ;
    private int imageIndex;
    private final int RESOURCELIMIT;
    private int resourceCount;
    private final int ACTIONPERIOD;
    private final int ANIMATIONPERIOD;
//    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
//    private static final String ORE_ID_PREFIX = "ore -- ";
//    private static final int ORE_CORRUPT_MIN = 20000;
//    private static final Random RAND = new Random();
//    private static final int ORE_CORRUPT_MAX = 30000;
    public static final String ORE_KEY = "ore";
//    private static final String BLOB_KEY = "blob";
//    private static final String BLOB_ID_SUFFIX = " -- blob";
//    private static final int BLOB_PERIOD_SCALE = 4;
//    private static final int BLOB_ANIMATION_MIN = 50;
//    private static final int BLOB_ANIMATION_MAX = 150;
//    private static final String QUAKE_ID = "quake";
//    private static final int QUAKE_ACTION_PERIOD = 1100;
//    private static final int QUAKE_ANIMATION_PERIOD = 100;

    public Obstacle(
            String ID,
            Point position,
            List<PImage> IMAGES,
            int RESOURCELIMIT,
            int resourceCount,
            int ACTIONPERIOD,
            int ANIMATIONPERIOD)
    {
        this.ID = ID;
        this.position = position;
        this.IMAGES = IMAGES;
        this.imageIndex = 0;
        this.RESOURCELIMIT = RESOURCELIMIT;
        this.resourceCount = resourceCount;
        this.ACTIONPERIOD = ACTIONPERIOD;
        this.ANIMATIONPERIOD = ANIMATIONPERIOD;
    }

    private Point nextPositionOreBlob(
            WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.X - position.X);
        Point newPos = new Point(position.X + horiz, position.Y);

        Optional<Entity> occupant = world.getOccupant(newPos);

        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                == Ore.class)))
        {
            int vert = Integer.signum(destPos.Y - position.Y);
            newPos = new Point(position.X, position.Y + vert);
            occupant = world.getOccupant(newPos);

            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass()
                    == Ore.class)))
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
        if (Functions.adjacent(position, target.getPosition())) {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else {
            Point nextPos = nextPositionOreBlob(world, target.getPosition());

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
        if (Functions.adjacent(position, target.getPosition())) {
            return true;
        }
        else {
            Point nextPos = nextPositionMiner(world, target.getPosition());

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
        if (Functions.adjacent(position, target.getPosition())) {
            resourceCount += 1;
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);

            return true;
        }
        else {
            Point nextPos = nextPositionMiner(world, target.getPosition());

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
        Miner_Not_Full miner = Factory.createMinerNotFull(ID, RESOURCELIMIT,
                position, ACTIONPERIOD,
                ANIMATIONPERIOD,
                IMAGES);

        world.removeEntity(miner);
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
            Miner_Full miner = Factory.createMinerFull(ID, RESOURCELIMIT,
                    position, ACTIONPERIOD,
                    ANIMATIONPERIOD,
                    IMAGES);

            world.removeEntity(miner);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % IMAGES.size();
    }

    public PImage getCurrentImage() {
        return (IMAGES.get(imageIndex));
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }

//    public int getAnimationPeriod() {
//        switch (KIND) {
//            case MINER_FULL:
//            case MINER_NOT_FULL:
//            case ORE_BLOB:
//            case QUAKE:
//                return ANIMATIONPERIOD;
//            default:
//                throw new UnsupportedOperationException(
//                        String.format("getAnimationPeriod not supported for %s",
//                                getKind()));
//        }
//    }
}
